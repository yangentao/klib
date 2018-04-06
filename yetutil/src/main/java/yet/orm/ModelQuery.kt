package yet.orm

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import yet.database.SQLType
import yet.ext.customName
import yet.ext.customNamePrefixClass
import yet.util.closeAfter
import yet.util.database.CursorResult
import yet.util.database.SafeCursor
import yet.util.debug
import yet.util.log.log
import yet.util.log.logd
import yet.util.log.loge
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2016-12-14.
 */


class ModelQuery(val db: SQLiteDatabase, val fromKClass: KClass<*>, vararg val otherKCls: KClass<*>) {
	private val modelInfo = ModelInfo.find(fromKClass)

	private var fromStr: String = ""
	private var whereStr: String = ""
	private val orderArr = arrayListOf<String>()
	private var limitStr: String = ""
	private var joinStr: String = ""
	private var onStr: String = ""
	private var selectStr: String = ""

	private val args: ArrayList<Any> = ArrayList()

	init {
		var s = "FROM " + modelInfo.tableName
		for (c in otherKCls) {
			val mi = ModelInfo.find(c)
			s += "," + mi.tableName
		}
		fromStr = s
	}

	fun toCountSQL(): String {
		val sb = StringBuilder(256)
		sb.append("SELECT COUNT(*) ")
		sb.append(fromStr).append(" ")

		if (joinStr.isNotEmpty() && onStr.isNotEmpty()) {
			sb.append(joinStr).append(" ").append(onStr).append(" ")
		}
		if (whereStr.isNotEmpty()) {
			sb.append(whereStr).append(" ")
		}
		if (limitStr.isNotEmpty()) {
			sb.append(limitStr).append(" ")
		}
		return sb.toString()
	}

	fun toSQL(): String {
		val sb = StringBuilder(256)
		if (selectStr.isEmpty()) {
			sb.append("SELECT * ")
		} else {
			sb.append(selectStr).append(" ")
		}
		sb.append(fromStr).append(" ")

		if (joinStr.isNotEmpty() && onStr.isNotEmpty()) {
			sb.append(joinStr).append(" ").append(onStr).append(" ")
		}

		if (whereStr.isNotEmpty()) {
			sb.append(whereStr).append(" ")
		}
		if (orderArr.isNotEmpty()) {
			sb.append("ORDER BY ").append(orderArr.joinToString(",")).append(" ")
		}
		if (limitStr.isNotEmpty()) {
			sb.append(limitStr).append(" ")
		}
		return sb.toString()
	}

	fun join(joinKClass: KClass<*>): ModelQuery {
		joinStr = "JOIN " + joinKClass.customName
		return this
	}

	fun on(left: KMutableProperty<*>, right: KMutableProperty<*>): ModelQuery {
		val s = left.customNamePrefixClass
		val s2 = right.customNamePrefixClass
		onStr = "ON $s=$s2"
		return this
	}

	fun select(vararg cols: KMutableProperty<*>): ModelQuery {
		var s = ""
		for (c in cols) {
			if (s.isEmpty()) {
				s = c.customNamePrefixClass
			} else {
				s += "," + c.customNamePrefixClass
			}
		}
		if (s.isEmpty()) {
			s = "*"
		}
		selectStr = "SELECT " + s
		return this
	}

	fun where(block: () -> Where): ModelQuery {
		val w = block.invoke()
		return where(w)
	}

	fun where(w: Where?): ModelQuery {
		if(w != null) {
			whereStr = "WHERE " + w.value
			args.addAll(w.args)
		}
		return this
	}

	fun orderBy(block: OrderBy.() -> Unit): ModelQuery {
		val ob = OrderBy()
		ob.block()
		orderArr.addAll(ob.orderArr)
		return this
	}

	fun orderBy(p: KProperty<*>, ascDesc: String): ModelQuery {
		orderArr.add(p.customNamePrefixClass + " $ascDesc")
		return this
	}

	fun asc(p: KProperty<*>): ModelQuery {
		orderArr.add(p.customNamePrefixClass + " ASC")
		return this
	}

	fun desc(p: KProperty<*>): ModelQuery {
		orderArr.add(p.customNamePrefixClass + " DESC")
		return this
	}


	fun limit(limit: Int): ModelQuery {
		if (limit > 0) {
			limitStr = " LIMIT $limit"
		}
		return this
	}

	fun limit(limit: Int, offset: Int): ModelQuery {
		if (limit > 0 && offset >= 0) {
			limitStr = " LIMIT $limit OFFSET $offset"
		}
		return this
	}

	fun query(): SafeCursor {
		val arr = args.map(Any::toString).toTypedArray()
		val sql = toSQL()
		if (debug) {
			log(sql)
			logd(arr)
		}
		val c = db.rawQuery(sql, arr)
		return SafeCursor(c)
	}

	fun queryCount(): Int {
		val arr = args.map(Any::toString).toTypedArray()
		val sql = toCountSQL()
		if (debug) {
			log(sql)
			logd(arr)
		}
		val c = db.rawQuery(sql, arr) ?: return 0
		return CursorResult(c).intValue() ?: 0
	}

	fun query(block: (SafeCursor) -> Unit) {
		val c = query()
		block(c)
		c.close()
	}

	fun queryResult(): CursorResult {
		return CursorResult(query())
	}

	fun eachRow(block: (SafeCursor) -> Unit) {
		val c = query()
		while (c.moveToNext()) {
			block(c)
		}
		c.close()
	}

	fun firstRow(block: (SafeCursor) -> Unit) {
		val c = query()
		if (c.moveToNext()) {
			block(c)
		}
		c.close()
	}

	//单表
	fun dump() {
		val c = query()
		CursorResult(c).dump()
	}

	//单表
	fun <T> one(): T? {
		limit(1)
		val c = query()
		c.closeAfter {
			if (c.moveToNext()) {
				return mapRow(c, modelInfo.createInstance()) as T
			}
		}
		return null
	}

	//单表
	fun <T> all(): ArrayList<T> {
		val c = query()
		val ls = ArrayList<T>(c.count + 8)
		c.closeAfter {
			while (c.moveToNext()) {
				val m = mapRow(c, modelInfo.createInstance())
				ls.add(m as T)
			}
		}
		return ls
	}

	//单表
	private fun mapRow(cursor: Cursor, model: Any): Any {
		for (name in cursor.columnNames) {
			val pi = modelInfo.shortNamePropMap[name] ?: continue
			val convert = pi.convert
			val index = cursor.getColumnIndex(name)
			val ctype = cursor.getType(index)
			if (ctype == Cursor.FIELD_TYPE_NULL) {
				convert.fromSqlNull(model, pi.prop)
			} else if (ctype == Cursor.FIELD_TYPE_INTEGER) {
				if (convert.sqlType == SQLType.INTEGER) {
					convert.fromSqlInteger(model, pi.prop, cursor.getLong(index))
				} else {
					loge("数据库类型不匹配${pi.fullName}")
				}
			} else if (ctype == Cursor.FIELD_TYPE_STRING) {
				if (convert.sqlType == SQLType.TEXT) {
					convert.fromSqlText(model, pi.prop, cursor.getString(index))
				} else {
					loge("数据库类型不匹配${pi.fullName}")
				}
			} else if (ctype == Cursor.FIELD_TYPE_FLOAT) {
				if (convert.sqlType == SQLType.REAL) {
					convert.fromSqlReal(model, pi.prop, cursor.getDouble(index))
				} else {
					loge("数据库类型不匹配${pi.fullName}")
				}
			} else if (ctype == Cursor.FIELD_TYPE_BLOB) {
				if (convert.sqlType == SQLType.BLOB) {
					convert.fromSqlBlob(model, pi.prop, cursor.getBlob(index))
				} else {
					loge("数据库类型不匹配${pi.fullName}")
				}
			}
		}
		return model
	}
}
package net.yet.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import net.yet.ext.len
import net.yet.orm.SqliteType
import net.yet.util.closeAfter
import net.yet.util.database.SafeCursor
import net.yet.util.log.loge
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2016-12-14.
 */


class KQuery(val db: SQLiteDatabase, val fromKClass: KClass<*>, vararg val otherKCls: KClass<*>) {
	private val modelInfo = ModelInfo.find(fromKClass)

	private var fromStr: String = ""
	private var whereStr: String = ""
	private var orderStr: String = ""
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
		if (limitStr.len > 0) {
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
		if (orderStr.isNotEmpty()) {
			sb.append(orderStr).append(" ")
		}
		if (limitStr.len > 0) {
			sb.append(limitStr).append(" ")
		}
		return sb.toString()
	}

	fun join(joinKClass: KClass<*>): KQuery {
		joinStr = "JOIN " + tableNameOf(joinKClass)
		return this
	}

	fun on(left: KMutableProperty<*>, right: KMutableProperty<*>): KQuery {
		val s = tableAndFieldNameOf(left)
		val s2 = tableAndFieldNameOf(right)
		onStr = "ON $s=$s2"
		return this
	}

	fun select(vararg cols: KMutableProperty<*>): KQuery {
		var s = ""
		for (c in cols) {
			if (s.isEmpty()) {
				s = tableAndFieldNameOf(c)
			} else {
				s += "," + tableAndFieldNameOf(c)
			}
		}
		if (s.isEmpty()) {
			s = "*"
		}
		selectStr = "SELECT " + s
		return this
	}

	fun where(block: () -> Where): KQuery {
		val w = block.invoke()
		return where(w)
	}

	fun where(w: Where): KQuery {
		whereStr = "WHERE " + w.value
		args.addAll(w.args)
		return this
	}

	fun asc(p: KMutableProperty<*>): KQuery {
		if (orderStr.isEmpty()) {
			orderStr = "ORDER BY " + tableAndFieldNameOf(p) + " ASC"
		} else {
			orderStr += ", " + tableAndFieldNameOf(p) + " ASC"
		}
		return this
	}

	fun desc(p: KMutableProperty<*>): KQuery {
		if (orderStr.isEmpty()) {
			orderStr = "ORDER BY " + tableAndFieldNameOf(p) + " DESC"
		} else {
			orderStr += ", " + tableAndFieldNameOf(p) + " DESC"
		}

		return this
	}

	fun limit(limit: Int): KQuery {
		if (limit > 0) {
			limitStr = " LIMIT $limit"
		}
		return this
	}

	fun limit(limit: Int, offset: Int): KQuery {
		if (limit > 0 && offset >= 0) {
			limitStr = " LIMIT $limit OFFSET $offset"
		}
		return this
	}

	fun query(): SafeCursor {
		val argArr = Array<String>(args.size) { args[it].toString() }
		val c = db.rawQuery(toSQL(), argArr)
		return SafeCursor(c)
	}

	fun queryCount(): Int {
		val argArr = Array<String>(args.size) { args[it].toString() }
		val c = db.rawQuery(toCountSQL(), argArr) ?: return 0
		return KCursorResult(c).resultCount()
	}

	fun <T> one(): T? {
		limit(1)
		val c = query()
		c.closeAfter {
			if (c.moveToNext()) {
				return mapRow(c, modelInfo, modelInfo.createInstance()) as T
			}
		}
		return null
	}

	fun <T> all(): ArrayList<T> {
		val c = query()
		val ls = ArrayList<T>(c.count + 8)
		c.closeAfter {
			while (c.moveToNext()) {
				val m = mapRow(c, modelInfo, modelInfo.createInstance())
				ls.add(m as T)
			}
		}
		return ls
	}


	private fun mapRow(cursor: Cursor, modelInfo: ModelInfo, model: Any): Any {
		for (name in cursor.columnNames) {
			val pi = modelInfo.namePropMap[name] ?: continue
			val convert = pi.convert
			val index = cursor.getColumnIndex(name)
			val ctype = cursor.getType(index)
			if (ctype == Cursor.FIELD_TYPE_NULL) {
				convert.fromSqlNull(model, pi.prop)
			} else if (ctype == Cursor.FIELD_TYPE_INTEGER) {
				if (convert.sqlType == SqliteType.INTEGER) {
					convert.fromSqlInteger(model, pi.prop, cursor.getLong(index))
				} else {
					loge("数据库类型不匹配${pi.name}")
				}
			} else if (ctype == Cursor.FIELD_TYPE_STRING) {
				if (convert.sqlType == SqliteType.TEXT) {
					convert.fromSqlText(model, pi.prop, cursor.getString(index))
				} else {
					loge("数据库类型不匹配${pi.name}")
				}
			} else if (ctype == Cursor.FIELD_TYPE_FLOAT) {
				if (convert.sqlType == SqliteType.REAL) {
					convert.fromSqlReal(model, pi.prop, cursor.getDouble(index))
				} else {
					loge("数据库类型不匹配${pi.name}")
				}
			} else if (ctype == Cursor.FIELD_TYPE_BLOB) {
				if (convert.sqlType == SqliteType.BLOB) {
					convert.fromSqlBlob(model, pi.prop, cursor.getBlob(index))
				} else {
					loge("数据库类型不匹配${pi.name}")
				}
			}
		}
		return model
	}
}
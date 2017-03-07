package net.yet.sqlite

import android.database.sqlite.SQLiteDatabase
import net.yet.ext.len
import net.yet.util.database.SafeCursor
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2016-12-14.
 */


class KQuery(val db: SQLiteDatabase, val fromKClass: KClass<*>, vararg val otherKCls: KClass<*>) {
	private var fromStr: String = ""
	private var whereStr: String = ""
	private var orderStr: String = ""
	private var limitStr: String = ""
	private var joinStr: String = ""
	private var onStr: String = ""
	private var selectStr: String = ""

	private val args: ArrayList<Any> = ArrayList()

	init {
		var s = "FROM " + tableNameOf(fromKClass)
		for (c in otherKCls) {
			s += "," + tableNameOf(c)
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
}
package net.yet.korm

import android.database.sqlite.SQLiteDatabase
import net.yet.ext.len
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2016-12-14.
 */


class KQuery<T : Any>(val db: SQLiteDatabase, val fromKClass: KClass<T>, vararg val otherKCls: KClass<*>) {
	private var fromStr: String = ""
	private var whereStr: String = ""
	private var orderStr: String = ""
	private var limitStr: String = ""
	private var joinStr: String = ""
	private var onStr: String = ""
	private var selectStr: String = ""

	private val args: ArrayList<Any> = ArrayList()

	init {
		var s = "FROM " + tableNameOf(fromKClass.java)
		for (c in otherKCls) {
			s += "," + tableNameOf(c.java)
		}
		fromStr = s
	}

	fun toCountSQL(): String {
		var s = " SELECT COUNT(*) $fromStr "
		if (whereStr.len > 0) {
			s += " WHERE $whereStr "
		}
		if (limitStr.len > 0) {
			s += " $limitStr "
		}
		return s
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

	fun join(joinKClass: KClass<*>): KQuery<T> {
		joinStr = "JOIN " + tableNameOf(joinKClass.java)
		return this
	}

	fun on(cond: OnCond): KQuery<T> {
		onStr = "ON " + cond.toString()
		return this
	}

	fun select(vararg cols: KProperty<*>): KQuery<T> {
		var s = ""
		for (c in cols) {
			if (s.isEmpty()) {
				s = fieldNameOf(c)
			} else {
				s += "," + fieldNameOf(c)
			}
		}
		if (s.isEmpty()) {
			s = "*"
		}
		selectStr = "SELECT " + s
		return this
	}

	fun where(block: () -> KWhere): KQuery<T> {
		val w = block.invoke()
		return where(w)
	}

	fun where(w: KWhere): KQuery<T> {
		whereStr = "WHERE " + w.value
		args.addAll(w.args)
		return this
	}

	fun asc(p: KProperty<*>): KQuery<T> {
		if (orderStr.isEmpty()) {
			orderStr = "ORDER BY " + fieldNameOf(p) + " ASC"
		} else {
			orderStr += ", " + fieldNameOf(p) + " ASC"
		}
		return this
	}

	fun desc(p: KProperty<*>): KQuery<T> {
		if (orderStr.isEmpty()) {
			orderStr = "ORDER BY " + fieldNameOf(p) + " DESC"
		} else {
			orderStr += ", " + fieldNameOf(p) + " DESC"
		}

		return this
	}

	fun limit(limit: Int): KQuery<T> {
		if (limit > 0) {
			limitStr = " LIMIT $limit"
		}
		return this
	}

	fun limit(limit: Int, offset: Int): KQuery<T> {
		if (limit > 0 && offset >= 0) {
			limitStr = " LIMIT $limit OFFSET $offset"
		}
		return this
	}
}
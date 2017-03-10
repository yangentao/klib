package net.yet.util.database

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import net.yet.ext.len
import net.yet.ext.toArray
import java.util.*

/**
 * 只能执行一次查询!

 * @author yangentao@gmail.com
 */
class Query(val db: SQLiteDatabase, vararg columns: String) {
	private var columnsStr: String
	private var fromStr: String? = null
	private var orderStr: String? = null
	private var limitStr: String? = null
	private var whereStr: String? = null
	private var argList = ArrayList<String>()
	private var distinct = false

	init {
		if (columns.size == 0) {
			this.columnsStr = "*"
		} else {
			this.columnsStr = columns.joinToString(",")
		}
	}

	fun from(vararg tables: String): Query {
		this.fromStr = tables.joinToString(",")
		return this
	}

	fun distinct(): Query {
		this.distinct = true
		return this
	}

	fun where(w: WhereNode): Query {
		this.whereStr = w.toString()
		this.argList.addAll(w.args)
		return this
	}

	fun whereEq(key: String, value: String): Query {
		return where(key EQ value)
	}

	fun whereEq(key: String, value: Long): Query {
		return where(key EQ value)
	}


	fun limit(limit: Int): Query {
		if (limit > 0) {
			limitStr = " LIMIT $limit "
		}
		return this
	}

	fun limit(limit: Int, offset: Int): Query {
		if (limit > 0 && offset >= 0) {
			limitStr = " LIMIT $limit OFFSET $offset "
		}
		return this
	}

	fun orderBy(sortOrder: String?): Query {
		this.orderStr = sortOrder
		return this
	}

	fun desc(column: String): Query {
		this.orderStr = column + " DESC "
		return this
	}

	fun asc(column: String): Query {
		this.orderStr = column + " ASC "
		return this
	}

	private fun safeQuery(sql: String, args: Array<String> = emptyArray()): Cursor {
		val c = db.rawQuery(sql, args)
		return c ?: SafeCursor(null)
	}

	private fun queryResult(sql: String, args: Array<String> = emptyArray()): CursorResult {
		val c = db.rawQuery(sql, args)
		return CursorResult(c)
	}

	fun queryCount(): Int {
		val sql = buildCountSql()
		return queryResult(sql, toArray(argList)).intValue() ?: 0
	}

	fun queryExist(): Boolean {
		limit(1)
		return queryCount() > 0
	}


	fun query(): Cursor {
		val sql = buildSql()
		return safeQuery(sql, toArray(argList))
	}

	fun queryOne(): Cursor {
		limit(1)
		return query()
	}

	fun resultOne(): CursorResult {
		return CursorResult(queryOne())
	}

	fun resultAll(): CursorResult {
		return CursorResult(query())
	}

	fun resultString(): String? {
		return resultOne().strValue()
	}

	fun resultLong(failVal: Long): Long? {
		return resultOne().longValue()
	}

	fun resultLong(): Int? {
		return resultOne().intValue()
	}

	fun buildSql(): String {
		var s = if (distinct) {
			"SELECT DISTINCT $columnsStr FROM $fromStr "
		} else {
			"SELECT $columnsStr FROM $fromStr "
		}
		if (whereStr.len > 0) {
			s += " WHERE $whereStr "
		}
		if (orderStr.len > 0) {
			s += " ORDER BY $orderStr "
		}
		if (limitStr.len > 0) {
			s += " $limitStr "
		}
		return s
	}

	private fun buildCountSql(): String {
		var s = "SELECT COUNT(*) FROM $fromStr "
		if (whereStr.len > 0) {
			s += " WHERE $whereStr "
		}
		if (limitStr.len > 0) {
			s += limitStr
		}
		return s
	}
}

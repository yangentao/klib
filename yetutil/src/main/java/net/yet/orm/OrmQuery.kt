package net.yet.orm

import android.database.Cursor
import net.yet.ext.len
import net.yet.util.Util
import net.yet.util.database.CursorResult
import net.yet.util.database.EQ
import net.yet.util.database.LiteBase
import net.yet.util.database.WhereNode
import net.yet.util.eachRow
import net.yet.util.firstRow
import java.util.*

/**
 * 只能执行一次查询!

 * @author yangentao@gmail.com
 */
class OrmQuery(val lb: LiteBase, val tableInfo: TableInfo) {
	private var orderStr: String? = null
	private var limitStr: String? = null
	private var whereStr: String? = null
	private var argList = ArrayList<Any>()

	constructor(lb: LiteBase, cls: Class<*>) : this(lb, TableInfo.findOrCreateTable(lb, cls)) {

	}

	fun where(w: WhereNode): OrmQuery {
		this.whereStr = w.toString()
		this.argList.addAll(w.args)
		return this
	}


	fun wherePk(pkValue: String): OrmQuery {
		limit(1)
		return where(tableInfo.pkName EQ pkValue)
	}

	fun wherePk(pkValue: Long): OrmQuery {
		limit(1)
		return where(tableInfo.pkName EQ pkValue)
	}

	fun limit(limit: Int): OrmQuery {
		if (limit > 0) {
			limitStr = " LIMIT $limit "
		}
		return this
	}

	fun limit(limit: Int, offset: Int): OrmQuery {
		if (limit > 0 && offset >= 0) {
			limitStr = " LIMIT $limit OFFSET $offset "
		}
		return this
	}

	fun orderBy(sortOrder: String?): OrmQuery {
		this.orderStr = sortOrder
		return this
	}

	fun desc(column: String): OrmQuery {
		this.orderStr = column + " DESC "
		return this
	}

	fun asc(column: String): OrmQuery {
		this.orderStr = column + " ASC "
		return this
	}
	@Suppress("UNCHECKED_CAST")
	fun <T : Any> findOne(): T? {
		val c = tableInfo.tableType.getConstructor();
		return findOne {
			c.newInstance() as T
		}
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Any> findAll(): ArrayList<T> {
		val c = tableInfo.tableType.getConstructor();
		return findAll {
			c.newInstance() as T
		}
	}

	fun <T : Any> findOne(block: () -> T): T? {
		limit(1)
		val cursor = query()
		cursor.firstRow {
			val value = block.invoke()
			TableInfo.loadFromCursor(tableInfo, it, value)
			return value
		}
		return null
	}

	fun <T : Any> findAll(block: () -> T): ArrayList<T> {
		val ls = ArrayList<T>(256)
		val cursor = query()
		cursor.eachRow {
			val value2 = block.invoke()
			TableInfo.loadFromCursor(tableInfo, cursor, value2)
			ls.add(value2)
		}
		return ls
	}

	fun query(): Cursor {
		val sql = buildSql()
		return lb.query(sql, *Util.toStringArray(argList))
	}

	fun queryResult(): CursorResult {
		return CursorResult(query())
	}

	fun exist(): Boolean {
		limit(1)
		return count() > 0
	}

	fun count(): Int {
		val sql = buildCountSql()
		return lb.queryReqult(sql, *Util.toStringArray(argList)).intValue(0)
	}

	private fun buildCountSql(): String {
		var s = " SELECT COUNT(*) FROM ${tableInfo.tableName} "
		if (whereStr.len > 0) {
			s += " WHERE $whereStr "
		}
		if (limitStr.len > 0) {
			s += " $limitStr "
		}
		return s
	}

	private fun buildSql(): String {
		var s = " SELECT * FROM ${tableInfo.tableName} "
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


}

package net.yet.util.database

import android.content.ContentValues
import android.database.Cursor
import net.yet.util.app.App
import net.yet.util.runOnce

/**
 * Created by entaoyang@163.com on 16/5/13.
 */
//val userTable = XTable("user"){
//      it.createTable("id text primary key", "name text")
//      it.createIndex("name")
//}
class XTable(val table: String, block: (XTable) -> Unit) {
	init {
		runOnce("xtable." + table) {
			block.invoke(this)
		}
	}

	fun select(vararg cols: String): Query {
		return Query(lb.db, *cols).from(table)
	}

	fun createTable(vararg columns: String) {
		lb.createTable(table, *columns)
	}

	fun dropTable() {
		lb.dropTable(table)
	}

	fun createIndex(vararg cols: String) {
		lb.createIndex(table, *cols)
	}

	fun execSQL(sql: String, vararg args: Any) {
		lb.execSQL(sql, *args)
	}

	fun rawQuery(sql: String, vararg args: String): Cursor? {
		return lb.rawQuery(sql, *args)
	}

	fun queryReqult(sql: String, vararg args: String): CursorResult {
		return lb.queryReqult(sql, * args)
	}

	fun query(sql: String, vararg args: String): Cursor {
		return lb.query(sql, *args)
	}

	fun insert(values: ContentValues): Long {
		return lb.insert(table, values)
	}

	fun insert(vararg keyValues: Pair<String, String?>): Long {
		return return lb.insert(table, *keyValues)
	}

	fun insert(values: Values): Long {
		return lb.insert(table, values)
	}

	fun replace(values: ContentValues): Long {
		return lb.replace(table, values)
	}

	fun replace(vararg keyValues: Pair<String, String?>): Long {
		return lb.replace(table, *keyValues)
	}

	fun replace(values: Values): Long {
		return lb.replace(table, values)
	}

	fun delete(whereClause: String, vararg whereArgs: String): Int {
		return lb.delete(table, whereClause, *whereArgs)
	}

	fun deleteAll(): Int {
		return lb.deleteAll(table)
	}

	fun deleteEq(key: String, value: String): Int {
		return lb.deleteEQ(table, key, value)
	}

	fun delete(w: WhereNode): Int {
		return lb.delete(table, w)
	}

	fun update(values: ContentValues, w: WhereNode): Int {
		return lb.update(table, values, w)
	}

	fun update(values: Values, w: WhereNode): Int {
		return lb.update(table, values, w)
	}

	fun update(values: Map<String, Any>, w: WhereNode): Int {
		return lb.update(table, values, w)
	}

	fun update(key: String, value: Any, w: WhereNode): Int {
		return lb.update(table, key, value, w)
	}

	fun dump() {
		lb.dumpTable(table)
	}

	companion object {
		private var lb: LiteBase

		init {
			val db = App.get().openOrCreateDatabase("xtable.db", 0, null)
			lb = LiteBase(db)
		}
	}
}
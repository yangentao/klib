package yet.util.database

import android.content.ContentValues
import android.database.Cursor
import yet.database.eachRow
import yet.database.firstRow
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/14.
 */


interface IDatabaseExt {
	fun execSQL(sql: String, vararg args: Any)
	fun rawQuery(sql: String, vararg args: String): Cursor?
	fun select(vararg cols: String): Query
	fun insert(table: String, values: ContentValues): Long
	fun replace(table: String, values: ContentValues): Long
	fun delete(table: String, whereClause: String?, vararg whereArgs: String): Int
	fun update(table: String, values: ContentValues, whereClause: String?, vararg whereArgs: String): Int


	fun countTable(table: String): Int {
		val c = query("select count(*) from " + table)
		c.firstRow {
			return c.getInt(0)
		}
		return 0
	}

	fun existTable(tableName: String): Boolean {
		return select().from("sqlite_master").where(("type" EQ "table") AND ("name" EQ tableName)).queryExist()
	}

	fun createTable(table: String, vararg columns: String) {
		val s = columns.joinToString(",")
		execSQL("CREATE TABLE IF NOT EXISTS $table ( $s )")
	}

	fun createTable(table: String, columns: List<String>) {
		val s = columns.joinToString(",")
		execSQL("CREATE TABLE IF NOT EXISTS $table ( $s )")
	}

	fun dropTable(table: String) {
		execSQL("DROP TABLE IF EXISTS $table")
	}

	fun createIndex(table: String, vararg cols: String) {
		val s1 = cols.joinToString("_")
		val s2 = cols.joinToString(",")
		execSQL("CREATE INDEX IF NOT EXISTS ${table}_$s1 ON $table ( $s2 )")
	}

	fun addColumn(table: String, columnDef: String) {
		execSQL("ALTER TABLE $table ADD COLUMN $columnDef")
	}

	fun queryReqult(sql: String, vararg args: String): CursorResult {
		val c = rawQuery(sql, *args)
		return CursorResult(c)
	}

	fun query(sql: String, vararg args: String): Cursor {
		val c = rawQuery(sql, *args)
		return c ?: SafeCursor(null)
	}

	fun queryTable(table: String, w: WhereNode, orderBy: String? = null): Cursor {
		return select().from(table).where(w).query()
	}

	fun insert(table: String, vararg keyValues: Pair<String, String?>): Long {
		return insert(table, Values(*keyValues).contentValues())
	}

	fun insert(table: String, values: Values): Long {
		return insert(table, values.contentValues())
	}

	fun replace(table: String, vararg keyValues: Pair<String, String?>): Long {
		return replace(table, Values(*keyValues).contentValues())
	}

	fun replace(table: String, values: Values): Long {
		return replace(table, values.contentValues())
	}

	fun deleteAll(table: String): Int {
		return delete(table, null)
	}

	fun deleteEQ(table: String, key: String, value: String): Int {
		return delete(table, key EQ value)
	}

	fun delete(table: String, w: WhereNode): Int {
		return delete(table, w.s, *w.toArgArray)
	}

	fun update(table: String, values: ContentValues, w: WhereNode): Int {
		return update(table, values, w.toString(), *w.toArgArray)
	}

	fun update(table: String, values: Values, w: WhereNode): Int {
		return update(table, values.contentValues(), w)
	}

	fun update(table: String, values: Map<String, Any>, w: WhereNode): Int {
		return update(table, Values(values), w)
	}

	fun update(table: String, key: String, value: Any): Int {
		return update(table, Values(key to value), WhereNode(""))
	}

	fun update(table: String, key: String, value: Any, w: WhereNode): Int {
		return update(table, Values(key to value), w)
	}

	fun update(table: String, keyValue: Pair<String, String?>, w: WhereNode): Int {
		return update(table, Values(keyValue), w)
	}

	fun <T> findAll(table: String, orderBy: String? = null, block: (Cursor) -> T): ArrayList<T> {
		val ls = ArrayList<T>()
		val c = select().from(table).orderBy(orderBy).query()
		c.eachRow {
			val v = block(it)
			ls.add(v)
		}
		return ls
	}

	fun <T> findAll(table: String, w: WhereNode, orderBy: String? = null, block: (Cursor) -> T): ArrayList<T> {
		val ls = ArrayList<T>()
		val c = select().from(table).where(w).orderBy(orderBy).query()
		c.eachRow {
			val v = block(it)
			ls.add(v)
		}
		return ls
	}

	fun <T> findOne(table: String, w: WhereNode = WhereNode(""), block: (Cursor) -> T): T? {
		val c = select().from(table).where(w).limit(1).query()
		c.firstRow {
			return block(it)
		}
		return null
	}


	fun dumpTable(table: String) {
		select().from(table).resultAll().dump()
	}

}
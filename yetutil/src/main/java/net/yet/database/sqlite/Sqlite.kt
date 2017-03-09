package net.yet.database.sqlite

import android.database.sqlite.SQLiteDatabase
import net.yet.util.JsonUtil
import net.yet.util.database.CursorResult
import net.yet.util.log.logd
import java.util.*

/**
 * Created by entaoyang@163.com on 2017-03-09.
 */

//"cid": 0,
//"name": "locale",
//"type": "TEXT",
//"notnull": 0,
//"dflt_value": null,
//"pk": 0
class TableInfoItem {
	var cid: Int = 0
	var name: String = ""
	var type: String = ""
	var notNull: Boolean = false
	var defaultValue: String? = null
	var pk: Boolean = false

	override fun toString(): String {
		return JsonUtil.toJson(this)
	}
}

class Sqlite(val db: SQLiteDatabase) {

	fun transaction(block: (Sqlite) -> Unit): Boolean {
		synchronized(db) {
			db.beginTransaction()
			try {
				block(this)
				db.setTransactionSuccessful()
				return true
			} catch(ex: Throwable) {
				ex.printStackTrace()
			} finally {
				db.endTransaction()
			}
		}
		return false
	}

	fun createTable(table: String, vararg columns: String) {
		val s = columns.joinToString(",")
		db.execSQL("CREATE TABLE IF NOT EXISTS $table ( $s )")
	}

	fun createTable(table: String, columns: List<String>) {
		val s = columns.joinToString(",")
		db.execSQL("CREATE TABLE IF NOT EXISTS $table ( $s )")
	}

	fun dropTable(table: String) {
		db.execSQL("DROP TABLE IF EXISTS $table")
	}

	fun createIndex(table: String, vararg cols: String) {
		val indexName = indexNameOf(table, *cols)
		val s2 = cols.joinToString(",")
		db.execSQL("CREATE INDEX IF NOT EXISTS $indexName  ON $table ( $s2 )")
	}

	fun indexNameOf(table: String, vararg cols: String): String {
		val s1 = mutableListOf(*cols).sorted().joinToString("_")
		return "${table}_$s1"
	}

	fun addColumn(table: String, columnDef: String) {
		db.execSQL("ALTER TABLE $table ADD COLUMN $columnDef")
	}

	fun existTable(table: String): Boolean {
		val c = db.rawQuery("select * from sqlite_master where type='table' and name='$table'", null)
		var ok = false
		if (c != null) {
			ok = c.count > 0
			c.close()
		}
		return ok
	}

	fun tables(): HashSet<String> {
		val all = HashSet<String>()
		val c = db.rawQuery("select name from sqlite_master where type='table'", null)
		CursorResult(c).each {
			val s = it.getString(0)
			all.add(s)
		}
		return all
	}

	fun indexs(): ArrayList<Pair<String, String>> {
		val all = ArrayList<Pair<String, String>>()
		val c = db.rawQuery("select name, tbl_name from sqlite_master where type='index'", null)
		CursorResult(c).each {
			val name = it.getString(0)
			val tb = it.getString(1)
			all.add(name to tb)
		}
		return all
	}

	fun indexsOf(table: String): HashSet<String> {
		val all = HashSet<String>()
		val c = db.rawQuery("select name from sqlite_master where type='index' and tbl_name='$table'", null)
		CursorResult(c).each {
			val name = it.getString(0)
			all.add(name)
		}
		return all
	}

	fun dumpMaster() {
		val c = db.rawQuery("select * from sqlite_master", null)
		CursorResult(c).dump()
	}

	//[{"cid":0,"name":"locale","type":"TEXT","notnull":0,"dflt_value":null,"pk":0}]
	fun tableInfo(tableName: String): ArrayList<TableInfoItem> {
		val all = ArrayList<TableInfoItem>()
		val c = db.rawQuery("PRAGMA table_info('$tableName')", null)
		CursorResult(c).each {
			val item = TableInfoItem()
			item.cid = it.getInt(it.getColumnIndex("cid"))
			item.name = it.getString(it.getColumnIndex("name"))
			item.type = it.getString(it.getColumnIndex("type"))
			item.notNull = it.getInt(it.getColumnIndex("notnull")) != 0
			val index = it.getColumnIndex("dflt_value")
			if (it.isNull(index)) {
				item.defaultValue = null
			} else {
				item.defaultValue = it.getString(index)
			}
			item.pk = it.getInt(it.getColumnIndex("pk")) != 0
			all.add(item)
		}
		return all
	}

	fun dumpTableInfo(table: String) {
		val ls = tableInfo(table)
		logd(ls)
	}

	fun dumpIndicesOfTable(table: String) {
		for (n in indexsOf(table)) {
			logd(indexInfo(n))
		}
	}

	fun indexInfo(indexName: String): HashSet<String> {
		val all = HashSet<String>()
		val c = db.rawQuery("PRAGMA index_info('$indexName')", null)
		CursorResult(c).each {
			val name = c.getString(c.getColumnIndex("name"))
			all.add(name)
		}
		return all
	}
}
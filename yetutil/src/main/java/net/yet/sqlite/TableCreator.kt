package net.yet.sqlite

import android.database.sqlite.SQLiteDatabase
import net.yet.database.sqlite.Sqlite
import java.util.*

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

object TableCreator {
	val checkedSet = HashSet<String>()
	fun check(db: SQLiteDatabase, mi: ModelInfo) {
		synchronized(checkedSet) {
			val k = db.path + "@" + mi.tableName
			if (k in checkedSet) {
				return
			}
			checkedSet.add(k)
			doCheck(db, mi)
		}

	}

	private fun doCheck(db: SQLiteDatabase, mi: ModelInfo) {
		val L = Sqlite(db)
		if (L.existTable(mi.tableName)) {
			L.transaction {
				checkTable(L, mi)
				checkIndex(L, mi)
			}
		} else {
			L.transaction {
				createTable(L, mi)
				createIndex(L, mi)
			}
		}
	}

	private fun checkTable(L: Sqlite, mi: ModelInfo) {
		if (!mi.autoAlterTable) {
			return
		}
		val set = L.tableInfo(mi.tableName).map { it.name }.toSet()
		for (p in mi.allProp) {
			if (p.shortName !in set) {
				L.addColumn(mi.tableName, p.defineColumn())
			}
		}
	}

	private fun checkIndex(L: Sqlite, mi: ModelInfo) {
		val set = L.indexsOf(mi.tableName)
		for (p in mi.allProp) {
			if (p.isPrimaryKey || p.unique || !p.index) {
				continue
			}
			val indexName = L.indexNameOf(mi.tableName, p.shortName)
			if (indexName !in set) {
				L.createIndex(mi.tableName, p.shortName)
			}
		}
	}

	private fun createTable(L: Sqlite, mi: ModelInfo) {
		val ls = ArrayList<String>()
		for (p in mi.allProp) {
			ls.add(p.defineColumn())
		}
		if (mi.uniques.isNotEmpty()) {
			val s = "CONSTRAINT " + mi.uniques.joinToString("_") + " UNIQUE (" + mi.uniques.joinToString(",") + ")"
			ls.add(s)
		}
		L.createTable(mi.tableName, ls)
	}

	private fun createIndex(L: Sqlite, mi: ModelInfo) {
		for (p in mi.allProp) {
			if (p.isPrimaryKey || p.unique || !p.index) {
				continue
			}
			L.createIndex(mi.tableName, p.shortName)
		}
	}


}
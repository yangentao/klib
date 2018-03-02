package yet.util.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import yet.util.app.App
import yet.util.log.logd
import java.io.Closeable
import java.io.File

/**
 * Created by yangentao on 2015/11/10.
 * entaoyang@163.com
 */
class LiteBase(val db: SQLiteDatabase) : IDatabaseExt, Closeable {

	val path:String get() = db.path

	constructor(dbname: String) : this(App.app.openOrCreateDatabase(dbname, 0, null)) {
	}

	constructor(file: File) : this(SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.CREATE_IF_NECESSARY)) {
	}

	val inTransaction: Boolean get() = db.inTransaction()


	override fun close() {
		db.close()
	}

	inline fun trans(block: (LiteBase) -> Unit): Boolean {
		synchronized(this) {
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

	override fun select(vararg cols: String): Query {
		return Query(db, *cols)
	}


	override fun execSQL(sql: String, vararg args: Any) {
		logd(sql)
		logd(args)
		db.execSQL(sql, args)
	}

	override fun rawQuery(sql: String, vararg args: String): Cursor? {
		return db.rawQuery(sql, args)
	}

	override fun insert(table: String, values: ContentValues): Long {
		return db.insert(table, null, values)
	}


	override fun replace(table: String, values: ContentValues): Long {
		return db.replace(table, null, values)
	}

	override fun delete(table: String, whereClause: String?, vararg whereArgs: String): Int {
		return db.delete(table, whereClause, whereArgs)
	}


	override fun update(table: String, values: ContentValues, whereClause: String?, vararg whereArgs: String): Int {
		return db.update(table, values, whereClause, whereArgs)
	}

}

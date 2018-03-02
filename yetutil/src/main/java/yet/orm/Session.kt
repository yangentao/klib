package yet.orm

import android.database.sqlite.SQLiteDatabase
import yet.database.Sqlite
import yet.file.UserFile
import yet.util.app.App
import java.io.Closeable
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-12-13.
 */


class Session(val db: SQLiteDatabase) : Closeable {
	val sqlite = Sqlite(db)

	inline fun <R> pushPop(block: () -> R): R {
		push()
		try {
			return block.invoke()
		} finally {
			pop()
		}
	}


	fun push() {
		val stack = sessionThreadLocal.get()
		stack.push(this)
	}

	fun pop() {
		val stack = sessionThreadLocal.get()
		stack.pop()
	}

	inline fun <R> transaction(block: Session.() -> R): R {
		var ok = true
		try {
			db.beginTransaction()
			return this.block()
		} catch (ex: Throwable) {
			ok = false
			ex.printStackTrace()
			throw ex
		} finally {
			if (ok) {
				db.setTransactionSuccessful()
			}
			db.endTransaction()
		}
	}

	internal fun existByPK(mi: ModelInfo, pkValue: Any): Boolean {
		val pk = mi.pk
		if (pk != null) {
			return from(mi.cls).where(pk.prop EQ pkValue.toString()).queryCount() > 0
		}
		return false
	}

	override fun close() {
		db.close()
	}


	companion object {
		val global: Session = named("globalOrm.db")

		fun named(dbname: String): Session {
			val db = App.openOrCreateDatabase(dbname)
			return Session(db)
		}

		fun user(user: String, dbname: String = "korm.db"): Session {
			val file = UserFile.file(user, dbname)
			val db = SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.CREATE_IF_NECESSARY)
			return Session(db)
		}

		val peek: Session get() {
			val stack = sessionThreadLocal.get()
			if (stack.isEmpty()) {
				return global
			}
			return stack.peek()
		}

		private val sessionThreadLocal = object : ThreadLocal<Stack<Session>>() {
			override fun initialValue(): Stack<Session> {
				return Stack<Session>()
			}
		}
	}
}


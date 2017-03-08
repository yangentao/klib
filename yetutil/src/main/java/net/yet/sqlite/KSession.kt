package net.yet.sqlite

import android.database.sqlite.SQLiteDatabase
import net.yet.file.UserFile
import net.yet.util.app.App
import java.io.Closeable
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2016-12-13.
 */


class KSession(val db: SQLiteDatabase) : Closeable {
	fun <R> asCurrent(block: () -> R): R {
		push()
		try {
			return block.invoke()
		} finally {
			pop()
		}
		this.use { }
	}

	fun <R> transaction(block: KSession.() -> R): R {
		push()
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
			pop()
		}
	}

	private fun push() {
		val stack = sessionThreadLocal.get()
		stack.push(this)
	}

	private fun pop() {
		val stack = sessionThreadLocal.get()
		stack.pop()
	}

	fun save(model: Any) {
		TableCreator.check(this, model::class)
	}

	fun insertOrReplace(model: Any, replace: Boolean): Long {
		TableCreator.check(this, model::class)
		val mi = ModelInfo.find(model::class)
		val values = mi.toContentValues(model)
		var assignId = false
		if (mi.pk?.autoInc ?: false) {//整形自增
			val pkValue = values.get(mi.pk!!.name)
			if (pkValue == null || 0 == (pkValue as Number).toInt()) {
				values.remove(mi.pk!!.name)
				assignId = true
			}
		}
		val id = if (replace) {
			db.replace(mi.tableName, null, values)
		} else {
			db.insert(mi.tableName, null, values)
		}
		if (assignId && id != -1L) {
			try {
				val type = mi.pk!!.prop.returnType
				if (type.isInt) {
					mi.pk!!.prop.setter.call(model, id.toInt())
				} else if (type.isLong) {
					mi.pk!!.prop.setter.call(model, id)
				}
			} catch (e: IllegalAccessException) {
				e.printStackTrace()
			}

		}
		return id
	}

	//Session.named("yang").from(Person::class).where(Person::name EQ "yang").orderBy(Person::name).one()
	fun from(modelClass: KClass<*>): KQuery {
		TableCreator.check(this, modelClass)
		return KQuery(db, modelClass)
	}

	fun existTable(name: String): Boolean {
		val c = db.rawQuery("select count(*) from sqlite_master where type='table' AND name=$name", null) ?: return false
		return KCursorResult(c).resultCount() > 0
	}

	override fun close() {
		db.close()
	}

	companion object {
		fun named(dbname: String): KSession {
			val db = App.get().openOrCreateDatabase(dbname, 0, null)
			return KSession(db)
		}

		fun user(user: String, dbname: String = "korm.db"): KSession {
			val file = UserFile.file(user, dbname)
			val db = SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.CREATE_IF_NECESSARY)
			return KSession(db)
		}

		val current: KSession get() {
			val stack = sessionThreadLocal.get()
			if (stack.isEmpty()) {
				throw  RuntimeException("No Session find")
			}
			return stack.peek()
		}

		private val sessionThreadLocal = object : ThreadLocal<Stack<KSession>>() {
			override fun initialValue(): Stack<KSession> {
				return Stack<KSession>()
			}
		}
	}
}


package net.yet.sqlite

import android.database.sqlite.SQLiteDatabase
import net.yet.database.sqlite.Sqlite
import net.yet.file.UserFile
import net.yet.util.app.App
import net.yet.util.log.xlog
import java.io.Closeable
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2016-12-13.
 */


class KSession(val db: SQLiteDatabase) : Closeable {
	val sqlite = Sqlite(db)

	fun <R> asCurrent(block: () -> R): R {
		push()
		try {
			return block.invoke()
		} finally {
			pop()
		}
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

	fun push() {
		val stack = sessionThreadLocal.get()
		stack.push(this)
	}

	fun pop() {
		val stack = sessionThreadLocal.get()
		stack.pop()
	}

	private fun existPK(mi: ModelInfo, pkValue: Any): Boolean {
		if (mi.pk != null) {
			return from(mi.cls).where(mi.pk.prop EQ pkValue.toString()).queryCount() > 0
		}
		return false
	}

	fun save(model: Any): Int {
		val mi = ModelInfo.find(model::class)
		TableCreator.check(db, mi)
		if (mi.hasPK) {
			val pkVal = mi.getPKValue(model)
			if (pkVal != null) {
				if (existPK(mi, pkVal)) {
					return updateByPK(model)
				}
			}
			val id = insert(model)
			return if (id > 0) 1 else 0
		}
		val id = replace(model)
		return if (id > 0) 1 else 0

	}

	fun updateByPK(model: Any): Int {
		val mi = ModelInfo.find(model::class)
		TableCreator.check(db, mi)
		val values = mi.toContentValues(model)
		if (mi.pk == null) {
			xlog.e("没有找到主键" + mi.tableName)
			return 0
		}
		val pkValue = values.get(mi.pk.shortName)
		if (pkValue == null) {
			xlog.e("主键没有值" + mi.tableName)
			return 0
		}
		values.remove(mi.pk.shortName)
		return db.update(mi.tableName, values, mi.pk.fullName + "=?", arrayOf(pkValue.toString()))
	}

	fun insert(model: Any): Long {
		return insertOrReplace(model, false)
	}

	fun replace(model: Any): Long {
		return insertOrReplace(model, true)
	}

	private fun insertOrReplace(model: Any, replace: Boolean): Long {
		val mi = ModelInfo.find(model::class)
		TableCreator.check(db, mi)
		val values = mi.toContentValues(model)
		var assignId = false
		if (mi.pk?.autoInc ?: false) {//整形自增
			val pkValue = values.get(mi.pk!!.shortName)
			if (pkValue == null || 0 == (pkValue as Number).toInt()) {
				values.remove(mi.pk.shortName)
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
					mi.pk.prop.setter.call(model, id.toInt())
				} else if (type.isLong) {
					mi.pk.prop.setter.call(model, id)
				}
			} catch (e: IllegalAccessException) {
				e.printStackTrace()
			}

		}
		return id
	}

	//Session.named("yang").from(Person::class).where(Person::name EQ "yang").orderBy(Person::name).one()
	fun from(modelClass: KClass<*>): KQuery {
		val mi = ModelInfo.find(modelClass)
		TableCreator.check(db, mi)
		return KQuery(db, modelClass)
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


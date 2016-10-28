package net.yet.util.database

import android.util.ArrayMap
import net.yet.util.app.App
import net.yet.util.log.xlog
import java.util.*


class LongMap(val table: String) : MapLike<Long> {

	init {
		if (table !in tableSet) {
			liteBase.createTable(table, "key bigint PRIMARY KEY", "value text")
			liteBase.createIndex(table, "value")
		}
	}

	fun trans(block: (LongMap) -> Unit): Boolean {
		liteBase.db.beginTransaction()
		try {
			block(this)
			liteBase.db.setTransactionSuccessful()
			return true
		} catch(ex: Throwable) {
			ex.printStackTrace()
		} finally {
			liteBase.db.endTransaction()
		}
		return false
	}

	fun toMap(map: MutableMap<Long, String>) {
		liteBase.queryReqult("select key, value from " + table).each {
			val key = it.getLong(0)
			val value = it.getString(1)
			map.put(key, value)
		}
	}

	fun toArrayMap(): ArrayMap<Long, String> {
		val map = ArrayMap<Long, String>(512)
		toMap(map)
		return map
	}

	fun toHashMap(): HashMap<Long, String> {
		val map = HashMap<Long, String>(512)
		toMap(map)
		return map
	}

	fun putAll(map: Map<Long, String>) {
		trans {
			for ((k, v) in map) {
				liteBase.replace(table, "key" to  "$k", "value" to v)
			}
		}
	}


	operator fun get(key: Long): String? {
		return liteBase.queryReqult("SELECT value from $table where key=?  limit 1", "" + key).strValue()
	}

	operator fun set(key: Long, value: String?) {
		liteBase.replace(table, "key" to "$key", "value" to value)
	}

	override fun getString(key: Long): String? {
		return get(key)
	}

	override fun putString(key: Long, value: String?) {
		set(key, value)
	}

	fun put(key: Long, value: Any?) {
		return set(key, value?.toString())
	}

	fun remove(key: Long): Int {
		return liteBase.deleteEQ(table, "key", "$key")
	}

	fun removeAll(): Int {
		return liteBase.deleteAll(table)
	}

	fun dumpAll() {
		val map = toArrayMap()
		for ((k, v) in map) {
			xlog.d(k, " = ", v)
		}
	}

	companion object {
		private val DEF_DB = "longmap.db"
		private var liteBase: LiteBase
		private val tableSet = HashSet<String>()

		init {
			val db = App.get().openOrCreateDatabase(DEF_DB, 0, null)
			liteBase = LiteBase(db)
		}
	}
}

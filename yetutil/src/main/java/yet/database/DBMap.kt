package yet.util.database

import yet.database.eachRow
import yet.util.log.xlog
import yet.util.runOnce
import java.util.*
import kotlin.reflect.KProperty


class DBMap(val table: String) : MapLike<String> {

	init {
		runOnce("dbmap." + table) {
			liteBase.createTable(table, "key text PRIMARY KEY", "value text")
			liteBase.createIndex(table, "value")
		}
	}


	fun trans(block: (DBMap) -> Unit): Boolean {
		return liteBase.trans {
			block(this)
		}
	}

	fun toHashMap(): HashMap<String, String> {
		val map = HashMap<String, String>(512)
		toMap(map)
		return map
	}

	fun toMap(map: MutableMap<String, String>) {
		liteBase.query("select key, value from " + table).eachRow {
			val key = it.getString(0)
			val value = it.getString(1)
			map.put(key, value)
		}
	}

	fun putAll(map: Map<String, String>) {
		this.trans {
			for ((k, v) in map) {
				liteBase.replace(table, "key" to k, "value" to v)
			}
		}

	}


	operator fun get(key: String): String? {
		return liteBase.queryReqult("SELECT value from $table where key=?  limit 1", key).strValue()
	}

	operator fun set(key: String, value: String?) {
		liteBase.replace(table, "key" to key, "value" to value)
	}

	override fun getString(key: String): String? {
		return get(key)
	}

	override fun putString(key: String, value: String?) {
		set(key, value)
	}

	fun put(key: String, value: Any?) {
		return set(key, value?.toString())
	}

	fun remove(key: String): Int {
		return liteBase.deleteEQ(table, "key", key)
	}

	fun removeAll(): Int {
		return liteBase.deleteAll(table)
	}

	fun dumpAll() {
		val map = toHashMap()
		for ((k, v) in map) {
			xlog.d(k, " = ", v)
		}
	}

	companion object {
		private var liteBase: LiteBase = LiteBase("dbmap.db")
		@JvmStatic fun tab(name: String): DBMap {
			return DBMap(name)
		}
	}
}

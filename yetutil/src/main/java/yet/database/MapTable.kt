package yet.database

import yet.ext.customName
import yet.ext.defaultValue
import yet.ext.defaultValueOfProperty
import yet.ext.strToV
import yet.util.database.LiteBase
import yet.util.log.logd
import yet.util.runOnce
import yet.yson.YsonArray
import yet.yson.YsonObject
import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2017-03-24.
 */

class MapTable(val table: String) {

	init {
		runOnce("maptable.$table") {
			liteBase.createTable(table, "key text PRIMARY KEY", "value text")
			liteBase.createIndex(table, "value")
		}
	}

	operator fun <V> setValue(thisRef: Any?, property: KProperty<*>, value: V) {
		this.put(property.customName, value)
	}

	@Suppress("UNCHECKED_CAST")
	operator fun <V> getValue(thisRef: Any?, property: KProperty<*>): V {
		val v = get(property.customName) ?: property.defaultValue
		?: return if (property.returnType.isMarkedNullable) {
			null as V
		} else {
			defaultValueOfProperty(property)
		}
		return strToV(v, property)
	}

	fun trans(block: (MapTable) -> Unit): Boolean {
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
		liteBase.query("select key, value from $table").eachRow {
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

	fun has(key: String): Boolean {
		return liteBase.queryReqult("SELECT value from $table where key=?  limit 1", key).exist
	}

	operator fun get(key: String): String? {
		return liteBase.queryReqult("SELECT value from $table where key=?  limit 1", key).strValue()
	}

	operator fun set(key: String, value: String?) {
		liteBase.replace(table, "key" to key, "value" to value)
	}

	fun getString(key: String): String? {
		return get(key)
	}

	fun putString(key: String, value: String?) {
		set(key, value)
	}

	fun put(key: String, value: Any?) {
		return set(key, value?.toString())
	}

	fun getInt(key: String): Int? {
		return getString(key)?.toIntOrNull()
	}

	fun getLong(key: String): Long? {
		return getString(key)?.toLongOrNull()
	}

	fun getDouble(key: String): Double? {
		return getString(key)?.toDoubleOrNull()
	}

	fun getBool(key: String): Boolean? {
		return getString(key)?.toBoolean()
	}

	fun getYsonObject(key: String): YsonObject? {
		val s = this.getString(key) ?: return null
		return YsonObject(s)
	}

	fun getYsonArray(key: String): YsonArray? {
		val s = this.getString(key) ?: return null
		return YsonArray(s)
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
			logd(k, " = ", v)
		}
	}

	companion object {
		private var liteBase: LiteBase = LiteBase("maptable.db")

		val config = MapTable("global_config_map_table")
	}
}

package yet.util.database

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import yet.json.GSON

/**
 * Created by entaoyang@163.com on 16/5/13.
 */


interface MapLike<K> {
	fun getString(key: K): String?
	fun putString(key: K, value: String?)

	fun getInt(key: K): Int? {
		return getString(key)?.toIntOrNull()
	}

	fun getLong(key: K): Long? {
		return getString(key)?.toLongOrNull()
	}

	fun getDouble(key: K): Double? {
		return getString(key)?.toDoubleOrNull()
	}

	fun getBool(key: K): Boolean? {
		return getString(key)?.toBoolean()
	}

	fun putInt(key: K, value: Int) {
		putString(key, value.toString())
	}

	fun putLong(key: K, value: Long) {
		putString(key, value.toString())
	}

	fun putDouble(key: K, value: Double) {
		putString(key, value.toString())
	}

	fun putBool(key: K, value: Boolean) {
		putString(key, value.toString())
	}

	fun getJsonObject(key: K): JsonObject? {
		val s = this.getString(key) ?: return null
		return GSON.parseObject(s)
	}

	fun getJsonArray(key: K): JsonArray? {
		val s = this.getString(key) ?: return null
		return GSON.parseArray(s)
	}

}
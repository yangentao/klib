package net.yet.util.database

/**
 * Created by entaoyang@163.com on 16/5/13.
 */


interface MapLike<K> {
	fun getString(key: K): String?
	fun putString(key: K, value: String?)

	fun getInt(key: K): Int? {
		return getString(key)?.toInt()
	}

	fun getLong(key: K): Long? {
		return getString(key)?.toLong()
	}

	fun getDouble(key: K): Double? {
		return getString(key)?.toDouble()
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

}
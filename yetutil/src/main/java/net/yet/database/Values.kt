package net.yet.util.database

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.yet.ext.*
import net.yet.json.*
import net.yet.util.Util
import net.yet.util.app.OS
import net.yet.util.log.loge
import net.yet.util.log.xlog
import java.io.Serializable
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/13.
 */

class Values(n: Int = 8) {
	val map: HashMap<String, Any?> = HashMap(n)

	constructor(other: Values) : this(other.map.size) {
		for ((k, v) in other.map) {
			map.put(k, v)
		}
	}

	constructor(map: Map<String, Any>) : this(map.size) {
		for ((k, v) in map) {
			this.map.put(k, v)
		}
	}

	constructor(b: Bundle?) : this(b?.size() ?: 8) {
		if (b != null) {
			for (key in b.keySet()) {
				map.put(key, b.get(key))
			}
		}
	}

	constructor(it: Intent) : this(it.extras) {
	}

	constructor(vararg keyValues: Pair<String, Any?>) : this(keyValues.size) {
		for ((k, v) in keyValues) {
			map.put(k, v)
		}
	}

	fun contentValues(): ContentValues {
		val v = ContentValues(map.size)
		for ((key, value) in map) {
			if (value == null) {
				v.putNull(key)
			} else if (value is String) {
				v.put(key, value)
			} else if (value is Boolean) {
				v.put(key, value)
			} else if (value is Double) {
				v.put(key, value)
			} else if (value is Float) {
				v.put(key, value)
			} else if (value is Int) {
				v.put(key, value)
			} else if (value is Long) {
				v.put(key, value)
			} else if (value is Byte) {
				v.put(key, value)
			} else if (value is Short) {
				v.put(key, value)
			} else if (value is ByteArray) {
				v.put(key, value)
			} else {
				xlog.e("未知的数据类型", key, value)
			}
		}
		return v
	}

	fun toJson(): JsonObject {
		val jo = JsonObject()
		for ((key, value) in map) {
			if (value == null) {
				jo.putNull(key)
				continue
			}
			when (value) {
				null -> jo.putNull(key)
				is JsonElement -> jo.add(key, value)
				is Values -> jo.add(key, value.toJson())
//				is Bundle -> jo.add(key, Values(value).toJson())
				is String -> jo.putString(key, value)
				is Boolean -> jo.putBool(key, value)
				is Long -> jo.putLong(key, value)
				is Int -> jo.putInt(key, value)
				is Short -> jo.putInt(key, value.toInt())
				is Byte -> jo.putInt(key, value.toInt())
				is Double -> jo.putDouble(key, value)
				is Float -> jo.putFloat(key, value)
				is Char -> jo.putString(key, value.toString())
				is CharSequence -> jo.putString(key, value.toString())

				is BooleanArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}
				is IntArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}

				is LongArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}
				is DoubleArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}
				is FloatArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}
				is ByteArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}

				is ShortArray -> {
					val jarr = JsonArray()
					for (s in value) {
						jarr.add(s)
					}
					jo.add(key, jarr)
				}

				is Array<*> -> {
					val jarr = JsonArray()
					for (v in value) {
						when (v) {
							is String -> jarr.add(v)
							is Number -> jarr.add(v)
							else -> {
								loge("不能识别的数组")
							}
						}
					}
					jo.add(key, jarr)
				}

				else -> {
					xlog.e("不支持的数据类型", key, value)
				}
			}

		}
		return jo
	}


	fun bundle(throwable: Boolean): Bundle {
		val b = Bundle(map.size)
		for ((key, value) in map) {
			when (value) {
				null -> b.putString(key, null)
				is Values -> b.putBundle(key, value.bundle(throwable))
				is Bundle -> b.putBundle(key, value)
				is String -> b.putString(key, value)
				is Boolean -> b.putBoolean(key, value)
				is BooleanArray -> b.putBooleanArray(key, value)
				is Double -> b.putDouble(key, value)
				is DoubleArray -> b.putDoubleArray(key, value)
				is Float -> b.putFloat(key, value)
				is FloatArray -> b.putFloatArray(key, value)
				is Int -> b.putInt(key, value)
				is IntArray -> b.putIntArray(key, value)
				is Long -> b.putLong(key, value)
				is LongArray -> b.putLongArray(key, value)
				is Byte -> b.putByte(key, value)
				is ByteArray -> b.putByteArray(key, value)
				is Short -> b.putShort(key, value)
				is ShortArray -> b.putShortArray(key, value)
				is Char -> b.putChar(key, value)
				is CharArray -> b.putCharArray(key, value)
				is CharSequence -> b.putCharSequence(key, value)
				is Parcelable -> b.putParcelable(key, value)
				is Serializable -> b.putSerializable(key, value)
				is Array<*> -> {
					if (value.size > 0) {
						when (value[0]) {
							is String -> {
								if (OS.API >= 21) {
									b.putStringArray(key, value as Array<String>)
								}
							}
							is CharSequence -> b.putCharSequenceArray(key, value as Array<CharSequence>)
							is Parcelable -> b.putParcelableArray(key, value as Array<Parcelable>)
						}
					}
				}
				else -> {
					xlog.e("未知的数据类型", key, value)
				}
			}
		}
		return b
	}

	fun jsonObject(): JsonObject {
		return toJson()
	}

	fun size(): Int {
		return map.size
	}


	fun keySet(): Set<String> {
		return map.keys
	}

	fun addAll(values: Values?): Values {
		if (values != null) {
			for ((k, v) in values.map) {
				map.put(k, v)
			}
		}
		return this
	}

	fun put(key: String, value: Any): Values {
		map.put(key, value)
		return this
	}

	fun putNull(key: String): Values {
		map.put(key, null)
		return this
	}

	fun get(key: String): Any? {
		return map[key]
	}

	fun has(key: String): Boolean {
		return map.containsKey(key)
	}

	fun getBoolean(key: String, defVal: Boolean = false): Boolean {
		val obj = map[key]
		if (obj is Boolean) {
			return obj
		} else if (obj is Number) {
			return obj.toInt() != 0
		} else if (obj is String) {
			return Util.equalStr("true", obj.toString())
		}
		return defVal
	}

	fun getInt(key: String, defVal: Int = 0): Int {
		val obj = map[key]
		if (obj is Number) {
			return obj.toInt()
		} else if (obj is String) {
			return Util.parseInt(obj, defVal)
		}
		return defVal
	}

	fun getLong(key: String, defVal: Long = 0L): Long {
		val obj = map[key]
		if (obj is Number) {
			return obj.toLong()
		} else if (obj is String) {
			return Util.parseLong(obj, defVal)
		}
		return defVal
	}

	fun getString(key: String): String? {
		val obj = map[key]
		when (obj) {
			null -> return null
			is String -> return obj
			is Number -> return obj.toString()
			else -> {
				return obj.toString()
			}
		}
		return null
	}

	fun getString(key: String, fallback: String?): String? {
		return getString(key) ?: fallback
	}


}

package yet.json

import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import yet.util.MyDate

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */

fun JsonArray.firstObject(): JsonObject? {
	if (this.size() > 0) {
		val je = get(0)
		if (je.isJsonObject) {
			return je.asJsonObject
		}
	}
	return null
}

val JsonObject.size:Int get() {
	return this.entrySet().size
}



inline fun JsonArray.eachInt(block: (Int) -> Unit) {
	for (je in this) {
		if (je.isJsonPrimitive) {
			val jp = je.asJsonPrimitive
			if (jp.isNumber) {
				block.invoke(je.asInt)
			}
		}
	}
}

inline fun JsonArray.eachLong(block: (Long) -> Unit) {
	for (je in this) {
		if (je.isJsonPrimitive) {
			val jp = je.asJsonPrimitive
			if (jp.isNumber) {
				block.invoke(je.asLong)
			}
		}
	}
}

inline fun JsonArray.eachObject(block: (JsonObject) -> Unit) {
	for (je in this) {
		if (je.isJsonObject) {
			block.invoke(je.asJsonObject)
		}
	}
}

fun JsonObject.getAny(key: String): Any? {
	val je = this.get(key)
	if (je == null || je.isJsonNull) {
		return null
	}
	if (je.isJsonObject) {
		return je as JsonObject
	}
	if (je.isJsonArray) {
		return je as JsonArray
	}
	if (je.isJsonPrimitive) {
		val jp = je as JsonPrimitive
		if (jp.isBoolean) {
			return jp.asBoolean
		}
		if (jp.isString) {
			return jp.asString
		}
		if (jp.isNumber) {
			return jp.asNumber
		}
	}
	return null
}

fun JsonObject.putAny(key: String, value: Any?) {
	when (value) {
		null -> putNull(key)
		is String -> putString(key, value)
		is Boolean -> putBool(key, value)
		is Number -> putNumber(key, value)
		is JsonObject -> putObject(key, value)
		is JsonArray -> putArray(key, value)
		else -> throw IllegalArgumentException("不支持的类型: $value ${value::class.qualifiedName}")
	}
}

fun JsonObject.putNull(key: String) {
	this.add(key, JsonNull.INSTANCE)
}

fun JsonObject.putString(key: String, value: String) {
	this.addProperty(key, value)
}

fun JsonObject.putNumber(key: String, value: Number) {
	this.addProperty(key, value)
}

fun JsonObject.putLong(key: String, value: Long) {
	this.addProperty(key, value)
}

fun JsonObject.putInt(key: String, value: Int) {
	this.addProperty(key, value)
}

fun JsonObject.putBool(key: String, value: Boolean) {
	this.addProperty(key, value)
}

fun JsonObject.putDouble(key: String, value: Double) {
	this.addProperty(key, value)
}

fun JsonObject.putFloat(key: String, value: Float) {
	this.addProperty(key, value)
}

fun JsonObject.putObject(key: String, value: JsonObject) {
	this.add(key, value)
}

fun JsonObject.putArray(key: String, value: JsonArray) {
	this.add(key, value)
}


fun JsonObject.optString(key: String, failVal: String): String {
	return optString(key) ?: failVal
}

fun JsonObject.optString(key: String): String? {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asString
		}
	}
	return null
}

fun JsonObject.optInt(key: String, value: Int = 0): Int {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asInt
		}
	}
	return value
}

fun JsonObject.optLong(key: String, value: Long = 0): Long {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asLong
		}
	}
	return value
}

fun JsonObject.optArray(key: String): JsonArray? {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonArray) {
			return je.asJsonArray
		}
	}
	return null
}

fun JsonObject.optObject(key: String): JsonObject? {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonObject) {
			return je.asJsonObject
		}
	}
	return null
}

fun JsonObject.optDouble(key: String, value: Double = 0.0): Double {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asDouble
		}
	}
	return value
}

fun JsonObject.optBool(key: String, value: Boolean = false): Boolean {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asBoolean
		}
	}
	return value
}

fun JsonObject.optDate(key: String): String? {
	val value: Long = optLong(key, 0L)
	if (value != 0L) {
		return MyDate(value).formatDate()
	}
	return null
}

fun JsonObject.optTime(key: String): String? {
	val value: Long = optLong(key, 0L)
	if (value != 0L) {
		return MyDate(value).formatTime()
	}
	return null
}

fun JsonObject.optDateTime(key: String): String? {
	val value: Long = optLong(key, 0L)
	if (value != 0L) {
		return MyDate(value).formatDateTime()
	}
	return null
}

fun JsonArray.addAny(value: Any?) {
	when (value) {
		null -> add(JsonNull.INSTANCE)
		is String -> add(value)
		is Long -> add(value)
		is Int -> add(value)
		is Boolean -> add(value)
		is Double -> add(value)
		is Float -> add(value)
		is JsonObject -> add(value)
		is JsonArray -> add(value)
		else -> throw IllegalArgumentException("不支持的类型: $value ${value::class.qualifiedName}")
	}
}


fun JsonObject.str(key: String): String? {
	return this.optString(key)
}

fun JsonObject.int(key: String): Int? {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asInt
		}
	}
	return null
}

fun JsonObject.long(key: String): Long? {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asLong
		}
	}
	return null
}

fun JsonObject.double(key: String): Double? {
	val je = this.get(key)
	if (je != null && !je.isJsonNull) {
		if (je.isJsonPrimitive) {
			return je.asDouble
		}
	}
	return null
}

fun JsonObject.array(key: String): JsonArray? {
	return this.optArray(key)
}

fun JsonObject.obj(key: String): JsonObject? {
	return this.optObject(key)
}


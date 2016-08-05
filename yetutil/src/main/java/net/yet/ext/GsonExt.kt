package net.yet.ext

import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject

/**
 * Created by entaoyang@163.com on 16/5/12.
 */



///==============gson================

inline fun JsonArray.eachObject(block: (JsonObject) -> Unit) {
	for (je in this) {
		if (je.isJsonObject) {
			block.invoke(je.asJsonObject)
		}
	}
}


fun JsonObject.putNull(key: String) {
	this.add(key, JsonNull.INSTANCE)
}

fun JsonObject.putString(key: String, value: String) {
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


fun JsonObject.optString(key: String, failVal:String): String {
	return optString(key) ?: failVal
}
fun JsonObject.optString(key: String): String? {
	val je = this.get(key)
	if (je != null) {
		if (je.isJsonPrimitive) {
			return je.asString
		}
	}
	return null
}

fun JsonObject.optInt(key: String, value: Int = 0): Int {
	val je = this.get(key)
	if (je != null) {
		if (je.isJsonPrimitive) {
			return je.asInt
		}
	}
	return value
}

fun JsonObject.optLong(key: String, value: Long = 0): Long {
	val je = this.get(key)
	if (je != null) {
		if (je.isJsonPrimitive) {
			return je.asLong
		}
	}
	return value
}

fun JsonObject.optBool(key: String, value: Boolean = false): Boolean {
	val je = this.get(key)
	if (je != null) {
		if (je.isJsonPrimitive) {
			return je.asBoolean
		}
	}
	return value
}
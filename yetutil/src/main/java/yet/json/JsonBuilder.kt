package yet.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * Created by entaoyang@163.com on 2016-12-29.
 */

class JsonObjectBuilder {
	val jo = JsonObject()

	fun toJson(): String {
		return jo.toString()
	}

	infix fun <V> String.to(value: V) {
		jo.putAny(this, value)
	}


	infix fun String.to(value: JsonObject) {
		jo.putObject(this, value)
	}

	infix fun String.to(value: JsonArray) {
		jo.putArray(this, value)
	}


}

fun jsonObject(vararg ps: Pair<String, Any?>): JsonObject {
	val jo = JsonObject()
	for (p in ps) {
		jo.putAny(p.first, p.second)
	}
	return jo
}

fun jsonObject(block: JsonObjectBuilder.() -> Unit): JsonObject {
	val b = JsonObjectBuilder()
	b.block()
	return b.jo
}

fun jsonArray(vararg values: Any): JsonArray {
	val arr = JsonArray()
	for (v in values) {
		arr.addAny(v)
	}
	return arr
}

fun <T> jsonArray(values: Collection<T>): JsonArray {
	val arr = JsonArray()
	for (v in values) {
		arr.addAny(v)
	}
	return arr
}

fun <T, R> jsonArray(values: Collection<T>, block: (T) -> R): JsonArray {
	val arr = JsonArray()
	for (v in values) {
		arr.addAny(block(v))
	}
	return arr
}
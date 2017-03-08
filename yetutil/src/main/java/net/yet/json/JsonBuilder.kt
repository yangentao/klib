package net.yet.json

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by entaoyang@163.com on 2016-12-29.
 */



class JsonObjectBuilder {
	val jo = JSONObject()

	infix fun <V> String.to(value: V) {
		jo.put(this, value)
	}

	fun toJson(): String {
		return jo.toString()
	}
}


fun jsonObject(block: JsonObjectBuilder.() -> Unit): JSONObject {
	val b = JsonObjectBuilder()
	b.block()
	return b.jo
}

fun jsonArray(vararg values: Any): JSONArray {
	val arr = JSONArray()
	for (v in values) {
		arr.put(v)
	}
	return arr
}

//fun testJson() {
//	jsonObject {
//		"bid" to "123"
//		"lat" to 123.444
//		"arr" to jsonArray(1, 2, 3)
//		"obj" to jsonObject {
//			"name" to "yang"
//		}
//	}
//}
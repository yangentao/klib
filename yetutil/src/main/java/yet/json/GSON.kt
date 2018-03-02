package yet.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import yet.util.JsonUtil
import java.util.*

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */

object GSON {
	fun parseObject(s: String?): JsonObject? {
		if (s != null && s.length > 0) {
			return JsonUtil.parseObject(s)
		}
		return null
	}

	fun parseArray(s: String?): JsonArray? {
		if (s != null && s.length > 0) {
			return JsonUtil.parseArray(s)
		}
		return null
	}
	fun toJson(model:Any):String {
		return JsonUtil.toJson(model)
	}

	val StringHashSetType = object : TypeToken<HashSet<String>>() {}.type
	val IntHashSetType = object : TypeToken<HashSet<Int>>() {}.type
	val LongHashSetType = object : TypeToken<HashSet<Long>>() {}.type

	var StringArrayListType = object : TypeToken<ArrayList<String>>() {}.type
	var IntArrayListType = object : TypeToken<ArrayList<Int>>() {}.type
	var LongArrayListType = object : TypeToken<ArrayList<Long>>() {}.type
}
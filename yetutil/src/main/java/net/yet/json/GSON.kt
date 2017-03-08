package net.yet.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.yet.util.JsonUtil

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */

object GSON {
	fun parseObject(s: String): JsonObject? {
		return JsonUtil.parseObject(s)
	}

	fun parseArray(s: String): JsonArray? {
		return JsonUtil.parseArray(s)
	}
}
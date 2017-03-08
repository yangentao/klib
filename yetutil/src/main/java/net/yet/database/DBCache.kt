package net.yet.database

import com.google.gson.JsonObject
import net.yet.json.JSON
import net.yet.json.long
import net.yet.json.string
import net.yet.util.JsonUtil
import net.yet.util.database.DBMap
import org.json.JSONObject

/**
 * Created by entaoyang@163.com on 2016-07-26.
 */

object DBCache {
	val DAY = 24 * 3600
	val HOUR = 3600

	private val map: DBMap = DBMap("dbcache")
	private val KEY = "key"
	private val BODY = "body"
	private val EXPIRE = "expire"


	fun put(key: String, body: String, expireSeconds: Long = -1) {
		val jo = JSONObject()
		jo.put(KEY, key)
		jo.put(BODY, body)
		if (expireSeconds < 0) {
			jo.put(EXPIRE, -1)
		} else {
			jo.put(EXPIRE, expireSeconds * 1000 + System.currentTimeMillis())
		}
		map.put(key, jo.toString())
	}

	fun remove(key: String) {
		map.remove(key)
	}

	fun get(key: String): String? {
		val s = map.get(key) ?: return null
		val jo = JSON.parseObject(s) ?: return null
		val expire = jo.long(EXPIRE) ?: return null
		val body = jo.string(BODY) ?: return null
		if (expire > 0) {
			if (System.currentTimeMillis() > expire) {
				return null
			}
		}
		return body
	}

	fun getJson(key: String): JSONObject? {
		val s = get(key) ?: return null
		return JSON.parseObject(s)
	}

	fun getGson(key: String): JsonObject ? {
		val s = get(key) ?: return null
		return JsonUtil.parseObject(s)
	}
}
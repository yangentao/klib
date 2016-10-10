package net.yet.ext

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.yet.net.IP
import net.yet.util.JsonUtil
import net.yet.util.StrBuilder

/**
 * Created by entaoyang@163.com on 16/5/13.
 */


val String?.isIP: Boolean get() {
	return IP.isIP4(this)
}

fun List<String>.join(sep: String): String {
	val sb = StrBuilder(this.size * 10 + 8, sep)
	for (s in this) {
		sb.appendS(s)
	}
	return sb.toString()
}

fun Array<String>.join(sep: String): String {
	val sb = StrBuilder(this.size * 10 + 8, sep)
	sb.appendS(*this)
	return sb.toString()
}


fun String?.toJsonObject(): JsonObject? {
	if (this != null) {
		return JsonUtil.parseObject(this)
	}
	return null
}

fun String?.toJsonArray(): JsonArray? {
	if (this != null) {
		return JsonUtil.parseArray(this)
	}
	return null
}

val String?.len: Int get() {
	if (this != null) {
		return this.length
	}
	return 0
}

fun String?.empty(): Boolean {
	return this == null || this.length == 0
}

fun String?.notEmpty(): Boolean {
	return this != null && this.length > 0
}

fun String?.emptyOr(s: String): String {
	return if (this == null || this.length == 0) s else this
}

fun String?.nullOr(s: String): String {
	return if (this == null) s else this
}

fun String?.hasCharLast(ch: Char): Boolean {
	return (this?.lastIndexOf(ch) ?: -1) >= 0
}

fun String?.hasChar(ch: Char): Boolean {
	return (this?.indexOf(ch) ?: -1) >= 0
}

fun String.head(n: Int): String {
	if (this.length <= n) {
		return this
	}
	return this.substring(0, n)
}
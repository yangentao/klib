package yet.ext

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import yet.net.IP
import yet.util.*
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/13.
 */


fun String.escapeXML(): String {
	return this.replaceChars('<' to "&lt;", '>' to "&gt;", '&' to "&amp;", '"' to "&quot;", '\'' to "&apos;")
}

fun String.replaceChars(vararg charValuePair: Pair<Char, String>): String {
	val sb = StringBuilder(this.length + 8)
	for (ch in this) {
		val p = charValuePair.find { it.first == ch }
		if (p != null) {
			sb.append(p.second)
		} else {
			sb.append(ch)
		}
	}
	return sb.toString()
}


val String?.formatPhone: String? get() {
	return FormatPhone(this)
}

infix fun String?.EMP(other: String): String {
	if (this == null || this.isEmpty()) {
		return other
	}
	return this
}

//trim and empty
infix fun String?.EMP_(other: String): String {
	if (this == null) {
		return other
	}
	val s = this.trim()
	if (s.isEmpty()) {
		return other
	}
	return s
}

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

fun String?.blankOr(s: String): String {
	if (this == null || this.isEmpty()) {
		return s
	}
	if (s.isBlank()) {
		return s
	}
	return this
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
	if (n <= 0) {
		return ""
	}
	if (this.length <= n) {
		return this
	}
	return this.substring(0, n)
}

fun String.tail(n: Int): String {
	if (n <= 0) {
		return ""
	}
	if (this.length < n) {
		return this
	}
	return this.substring(this.length - n)
}

//分隔成长度不大于n的字符串数组
fun String.truck(n: Int): List<String> {
	val ls = ArrayList<String>()
	if (this.length <= n) {
		ls.add(this)
	} else {
		val x = this.length / n
		val y = this.length % n
		for (i in 1..x) {
			val start = (i - 1) * n
			ls.add(this.substring(start, start + n))
		}
		if (y != 0) {
			ls.add(this.substring(x * n))
		}
	}
	return ls
}
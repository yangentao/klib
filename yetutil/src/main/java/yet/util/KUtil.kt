package yet.util

import android.net.Uri
import yet.util.app.App
import yet.util.log.loge
import java.io.*

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

private val PROGRESS_DELAY = 50

val debug: Boolean by lazy { App.debug }


fun debugThrow(msg: String) {
	loge(msg)
	if (debug) {
		throw IllegalStateException(msg)
	}
}


fun <T> Set<T>?.empty(): Boolean {
	return this?.isEmpty() ?: true
}


/**
 * null, false, 空字符串,空集合, 空数组, 空map都认定为空,  返回true
 */
fun empty(obj: Any?): Boolean {
	if (obj == null) {
		return true
	}
	return when (obj) {
		is String -> obj.length == 0
		is Boolean -> !obj
		is Float, Double -> obj == 0
		is Number -> obj.toInt() == 0
		is Collection<*> -> obj.size == 0
		is Map<*, *> -> obj.size == 0
		is Array<*> -> obj.size == 0
		else -> false
	}
}

fun <T> emptyOr(obj: T?, v: T): T {
	return if (empty(obj)) v else obj!!
}

fun <T> nullOr(obj: T?, v: T): T {
	return obj ?: v
}


fun notEmpty(obj: Any?): Boolean {
	return !empty(obj)
}

fun OR(vararg objs: Any?): Any? {
	for (obj in objs) {
		if (notEmpty(obj)) {
			return obj
		}
	}
	return null
}

fun AND(vararg objs: Any?): Boolean {
	for (obj in objs) {
		if (empty(obj)) {
			return false
		}
	}
	return objs.size > 0
}


@Throws(IOException::class)
fun copyStream(input: InputStream, closeIs: Boolean, os: OutputStream, closeOs: Boolean, total: Int, progress: Progress?) {
	try {
		progress?.onStart(total)

		val buf = ByteArray(4096)
		var pre = System.currentTimeMillis()
		var recv = 0

		var n = input.read(buf)
		while (n != -1) {
			os.write(buf, 0, n)
			recv += n
			if (progress != null) {
				val curr = System.currentTimeMillis()
				if (curr - pre > PROGRESS_DELAY) {
					pre = curr
					progress.onProgress(recv, total, if (total > 0) recv * 100 / total else 0)
				}
			}
			n = input.read(buf)
		}
		os.flush()
		progress?.onProgress(recv, total, if (total > 0) recv * 100 / total else 0)
	} finally {
		if (closeIs) {
			close(input)
		}
		if (closeOs) {
			close(os)
		}
		progress?.onFinish()
	}
}

class SizeStream : OutputStream() {
	var size = 0
		private set

	@Throws(IOException::class)
	override fun write(oneByte: Int) {
		++size
	}

	fun incSize(size: Int) {
		this.size += size
	}

}

fun close(c: Closeable?) {
	try {
		c?.close()
	} catch (e: Exception) {
		e.printStackTrace()
	}
}

fun toast(vararg args: Any?) {
	val sb = StringBuffer(args.size * 8 + 8)
	for (obj in args) {
		sb.append(Util.toLogString(obj))
	}
	ToastUtil.show(sb.toString())
}

fun ByteArray?.prefix(vararg bs: Byte): Boolean {
	if (this == null) {
		return false
	}
	if (this.size < bs.size) {
		return false
	}
	for (i: Int in bs.indices) {
		if (this[i] != bs[i]) {
			return false
		}
	}
	return true
}

fun ByteArray?.skip(n: Int): ByteArray {
	if (this == null || this.size <= n) {
		return byteArrayOf()
	}
	val arr = ByteArray(this.size - n)
	for (i in this.indices) {
		if (i >= n) {
			arr[i - n] = this[i]
		}
	}
	return arr
}

fun sleep(ms: Int) {
	sleep(ms.toLong())
}

fun sleep(ms: Long) {
	try {
		Thread.sleep(ms)
	} catch (ex: Exception) {
	}
}

inline fun <T : Closeable> T.closeAfter(block: (T) -> Unit): Unit {
	try {
		block(this)
	} catch (e: Exception) {
		e.printStackTrace()
	} finally {
		try {
			this.close()
		} catch (ex: Exception) {
		}
	}
}


// 去掉所有空格, 剩下全数字
fun FormatPhone(tel: String?): String? {
	var phone: String = tel ?: return null
	phone = phone.filter { it in '0'..'9' }
	if (phone.length == 12 && phone.startsWith("01")) {//013812345678, 固话也可能是12位, 0531+12345678
		return phone.substring(1)
	}
	if (phone.length == 13 && phone.startsWith("86")) {//86 13812345678
		return phone.substring(2)
	}
	if (phone.length == 14 && phone.startsWith("086")) {//086 13812345678
		return phone.substring(3)
	}
	return if (phone.length >= 3) phone else null
}


fun UriFromSdFile(file: File): Uri {
	return Uri.fromFile(file)
}
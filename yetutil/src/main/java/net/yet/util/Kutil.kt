@file:JvmMultifileClass
@file:JvmName("KUtil")

package net.yet.util

import android.database.Cursor
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

private val PROGRESS_DELAY = 50

val debug: Boolean get() = Util.debug()

fun debugThrow(msg: String) {
	loge(msg)
	if (debug) {
		throw IllegalStateException(msg)
	}
}

fun inSet(value: Int, vararg ns: Int): Boolean {
	for (n in ns) {
		if (value == n) {
			return true
		}
	}
	return false
}

fun inSet(value: String, vararg ss: String): Boolean {
	for (s in ss) {
		if (value == s) {
			return true
		}
	}
	return false
}

fun <T> Set<T>?.empty(): Boolean {
	return this?.isEmpty() ?: true
}



inline fun Cursor?.eachRow(block: (Cursor) -> Unit) {
	if (this != null) {
		try {
			while (this.moveToNext()) {
				block(this)
			}
		} catch(ex: Throwable) {
			ex.printStackTrace()
		} finally {
			this.close()
		}
	}
}

inline fun Cursor?.firstRow(block: (Cursor) -> Unit) {
	if (this != null) {
		try {
			if (this.moveToNext()) {
				block(this)
			}
		} catch(ex: Throwable) {
			ex.printStackTrace()
		} finally {
			this.close()
		}
	}
}


inline infix fun <T> T?.DO(block: (T) -> Unit) {
	if (this != null) {
		block.invoke(this)
	}
}

inline infix fun Boolean?.DO(block: () -> Unit) {
	if (this != null && this) {
		block.invoke()
	}
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
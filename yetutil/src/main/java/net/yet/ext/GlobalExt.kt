package net.yet.ext

import net.yet.util.Util
import java.io.Closeable
import java.util.*

/**
 * Created by entaoyang@163.com on 16/7/20.
 */


val chinaComparator = Comparator<String> { left, right -> Util.collatorChina.compare(left, right) }

inline fun <T> T?.notNull(block: (T) -> Unit) {
	if (this != null) {
		block(this)
	}
}

fun <T : Closeable> T?.closeSafe() {
	try {
		this?.close()
	} catch (ex: Exception) {
		ex.printStackTrace()
	}
}

inline fun <T : Closeable> T?.useSafe(block: (T) -> Unit) {
	try {
		if (this != null) {
			block(this)
		}
	} catch (ex: Exception) {
		ex.printStackTrace()
	} finally {
		this.closeSafe()
	}
}

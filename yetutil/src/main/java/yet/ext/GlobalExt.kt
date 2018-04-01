package yet.ext

import java.io.Closeable
import java.text.Collator
import java.util.*

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

val UTF8 = "UTF-8"
val collatorChina = Collator.getInstance(Locale.CHINA)
val chinaComparator = Comparator<String> { left, right -> collatorChina.compare(left, right) }



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

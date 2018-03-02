package yet.database

import android.database.Cursor
import yet.ext.closeSafe
import yet.util.database.CursorResult
import yet.util.log.loge

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */

fun Cursor.isNull(columnName: String): Boolean {
	return this.isNull(this.getColumnIndex(columnName))
}

fun Cursor.stringValue(columnName: String): String? {
	val n = this.getColumnIndex(columnName)
	return if (isNull(n)) null else this.getString(n)
}

fun Cursor.intValue(columnName: String): Int? {
	val n = this.getColumnIndex(columnName)
	return if (isNull(n)) null else this.getInt(n)
}

fun Cursor.boolValue(columnName: String): Boolean? {
	val n = this.getColumnIndex(columnName)
	return if (isNull(n)) null else this.getInt(n) != 0
}

fun Cursor.longValue(columnName: String): Long? {
	val n = this.getColumnIndex(columnName)
	return if (isNull(n)) null else this.getLong(n)
}

fun Cursor.blobValue(columnName: String): ByteArray? {
	val n = this.getColumnIndex(columnName)
	return if (isNull(n)) null else this.getBlob(n)
}


inline fun Cursor?.eachRow(block: (Cursor) -> Unit) {
	if (this != null) {
		try {
			while (this.moveToNext()) {
				block(this)
			}
		} catch(ex: Throwable) {
			ex.printStackTrace()
			loge(ex)
		} finally {
			this.closeSafe()
		}
	}
}

inline fun Cursor?.firstRow(block: (Cursor) -> Unit): Boolean {
	var has = false
	if (this != null) {
		try {
			if (this.moveToNext()) {
				has = true
				block(this)
			}
		} catch(ex: Throwable) {
			ex.printStackTrace()
			loge(ex)
		} finally {
			this.closeSafe()
		}
	}
	return has
}


fun Cursor?.dumpAndClose() {
	CursorResult(this).dump()
}

val Cursor?.result: CursorResult get() = CursorResult(this)
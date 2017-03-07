package net.yet.sqlite

import android.database.Cursor

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

class KCursorResult(val c: Cursor) {

	fun resultCount(): Int {
		var n = 0
		try {
			if (c.moveToNext()) {
				n = c.getInt(0)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		}
		try {
			c.close()
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
		return n
	}
}
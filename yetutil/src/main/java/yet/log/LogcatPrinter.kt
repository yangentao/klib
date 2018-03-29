package yet.util.log

import android.util.Log

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

class LogcatPrinter : LogPrinter {
	override fun flush() {

	}

	override fun println(priority: LogLevel2, tag: String, msg: String) {
		var n = priority.ordinal
		if (n > Log.VERBOSE) {
			n = Log.VERBOSE
		}
		Log.println(n, tag, msg)
	}

}
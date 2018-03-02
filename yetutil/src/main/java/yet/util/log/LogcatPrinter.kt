package yet.util.log

import android.util.Log

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

class LogcatPrinter : LogPrinter {
	override fun flush() {

	}

	override fun println(priority: Int, tag: String, msg: String) {
		Log.println(priority, tag, msg)
	}

}
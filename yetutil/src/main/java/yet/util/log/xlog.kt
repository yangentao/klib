package yet.util.log

import yet.file.SdAppFile
import yet.util.DateUtil
import yet.util.debug
import yet.util.mergeAction

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

object xlog : LogPrinter {
	var level = LogLevel.DEBUG
	var printer: LogPrinter

	init {
		if (debug) {
			level = LogLevel.ENABLE_ALL
		} else {
			level = LogLevel.INFO
		}
		printer = TreePrinter(LogcatPrinter(), FilePrinter(SdAppFile.log(DateUtil.date() + ".txt")))
	}

	private fun enableLevel(level: Int): Boolean {
		return level > 0 && level >= this.level
	}

	override fun flush() {
		printer.flush()

	}

	override fun println(priority: Int, tag: String, msg: String) {
		if (enableLevel(priority)) {
			printer.println(priority, tag, msg)
			mergeAction("xlog.flush", 1000) {
				flush()
			}
		}
	}


}
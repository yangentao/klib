package yet.util.log

import yet.file.SdAppFile
import yet.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

object xlog : LogPrinter {
	var tag: String = "xlog"
	var level = LogLevel2.DEBUG
	var printer: LogPrinter

	init {
		level = if (debug) {
			LogLevel2.ENABLE_ALL
		} else {
			LogLevel2.INFO
		}
		printer = TreePrinter(LogcatPrinter(), FilePrinter(SdAppFile.log(MyDate().formatDate() + ".txt")))
	}

	override fun flush() {
		printer.flush()

	}

	override fun println(priority: LogLevel2, tag: String, msg: String) {
		if (priority.ge(this.level)) {
			printer.println(priority, tag, msg)
			mergeAction("xlog.flush", 1000) {
				flush()
			}
		}
	}


}
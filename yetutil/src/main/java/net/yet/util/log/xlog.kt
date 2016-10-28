package net.yet.util.log

import net.yet.file.SdAppFile
import net.yet.util.DateUtil
import net.yet.util.debug
import net.yet.util.event.ActionMerger

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

object xlog : LogPrinter {
	const val TAG = "xlog"
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
			autoFlushTrigger.trigger()
		}
	}

	// 2秒延时刷新
	private val autoFlushTrigger = ActionMerger(2000, Runnable { flush() })

}
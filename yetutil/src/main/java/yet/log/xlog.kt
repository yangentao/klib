package yet.util.log

import yet.file.SdAppFile
import yet.util.MyDate
import yet.util.debug
import yet.util.mergeAction
import java.io.PrintWriter
import java.io.StringWriter

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

	fun toLogString(obj: Any?): String {
		if (obj == null) {
			return "null"
		}
		if (obj is String) {
			return obj
		}
		if (obj.javaClass.isPrimitive) {
			return obj.toString()
		}

		if (obj is Throwable) {
			val sw = StringWriter(256)
			val pw = PrintWriter(sw)
			obj.printStackTrace(pw)
			return sw.toString()
		}

		if (obj is Array<*>) {
			val sb = StringBuilder(256)
			sb.append("Array[")
			for (i in obj.indices) {
				if (i != 0) {
					sb.append(",")
				}
				val s = toLogString(obj[i])
				sb.append(s)
			}
			sb.append("]")
			return sb.toString()
		}
		if (obj is List<*>) {
			val sb = StringBuilder(256)
			sb.append("[")
			for (i in obj.indices) {
				if (i != 0) {
					sb.append(",")
				}
				val s = toLogString(obj[i])
				sb.append(s)
			}
			sb.append("]")
			return sb.toString()
		}
		if (obj is Map<*, *>) {
			val sb = StringBuilder(256)
			sb.append("{")
			var first = true
			for (key in obj.keys) {
				if (!first) {
					sb.append(",")
				}
				first = false
				sb.append(toLogString(key), "=", toLogString(obj[key]))
			}
			sb.append("}")
			return sb.toString()
		}
		if (obj is Iterable<*>) {
			val sb = StringBuilder(256)
			sb.append("[")

			var first = true
			for (a in obj) {
				if (!first) {
					sb.append(",")
				}
				first = false
				sb.append(toLogString(a))
			}
			sb.append("]")
			return sb.toString()
		}
		//TODO json
		return obj.toString()
	}
}
package yet.util.log

import yet.util.MyDate
import yet.util.debug
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

interface LogPrinter {

	fun flush()
	fun println(priority: LogLevel2, tag: String, msg: String)

	fun printLines(priority: LogLevel2, tag: String, msg: String) {
		val ML = 1024
		if (msg.length < ML) {
			this.println(priority, tag, msg)
		} else {
			var s = msg
			while (s.length > ML) {
				this.println(priority, tag, s.substring(0, ML))
				s = s.substring(ML)
			}
			if (s.isNotEmpty()) {
				this.println(priority, tag, s)
			}
		}
	}


	fun printsome(level: LogLevel2, vararg args: Any?) {
		val msg = makeString(*args)
		printLines(level, xlog.tag, msg)
	}

	fun dTag(tag: String, vararg args: Any?) {
		val msg = makeString(*args)
		printLines(LogLevel2.DEBUG, tag, msg)
	}

	fun wTag(tag: String, vararg args: Any?) {
		val msg = makeString(*args)
		printLines(LogLevel2.WARN, tag, msg)
	}


	fun dIf(c: Boolean, vararg args: Any?) {
		if (c) {
			d(*args)
		}
	}

	fun d(vararg args: Any?) {
		printsome(LogLevel2.DEBUG, *args)
	}

	fun w(vararg args: Any?) {
		printsome(LogLevel2.WARN, *args)
	}

	fun e(vararg args: Any?) {
		printsome(LogLevel2.ERROR, *args)
	}

	fun i(vararg args: Any?) {
		printsome(LogLevel2.INFO, *args)
	}

	fun v(vararg args: Any?) {
		printsome(LogLevel2.VERBOSE, *args)
	}

	fun fatal(vararg args: Any?) {
		e(*args)
		flush()
		throw RuntimeException("fatal error!")
	}

	fun debugFatal(vararg args: Any) {
		if (debug) {
			fatal(*args)
		} else {
			e(*args)
			flush()
		}
	}

	fun formatMsg(priority: LogLevel2, tag: String, msg: String): String {
		val sb = StringBuilder(msg.length + tag.length + 64)
		val date = MyDate().formatDateTimeX()
		sb.append(date)
		sb.append(String.format(Locale.getDefault(), "%6d ", Thread.currentThread().id))
		sb.append(priority.name)
		sb.append(" [")
		sb.append(tag)
		sb.append("] ")
		sb.append(msg)
		return sb.toString()
	}

	fun makeString(vararg args: Any?): String {
		val sb = StringBuilder(args.size * 16 + 16)
		var first = true
		for (obj in args) {
			if (first) {
				first = false
			} else {
				sb.append(" ")
			}
			sb.append(xlog.toLogString(obj))
		}
		return sb.toString()
	}




}
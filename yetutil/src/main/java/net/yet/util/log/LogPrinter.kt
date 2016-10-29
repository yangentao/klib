package net.yet.util.log

import net.yet.util.MyDate
import net.yet.util.Util
import net.yet.util.debug
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

interface LogPrinter {

	fun flush()
	fun println(priority: Int, tag: String, msg: String)

	fun printsome(level: Int, vararg args: Any?) {
		val msg = makeString(*args)
		println(level, LogLevel.TAG, msg)
	}

	fun dTag(tag: String, vararg args: Any?) {
		val msg = makeString(*args)
		println(LogLevel.DEBUG, tag, msg)
	}
	fun wTag(tag: String, vararg args: Any?) {
		val msg = makeString(*args)
		println(LogLevel.WARN, tag, msg)
	}

	fun dIf(c: Boolean, vararg args: Any?) {
		if (c) {
			d(*args)
		}
	}

	fun d(vararg args: Any?) {
		printsome(LogLevel.DEBUG, *args)
	}

	fun w(vararg args: Any?) {
		printsome(LogLevel.WARN, *args)
	}

	fun e(vararg args: Any?) {
		printsome(LogLevel.ERROR, *args)
	}

	fun i(vararg args: Any?) {
		printsome(LogLevel.INFO, *args)
	}

	fun v(vararg args: Any?) {
		printsome(LogLevel.VERBOSE, *args)
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

	fun formatMsg(priority: Int, tag: String, msg: String): String {
		val sb = StringBuilder(msg.length + tag.length + 64)
		val date = MyDate().formatDateTimeX()
		sb.append(date)
		sb.append(String.format(Locale.getDefault(), "%6d ", Thread.currentThread().id))
		sb.append(LogLevel.getText(priority))
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
			sb.append(Util.toLogString(obj))
		}
		return sb.toString()
	}


}
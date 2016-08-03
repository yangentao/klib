//@file:JvmMultifileClass
//@file:JvmName("KUtil")
package net.yet.util

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

//enum class LogLevel(val n: Int) {
//	DISABLE(0), ENABLE_ALL(1), VERBOSE(2), DEBUG(3), INFO(4), WARN(5), ERROR(6)
//}
//
//interface LogPrinter {
//	fun flush()
//
//	fun println(priority: LogLevel, tag: String, msg: String)
//}

//object klog {
//	var TAG = "log"
//	var logLevel = LogLevel.ENABLE_ALL
//	var seprator: String = " "
//	var printer: LogPrinter = LogcatPrinter()
//
//	init {
//		if (Util.debug()) {
//			logLevel = LogLevel.ENABLE_ALL
//		} else {
//			logLevel = LogLevel.INFO
//		}
//	}
//
//	fun isEnable(level: LogLevel): Boolean {
//		return logLevel.n > 0 && level.n >= logLevel.n
//	}
//
//
//	fun flush() {
//		printer.flush()
//	}
//
//	fun setPrinters(vararg ps: LogPrinter) {
//		printer = TreePrinter(*ps)
//	}
//
//
//	fun dIf(condition: Boolean, vararg args: Any?) {
//		if (condition) {
//			d(*args)
//		}
//	}
//
//	fun wIf(condition: Boolean, vararg args: Any?) {
//		if (condition) {
//			w(*args)
//		}
//	}
//
//	fun eIf(condition: Boolean, vararg args: Any?) {
//		if (condition) {
//			e(*args)
//		}
//	}
//
//	fun d(vararg args: Any?) {
//		println(LogLevel.DEBUG, TAG, *args)
//	}
//
//	fun w(vararg args: Any?) {
//		println(LogLevel.WARN, TAG, *args)
//	}
//
//	fun e(vararg args: Any?) {
//		println(LogLevel.ERROR, TAG, *args)
//	}
//
//	fun i(vararg args: Any?) {
//		println(LogLevel.INFO, TAG, *args)
//	}
//
//	fun v(vararg args: Any?) {
//		println(LogLevel.VERBOSE, TAG, *args)
//	}
//
//	fun dTag(tag: String, vararg args: Any?) {
//		println(LogLevel.DEBUG, tag, *args)
//	}
//
//	fun wTag(tag: String, vararg args: Any?) {
//		println(LogLevel.WARN, tag, *args)
//	}
//
//	fun eTag(tag: String, vararg args: Any?) {
//		println(LogLevel.ERROR, tag, *args)
//	}
//
//	fun iTag(tag: String, vararg args: Any?) {
//		println(LogLevel.INFO, tag, *args)
//	}
//
//	fun vTag(tag: String, vararg args: Any?) {
//		println(LogLevel.VERBOSE, tag, *args)
//	}
//
//	private fun getString(obj: Any?): String {
//		return Util.toLogString(obj)
//	}
//
//
//	fun println(priority: LogLevel, tag: String, vararg args: Any?) {
//		if (!isEnable(priority)) {
//			return
//		}
//		var msg: String = if (args.size == 0) {
//			""
//		} else {
//			val sb = StringBuffer(args.size * 8 + 8)
//			var first = true
//			for (obj in args) {
//				if (first) {
//					first = false
//				} else {
//					sb.append(seprator)
//				}
//				sb.append(getString(obj))
//			}
//			sb.toString()
//		}
//
//		printer.println(priority, tag, msg)
//		autoFlushTrigger.trigger()
//	}
//
//	// 2秒延时刷新
//	private val autoFlushTrigger = ActionMerger(2000, Runnable { flush() })
//
//
//	fun formatMsg(priority: LogLevel, tag: String, msg: String): String {
//		val sb = StringBuilder(msg.length + tag.length + 8)
//		val date = getDateString("yyyy-MM-dd HH:mm:ss.SSS ")
//		sb.append(date)
//		sb.append("%6d ".format(Locale.getDefault(), Thread.currentThread().id))
//		sb.append(priority.name)
//		sb.append(" [")
//		sb.append(tag)
//		sb.append("] ")
//		sb.append(msg)
//		return sb.toString()
//	}
//
//
//	private fun getDateString(pattern: String): String {
//		val ff = SimpleDateFormat(pattern, Locale.getDefault())
//		return ff.format(Date())
//	}
//
//	// 打印失败信息, 并抛出运行时异常, 终止程序
//	fun fatal(vararg args: Any?) {
//		e(*args)
//		throw RuntimeException("fatal error!")
//	}
//
//	fun debugFail(b: Boolean, vararg args: Any?) {
//		if (b) {
//			if (Util.debug()) {
//				fatal(*args)
//			} else {
//				e(*args)
//			}
//		}
//	}
//}
//
//class LogcatPrinter : LogPrinter {
//
//	override fun println(priority: LogLevel, tag: String, msg: String) {
//		Log.println(priority.n, tag, "" + msg)
//	}
//
//	override fun flush() {
//	}
//
//}
//
//class ConsolePrinter : LogPrinter {
//
//	override fun println(priority: LogLevel, tag: String, msg: String) {
//		var ps = System.out
//		if (priority.n >= LogLevel.ERROR.n) {
//			ps = System.err
//		}
//		val s = klog.formatMsg(priority, tag, msg)
//		ps.println(s)
//	}
//
//	override fun flush() {
//		System.out.flush()
//		System.err.flush()
//
//	}
//}
//
//class StreamPrinter(val ps: PrintStream) : LogPrinter {
//
//	override fun println(priority: LogLevel, tag: String, msg: String) {
//		val s = klog.formatMsg(priority, tag, msg)
//		ps.println(s)
//	}
//
//	override fun flush() {
//		ps.flush()
//
//	}
//}
//
//class FilePrinter(f: File, cacheSize: Int, limit: Int) : LogPrinter {
//	private var writer: BufferedWriter? = null
//
//	init {
//		try {
//			if (f.exists()) {
//				val input: FileInputStream = FileInputStream(f)
//				if (input.available() > limit) {
//					f.delete()
//				}
//				input.close()
//			}
//			writer = BufferedWriter(FileWriter(f, true), cacheSize)
//		} catch (e: IOException) {
//			e.printStackTrace()
//		}
//
//	}
//
//	override fun flush() {
//		try {
//			if (writer != null) {
//				writer!!.flush()
//			}
//		} catch (e: Exception) {
//			e.printStackTrace()
//		}
//
//	}
//
//	override fun println(priority: LogLevel, tag: String, msg: String) {
//		if (writer != null) {
//			val s = klog.formatMsg(priority, tag, msg)
//			try {
//				writer!!.write(s)
//				writer!!.write("\n")
//			} catch (e: IOException) {
//				e.printStackTrace()
//			}
//
//		}
//	}
//}
//
//class TreePrinter(vararg ps: LogPrinter) : LogPrinter {
//	private val ps: List<LogPrinter>
//
//	init {
//		this.ps = listOf(*ps)
//	}
//
//	override fun println(priority: LogLevel, tag: String, msg: String) {
//		for (p in ps) {
//			p.println(priority, tag, msg)
//		}
//	}
//
//	override fun flush() {
//		for (p in ps) {
//			p.flush()
//		}
//	}
//}


fun logd(vararg args: Any?) {
	xlog.d(*args)
}

fun loge(vararg args: Any?) {
	xlog.e(*args)
}

fun log(vararg args: Any?) {
	xlog.d(*args)
}
package yet.util.log

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

class FilePrinter(f: File , limit: Int = 4 * 1024 * 1024) : LogPrinter {
	private var writer: BufferedWriter? = null


	init {
		try {
			if (f.exists()) {
				if (f.length() > limit) {
					f.delete()
				}
			}
			writer = BufferedWriter(FileWriter(f, true), 5*1024)
		} catch (e: IOException) {
			e.printStackTrace()
		}

	}

	override fun flush() {
		try {
			writer?.flush()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun println(priority: Int, tag: String, msg: String) {
		val w = writer ?: return
		val s = formatMsg(priority, tag, msg)
		try {
			w.write(s)
			w.write("\n")
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

}
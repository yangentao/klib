package net.yet.util.log

import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

class TreePrinter(vararg ps: LogPrinter) : LogPrinter {
	val ls = ArrayList <LogPrinter>()

	init {
		for (p in ps) {
			ls.add(p)
		}
	}

	override fun flush() {
		for (p in ls) {
			p.flush()
		}
	}

	override fun println(priority: Int, tag: String, msg: String) {
		for (p in ls) {
			p.println(priority, tag, msg)
		}
	}

}
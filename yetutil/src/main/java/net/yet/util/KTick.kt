@file:JvmMultifileClass
@file:JvmName("KUtil")
package net.yet.util

import net.yet.util.xlog

private const val TAG = "tick"

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */
class Tick {
	private var start_time: Long = 0
	private var end_time: Long = 0

	init {
		start_time = System.currentTimeMillis()
	}

	fun end(prefix: String): Long {
		return end(0, prefix)
	}

	fun end(warnLevel: Long, prefix: String): Long {
		end_time = System.currentTimeMillis()
		val tick = end_time - start_time
		if (warnLevel > 0 && tick > warnLevel) {
			xlog.wTag(TAG, "[TimeTick]", prefix, ":", tick, "(expect<", warnLevel, ")")
		} else {
			xlog.dTag(TAG, "[TimeTick]", prefix, ":", tick)
		}
		start_time = System.currentTimeMillis()
		return tick
	}
}

fun tick(): Tick {
	return Tick()
}

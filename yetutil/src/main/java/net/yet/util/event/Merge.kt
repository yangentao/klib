package net.yet.util.event

import net.yet.util.foreDelay
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/14.
 */

private class MergeItem(val block: (String) -> Unit) {
	val nano: Long = System.nanoTime()
}

private val mergeMap = Hashtable<String, MergeItem>()

fun mergeAction(key: String, ms: Long = 300, block: (String) -> Unit) {
	mergeMap[key] = MergeItem(block)
	foreDelay(ms) {
		val item = mergeMap.remove(key)
		item?.block?.invoke(key)
	}
}
package net.yet.util.event

import net.yet.util.foreDelay
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/14.
 */

private class MergeItem(val block: (String) -> Unit) {
	val nano: Long = System.nanoTime()
}

private val mergeMap = HashMap<String, MergeItem>()

fun mergeAction(key: String, ms: Long = 300, block: (String) -> Unit) {
	val item = MergeItem(block)
	synchronized(mergeMap) {
		mergeMap[key] = item
	}
	foreDelay(ms) {
		synchronized(mergeMap) {
			val item2 = mergeMap[key]
			if (item2 === item) {
				mergeMap.remove(key)
				item.block.invoke(key)
			}
		}
	}
}
package net.yet.util

/**
 * Created by entaoyang@163.com on 16/6/17.
 */

interface Progress {
	fun onStart(total: Int)

	fun onProgress(current: Int, total: Int, percent: Int)

	fun onFinish()
}

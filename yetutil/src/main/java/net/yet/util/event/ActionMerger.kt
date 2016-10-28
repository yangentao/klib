package net.yet.util.event

import net.yet.util.TaskUtil

import java.util.concurrent.atomic.AtomicBoolean

// 动作合并, 延时执行
class ActionMerger {
	private val hasTimer = AtomicBoolean(false)
	private var millSedonds = 0
	private var callback: Runnable? = null

	fun clear() {
		millSedonds = 0
		callback = null
		hasTimer.set(false)
	}

	/**
	 * @param millSec
	 * *            >0: 延时广播消息; <=0:立即广播消息
	 * *
	 * @param callback
	 */
	constructor(millSec: Int, callback: Runnable) {
		this.callback = callback
		this.millSedonds = millSec
	}

	fun setDelay(millSec: Int): ActionMerger {
		this.millSedonds = millSec
		return this
	}

	fun setCallback(callback: Runnable): ActionMerger {
		this.callback = callback
		return this
	}

	fun trigger() {
		if (millSedonds > 0) {
			if (!hasTimer.getAndSet(true)) {
				TaskUtil.foreDelay(millSedonds.toLong(), run)// flush
			}
		} else {
			TaskUtil.fore(run)
		}
	}

	private val run = Runnable {
		hasTimer.set(false)
		if (callback != null) {
			callback!!.run()
		}
	}
}

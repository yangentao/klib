package net.yet.util

import net.yet.util.event.mergeAction
import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

class Msg(val msg: String) {
	@JvmField var result = ArrayList<Any>()
	@JvmField var n1: Long = 0
	@JvmField var n2: Long = 0
	@JvmField var s1: String = ""
	@JvmField var s2: String = ""
	@JvmField var b1: Boolean = false
	@JvmField var b2: Boolean = false

	constructor(cls: Class<*>) : this(cls.name) {
	}

	fun isMsg(msg: String): Boolean {
		return this.msg == msg
	}

	fun isCls(cls: Class<*>): Boolean {
		return this.msg == cls.name
	}

	fun argB(b1: Boolean, b2: Boolean = false): Msg {
		this.b1 = b1
		this.b2 = b2
		return this
	}

	fun argN(n1: Long, n2: Long = 0): Msg {
		this.n1 = n1
		this.n2 = n2
		return this
	}

	fun argS(s1: String, s2: String = ""): Msg {
		this.s1 = s1
		this.s2 = s2
		return this
	}

	fun addResult(value: Any): Msg {
		this.result.add(value)
		return this
	}

	fun ret(value: Any): Msg {
		this.result.add(value)
		return this
	}

	fun fire() {
		MsgCenter.fire(this)
	}

	fun fireCurrent() {
		MsgCenter.fireCurrent(this)
	}

	fun fireMerge(delay: Long = 200) {
		MsgCenter.fireMerge(this, delay)
	}
}

fun fireMsg(msg: String) {
	Msg(msg).fire()
}

interface MsgListener {
	fun onMsg(msg: Msg)
}


object MsgCenter {
	private val map = MultiMap<String, MsgListener>()

	@JvmStatic fun listen(listener: MsgListener, vararg msgs: String) {
		for (m in msgs) {
			listen(m, listener)
		}
	}

	@JvmStatic fun listen(listener: MsgListener, vararg msgs: Class<*>) {
		for (m in msgs) {
			listen(m, listener)
		}
	}

	@JvmStatic fun listen(msg: String, listener: MsgListener) {
		sync(this) {
			map[msg] = listener
		}
	}

	@JvmStatic fun listen(cls: Class<*>, listener: MsgListener) {
		listen(cls.name, listener)
	}


	@JvmStatic fun remove(listener: MsgListener) {
		sync(this) {
			map.removeValue(listener)
		}
	}

	@JvmStatic fun remove(msg: String, listener: MsgListener) {
		sync(this) {
			map.removeValue(msg, listener)
		}
	}

	@JvmStatic fun remove(cls: Class<*>, listener: MsgListener) {
		remove(cls.name, listener)
	}

	@JvmStatic fun fireCurrent(msg: Msg) {
		log("fireMsg:", msg.msg)
		var ls2 = ArrayList<MsgListener>()
		sync(this) {
			val ls = map[msg.msg]
			if (ls != null) {
				ls2 = ArrayList<MsgListener>(ls)
			}
		}
		for (listener in ls2) {
			listener.onMsg(msg)
		}
	}

	@JvmStatic fun fire(msg: Msg) {
		fore {
			fireCurrent(msg)
		}
	}

	@JvmStatic fun fire(msg: String) {
		fire(Msg(msg))
	}

	@JvmStatic fun fire(cls: Class<*>) {
		fire(Msg(cls))
	}

	@JvmStatic fun fireMerge(msg: Msg, delay: Long = 200) {
		mergeAction("MsgCenter.mergeAction" + msg.msg, delay) {
			fire(msg)
		}
	}
}
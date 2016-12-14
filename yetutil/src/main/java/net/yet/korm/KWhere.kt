package net.yet.korm

import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2016-12-14.
 */


class KWhere(val value: String) {
	val args = ArrayList<Any>()

	fun addArg(s: Any): KWhere {
		args.add(s)
		return this
	}

	override fun toString(): String {
		return value
	}
}

infix fun KProperty<*>.EQ(value: String): KWhere {
	val s = fieldNameOf(this)
	return KWhere("$s=?").addArg(value)
}

infix fun KWhere.AND(value: KWhere): KWhere {
	val w = KWhere("(" + this.toString() + ") AND (" + value.toString() + ")")
	w.args.addAll(this.args)
	w.args.addAll(value.args)
	return w
}
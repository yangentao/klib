package net.yet.korm

import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2016-12-14.
 */


class OnCond(val value: String) {
	override fun toString(): String {
		return value
	}
}

infix fun KProperty<*>.EQ(value: KProperty<*>): OnCond {
	val s = fieldNameOf(this)
	val s2 = fieldNameOf(value)
	return OnCond("$s=$s2")
}
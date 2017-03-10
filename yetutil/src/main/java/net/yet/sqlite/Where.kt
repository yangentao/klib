package net.yet.sqlite

import java.util.*
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


class Where(val value: String) {
	val args = ArrayList<Any>()

	fun addArg(s: Any): Where {
		args.add(s)
		return this
	}

	override fun toString(): String {
		return value
	}
}

infix fun Where.AND(other: Where): Where {
	val w = Where("( ${this.value} ) AND ( ${other.value} )")
	w.args.addAll(this.args)
	w.args.addAll(other.args)
	return w
}

infix fun Where.OR(other: Where): Where {
	val w = Where("( ${this.value} ) OR ( ${other.value} )")
	w.args.addAll(this.args)
	w.args.addAll(other.args)
	return w
}

fun IsNull(p: KMutableProperty<*>): Where {
	val s = tableAndFieldNameOf(p)
	return Where("$s IS NULL")
}

fun NotNull(p: KMutableProperty<*>): Where {
	val s = tableAndFieldNameOf(p)
	return Where("$s IS NOT NULL")
}

infix fun KMutableProperty<*>.LIKE(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s LIKE $value")
}

infix fun KMutableProperty<*>.EQ(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s=?").addArg(value)
}

infix fun KMutableProperty<*>.NE(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s!=?").addArg(value)
}

infix fun KMutableProperty<*>.GE(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s>=?").addArg(value)
}

infix fun KMutableProperty<*>.GT(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s>?").addArg(value)
}

infix fun KMutableProperty<*>.LE(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s<=?").addArg(value)
}

infix fun KMutableProperty<*>.LT(value: String): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s<?").addArg(value)
}


infix fun KMutableProperty<*>.EQ(value: Int): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s=?").addArg(value)
}

infix fun KMutableProperty<*>.NE(value: Int): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s!=?").addArg(value)
}

infix fun KMutableProperty<*>.GE(value: Int): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s>=?").addArg(value)
}

infix fun KMutableProperty<*>.GT(value: Int): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s>?").addArg(value)
}

infix fun KMutableProperty<*>.LE(value: Int): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s<=?").addArg(value)
}

infix fun KMutableProperty<*>.LT(value: Int): Where {
	val s = tableAndFieldNameOf(this)
	return Where("$s<?").addArg(value)
}
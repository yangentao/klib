package yet.orm

import yet.ext.customNamePrefixClass
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

	val sqlArgs: Array<String> get() {
		return args.map(Any::toString).toTypedArray()
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
	val s = p.customNamePrefixClass
	return Where("$s IS NULL")
}

fun NotNull(p: KMutableProperty<*>): Where {
	val s = p.customNamePrefixClass
	return Where("$s IS NOT NULL")
}

infix fun KMutableProperty<*>.LIKE(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s LIKE $value")
}

infix fun KMutableProperty<*>.EQ(value: Any?): Where {
	val s = this.customNamePrefixClass
	if (value != null) {
		val w = Where("$s=?")
		if (value is Boolean) {
			w.addArg(if (value) 1 else 0)
		} else {
			w.addArg(value)
		}
		return w
	} else {
		return Where("ISNULL($s)")
	}
}

infix fun KMutableProperty<*>.EQ(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s=?").addArg(value)
}

infix fun KMutableProperty<*>.NE(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s!=?").addArg(value)
}

infix fun KMutableProperty<*>.GE(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s>=?").addArg(value)
}

infix fun KMutableProperty<*>.GT(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s>?").addArg(value)
}

infix fun KMutableProperty<*>.LE(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s<=?").addArg(value)
}

infix fun KMutableProperty<*>.LT(value: String): Where {
	val s = this.customNamePrefixClass
	return Where("$s<?").addArg(value)
}


infix fun KMutableProperty<*>.EQ(value: Number): Where {
	val s = this.customNamePrefixClass
	return Where("$s=$value")
}

infix fun KMutableProperty<*>.NE(value: Number): Where {
	val s = this.customNamePrefixClass
	return Where("$s!=$value")
}

infix fun KMutableProperty<*>.GE(value: Number): Where {
	val s = this.customNamePrefixClass
	return Where("$s>=$value")
}

infix fun KMutableProperty<*>.GT(value: Number): Where {
	val s = this.customNamePrefixClass
	return Where("$s>$value")
}

infix fun KMutableProperty<*>.LE(value: Number): Where {
	val s = this.customNamePrefixClass
	return Where("$s<=$value")
}

infix fun KMutableProperty<*>.LT(value: Number): Where {
	val s = this.customNamePrefixClass
	return Where("$s<$value")
}
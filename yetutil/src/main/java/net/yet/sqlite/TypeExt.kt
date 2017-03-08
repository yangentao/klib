package net.yet.sqlite

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */


val KType.isString: Boolean get() {
	return this.javaType == String::class.java
}


val KType.isInt: Boolean get() {
	return this.javaType == Int::class.java
}
val KType.isLong: Boolean get() {
	return this.javaType == Long::class.java
}
val KType.isByte: Boolean get() {
	return this.javaType == Byte::class.java
}
val KType.isShort: Boolean get() {
	return this.javaType == Short::class.java
}
val KType.isChar: Boolean get() {
	return this.javaType == Char::class.java
}
val KType.isBoolean: Boolean get() {
	return this.javaType == Boolean::class.java
}
val KType.isFloat: Boolean get() {
	return this.javaType == Float::class.java
}
val KType.isDouble: Boolean get() {
	return this.javaType == Double::class.java
}
val KType.isByteArray: Boolean get() {
	return this.javaType == ByteArray::class.java
}

fun KType.isKClass(kcls: KClass<*>): Boolean {
	return this.javaType == kcls.java
}

fun KType.isJavaClass(cls: Class<*>): Boolean {
	return this.javaType == cls
}

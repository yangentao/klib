package net.yet.sqlite

import net.yet.annotation.Name
import net.yet.orm.SqliteType
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


fun tableNameOf(c: KClass<*>): String {
	val a = c.findAnnotation<Name>()
	if (a != null && a.value.isNotEmpty()) {
		return a.value
	}
	return c.simpleName!!
}

fun fieldNameOf(p: KMutableProperty<*>): String {
	val a = p.findAnnotation<Name>()
	if (a != null && a.value.isNotEmpty()) {
		return a.value
	}
	return p.name
}

fun tableNameOfField(p: KMutableProperty<*>): String {
	return tableNameOf(p.javaField!!.declaringClass!!.kotlin)
}

fun tableAndFieldNameOf(p: KMutableProperty<*>): String {
	return tableNameOfField(p) + "." + fieldNameOf(p)
}

val SQL_TYPE_MAP: HashMap<Class<*>, SqliteType> = object : HashMap<Class<*>, SqliteType>() {
	init {
		put(String::class.java, SqliteType.TEXT)
		put(Char::class.java, SqliteType.TEXT)

		put(Boolean::class.java, SqliteType.INTEGER)
		put(Byte::class.java, SqliteType.INTEGER)
		put(Short::class.java, SqliteType.INTEGER)
		put(Int::class.java, SqliteType.INTEGER)
		put(Long::class.java, SqliteType.INTEGER)

		put(Float::class.java, SqliteType.REAL)
		put(Double::class.java, SqliteType.REAL)

		put(ByteArray::class.java, SqliteType.BLOB)
		put(Array<Byte>::class.java, SqliteType.BLOB)
	}
}
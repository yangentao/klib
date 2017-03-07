package net.yet.yetlibdemo

import net.yet.sqlite.SQL_TYPE_MAP
import net.yet.sqlite.fieldNameOf
import net.yet.util.log.log
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaType

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

class Person {
	var name: String = ""
	var age: Int = 0
}

class Worker {
	var name: String = ""
	var dollary: Int = 0
}

fun defProp(p: KMutableProperty<*>): String {
	val name = fieldNameOf(p)
	val sqlType = SQL_TYPE_MAP[p.returnType.javaType]
	if (sqlType == null) {
		return ""
	} else {
		return name + " " + sqlType.toString()
	}
}

fun testlite() {
	val s = defProp(Person::name)
	val s2 = defProp(Person::age)
	log(s)
	log(s2)
}
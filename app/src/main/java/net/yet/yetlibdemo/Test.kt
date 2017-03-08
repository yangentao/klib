package net.yet.yetlibdemo

import net.yet.sqlite.convert.IntegerDataConvert
import net.yet.sqlite.isKClass
import net.yet.util.log.log

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

class Person {
	var name: String = "def_name"
	var age: Int = 0
	var addr: String? = null
	var image: ByteArray? = null
}


fun testlite() {
	val p = Person()
	val c = IntegerDataConvert()
	log(Person::age.returnType.isKClass(Int::class))
	log(Person::age.returnType.isKClass(Long::class))
	log(Person::age.returnType.isKClass(Number::class))
}
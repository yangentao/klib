package net.yet.yetlibdemo

import net.yet.annotation.PrimaryKey
import net.yet.orm.TableClass
import net.yet.orm.Session
import net.yet.orm.LE
import net.yet.util.JsonUtil
import net.yet.util.log.logd

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

class Person {
	@PrimaryKey
	var name: String = "def_name"
	var age: Int = 0
	var addr: String? = null

	override fun toString(): String {
		return JsonUtil.toJson(this)
	}

	companion object : TableClass<Person>()
}


fun testlite() {
	Session.named("test").pushPop {
		//		val ls = Person.findAll()
//		val ls = Person.find(Person::age GE 5)
//		logd(ls)
		val ls = Person.find().where(Person::age LE 10).all<Person>()
		logd(ls)
//		val p = Person.findByKey("yang")
//		logd(p)
	}

}
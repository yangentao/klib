package net.yet.yetlibdemo

import net.yet.annotation.PrimaryKey
import net.yet.sqlite.KSession
import net.yet.util.JsonUtil
import net.yet.util.log.log

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
}


fun testlite() {
	val s = KSession.named("test")
	val p = Person()
	p.name = "yang"
	p.age = 35
	p.addr = "Jinan"
	s.save(p)
	s.sqlite.dumpMaster()
	s.sqlite.dumpTableInfo("Person")
	s.sqlite.dumpIndicesOfTable("Person")
	val ls = s.from(Person::class).all<Person>()
	for (a in ls) {
		log(a.toString())
	}
	s.from(Person::class).dump()
}
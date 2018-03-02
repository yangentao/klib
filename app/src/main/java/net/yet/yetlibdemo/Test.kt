package net.yet.yetlibdemo


import yet.anno.PrimaryKey
import yet.orm.Model
import yet.orm.ModelClass
import yet.util.JsonUtil

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

class Person : Model() {
	@PrimaryKey
	var name: String = "def_name"
	var age: Int = 0
	var addr: String? = null

	override fun toString(): String {
		return JsonUtil.toJson(this)
	}

	companion object : ModelClass<Person>()
}


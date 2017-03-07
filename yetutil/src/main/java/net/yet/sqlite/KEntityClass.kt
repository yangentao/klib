package net.yet.sqlite

import net.yet.util.log.log

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


open class KEntityClass<out T> {
	val entityCls: Class<*> = javaClass.enclosingClass as Class<T>

	fun findByID(key: String): T? {
		log("find()")
		log("enclosingClass: ", javaClass.enclosingClass?.toString())
		log("declaringClass: ", javaClass.declaringClass?.toString())
		return null
	}

	fun findByID(key: Int): T? {
		return null
	}

	fun findByID(key: Long): T? {
		return null
	}
}
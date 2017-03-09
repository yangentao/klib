package net.yet.sqlite

import java.util.*

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


open class EntityClass<T> {
	val modelClass = javaClass.enclosingClass.kotlin


	fun findAll(): ArrayList<T> {
		return KSession.current.from(modelClass).all<T>()
	}

	fun findByKey(key: String): T? {
		val mi = ModelInfo.find(modelClass)
		return KSession.current.from(modelClass).where(mi.pk!!.prop EQ key).one<T>()
	}

	fun find(w: Where): ArrayList<T> {
		return KSession.current.from(modelClass).where(w).all<T>()
	}

	fun find(): KQuery {
		return KSession.current.from(modelClass)
	}
}
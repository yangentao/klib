package net.yet.orm

import java.util.*

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


open class TableClass<T> {
	val modelClass = javaClass.enclosingClass.kotlin


	fun findAll(): ArrayList<T> {
		return Session.peek.from(modelClass).all<T>()
	}

	fun findByKey(key: String): T? {
		val mi = ModelInfo.find(modelClass)
		return Session.peek.from(modelClass).where(mi.pk!!.prop EQ key).one<T>()
	}

	fun find(w: Where): ArrayList<T> {
		return Session.peek.from(modelClass).where(w).all<T>()
	}

	fun find(): ModelQuery {
		return Session.peek.from(modelClass)
	}

	fun dumpTable() {
		Session.peek.dump(modelClass)
	}
}
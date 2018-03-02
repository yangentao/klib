package yet.orm

import android.content.ContentValues
import yet.ext.getValue
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty0

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


open class ModelClass<T : Model> {
	val modelClass = javaClass.enclosingClass.kotlin
	val MSG_RELOAD = "${modelClass.simpleName}.reload"

	open fun deleteAll(where: Where?): Int {
		return Session.peek.deleteAll(modelClass, where)
	}

	open fun delete(where: Where): Int {
		return Session.peek.delete(modelClass, where)
	}

	open fun updateAll(vararg args: Pair<KMutableProperty<*>, Any>): Int {
		return Session.peek.update(modelClass, listOf(*args), null)
	}

	open fun update(vararg args: Pair<KMutableProperty<*>, Any>, block: () -> Where?): Int {
		return Session.peek.update(modelClass, listOf(*args), block())
	}


	open fun update(args: List<Pair<KMutableProperty<*>, Any>>, where: Where?): Int {
		return Session.peek.update(modelClass, args, where)
	}

	open fun update(cv: ContentValues, where: Where?): Int {
		return Session.peek.update(modelClass, cv, where)
	}

	open fun findAll(w: Where?, block: ModelQuery.() -> Unit = {}): ArrayList<T> {
		val q = Session.peek.from(modelClass)
		if (w != null) {
			q.where(w)
		}
		q.block()
		return q.all<T>()
	}

	open fun findAll(): ArrayList<T> {
		return Session.peek.from(modelClass).all<T>()
	}


	open fun findOne(w: Where): T? {
		return findOne(w, {})
	}

	open fun findOne(block: ModelQuery.() -> Unit): T? {
		return findOne(null, block)
	}

	open fun findOne(w: Where?, block: ModelQuery.() -> Unit): T? {
		val q = Session.peek.from(modelClass)
		if (w != null) {
			q.where(w)
		}
		q.block()
		return q.one<T>()
	}

	open fun findByKey(key: Any): T? {
		val mi = ModelInfo.find(modelClass)
		return Session.peek.from(modelClass).where(mi.pk!!.prop EQ key).one<T>()
	}

	open fun exist(w: Where): Boolean {
		return findOne(w) != null
	}
	open fun count(w:Where?):Int {
		return Session.peek.from(modelClass).where(w).queryCount()
	}

	open fun maxLong(p: KMutableProperty<*>): Long? {
		val q = Session.peek.from(modelClass).select(p).desc(p).limit(1)
		return q.queryResult().longValue()
	}

	fun property0ListToWhere(vararg ps: KMutableProperty0<*>): Where? {
		var w: Where? = null
		ps.forEach {
			if (w == null) {
				w = it EQ it.getValue()
			} else {
				w = w!! AND (it EQ it.getValue())
			}
		}
		return w
	}


	fun saveAll(list: List<T>) {
		this.transaction {
			list.forEach {
				it.save()
			}
		}
	}


	inline fun <R> transaction(block: Session.() -> R): R {
		return Session.peek.transaction(block)
	}

	open fun dumpTable() {
		Session.peek.dump(modelClass)
	}
}
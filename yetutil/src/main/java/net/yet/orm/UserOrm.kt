package net.yet.orm

import net.yet.util.database.WhereNode
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-07-31.
 */


object UserOrm {

	fun delete(user: String, cls: Class<*>, pk: String): Int {
		return Orm(user).use {
			it.deleteModelByPk(cls, pk)
		}
	}

	fun delete(user: String, cls: Class<*>, pk: Long): Int {
		return Orm(user).use {
			it.deleteModelByPk(cls, pk)
		}
	}

	fun delete(user: String, cls: Class<*>, w: WhereNode): Int {
		return Orm(user).use {
			it.deleteModel(cls, w)
		}
	}

	fun deleteAll(user: String, cls: Class<*>): Int {
		return Orm(user).use {
			it.deleteAllModel(cls)
		}
	}


	fun save(user: String, model: Any): Int {
		return Orm(user).use {
			it.save(model)
		}
	}

	fun <T : Any> one(user: String, cls: Class<T>, pk: Long): T? {
		return Orm(user).use {
			it.findPk(cls, pk)
		}
	}

	fun <T : Any> one(user: String, cls: Class<T>, pk: String): T? {
		return Orm(user).use {
			it.findPk(cls, pk)
		}
	}

	fun <T : Any> one(user: String, cls: Class<T>, w: WhereNode): T? {
		return Orm(user).use {
			it.findOne(cls, w)
		}
	}

	fun <T : Any> first(user: String, cls: Class<T>, w: WhereNode, orderBy: String? = null): T? {
		return Orm(user).use {
			it.select(cls).where(w).orderBy(orderBy).limit(1).findOne()
		}
	}

	fun <T : Any> first(user: String, cls: Class<T>, orderBy: String? = null): T? {
		return Orm(user).use {
			it.select(cls).orderBy(orderBy).limit(1).findOne()
		}
	}

	fun <T : Any> all(user: String, cls: Class<T>): ArrayList<T> {
		return Orm(user).use {
			it.findAll(cls)
		}
	}

	fun <T : Any> all(user: String, cls: Class<T>, orderBy: String): ArrayList<T> {
		return Orm(user).use {
			it.findAll(cls, orderBy)
		}
	}

	fun <T : Any> all(user: String, cls: Class<T>, w: WhereNode): ArrayList<T> {
		return Orm(user).use {
			it.findAll(cls, w)
		}
	}

	fun <T : Any> all(user: String, cls: Class<T>, w: WhereNode, orderBy: String): ArrayList<T> {
		return Orm(user).use {
			it.findAll(cls, w, orderBy)
		}
	}
}
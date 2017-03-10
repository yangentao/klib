package net.yet.orm

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */


abstract class TableModel {

	fun save() {
		Session.peek.save(this)
	}

	fun delete() {
		Session.peek.deleteByPK(this)
	}

}
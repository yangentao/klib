package net.yet.orm

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */


abstract class TableModel {

	open fun save(): Boolean {
		return Session.peek.save(this) > 0
	}

	open fun delete(): Int {
		return Session.peek.deleteByPK(this)
	}

}
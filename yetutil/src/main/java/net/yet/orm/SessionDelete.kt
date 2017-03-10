package net.yet.orm

import net.yet.util.log.loge
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2017-03-10.
 */


fun Session.deleteAll(cls: KClass<*>): Int {
	return delete(ModelInfo.find(cls), null)
}

fun Session.delete(cls: KClass<*>, w: Where): Int {
	val mi = ModelInfo.find(cls)
	return delete(mi, w)
}

fun Session.deleteByPK(model: Any): Int {
	val mi = ModelInfo.find(model::class)
	val pk = mi.pk
	if (pk == null) {
		loge("NO primary key define in ${mi.tableName}")
		return 0
	} else {
		val pkVal = pk.getValue(model)
		if (pkVal == null) {
			loge("Primary Key Value is NULL")
			return 0
		}
		return delete(mi, pk.prop EQ pkVal.toString())
	}

}

private fun Session.delete(mi: ModelInfo, w: Where?): Int {
	TableCreator.check(db, mi)
	return db.delete(mi.tableName, w?.value, w?.sqlArgs)
}
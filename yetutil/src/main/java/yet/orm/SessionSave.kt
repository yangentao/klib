package yet.orm

import android.content.ContentValues
import yet.ext.isTypeInt
import yet.ext.isTypeLong
import yet.util.log.xlog
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-10.
 */


fun Session.save(model: Any): Int {
	val mi = ModelInfo.find(model::class)
	TableCreator.check(db, mi)
	if (mi.hasPK) {
		val pkVal = mi.getPKValue(model)
		if (pkVal != null) {
			if (existByPK(mi, pkVal)) {
				return updateByPK(model)
			}
		}
		val id = insert(model)
		return if (id > 0) 1 else 0
	}
	val id = replace(model)
	return if (id > 0) 1 else 0

}

fun Session.update(cls: KClass<*>, args: List<Pair<KMutableProperty<*>, Any?>>, where: Where?): Int {
	return this.update(cls, yet.orm.buildContentValues(args), where)
}

fun Session.update(cls: KClass<*>, cv: ContentValues, where: Where?): Int {
	val mi = ModelInfo.find(cls)
	TableCreator.check(db, mi)
	return db.update(mi.tableName, cv, where?.value, where?.sqlArgs)
}

fun Session.updateByPK(model: Any): Int {
	val mi = ModelInfo.find(model::class)
	TableCreator.check(db, mi)
	val values = mi.toContentValues(model)
	if (mi.pk == null) {
		xlog.e("没有找到主键" + mi.tableName)
		return 0
	}
	val pk = mi.pk ?: return 0
	val pkValue = values.get(pk.shortName)
	if (pkValue == null) {
		xlog.e("主键没有值" + mi.tableName)
		return 0
	}
	values.remove(pk.shortName)
	return db.update(mi.tableName, values, pk.fullName + "=?", arrayOf(pkValue.toString()))
}

fun Session.insert(model: Any): Long {
	return insertOrReplace(model, false)
}

fun Session.replace(model: Any): Long {
	return insertOrReplace(model, true)
}

private fun Session.insertOrReplace(model: Any, replace: Boolean): Long {
	val mi = ModelInfo.find(model::class)
	TableCreator.check(db, mi)
	val values = mi.toContentValues(model)
	var assignId = false
	if (mi.pk?.autoInc == true) {//整形自增
		val pkValue = values.get(mi.pk!!.shortName)
		if (pkValue == null || 0 == (pkValue as Number).toInt()) {
			values.remove(mi.pk!!.shortName)
			assignId = true
		}
	}
	val id = if (replace) {
		db.replace(mi.tableName, null, values)
	} else {
		db.insert(mi.tableName, null, values)
	}
	if (assignId && id != -1L) {
		try {
			val p = mi.pk!!.prop
			if (p.isTypeInt) {
				mi.pk?.prop?.setter?.call(model, id.toInt())
			} else if (p.isTypeLong) {
				mi.pk?.prop?.setter?.call(model, id)
			}
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
		}

	}
	return id
}
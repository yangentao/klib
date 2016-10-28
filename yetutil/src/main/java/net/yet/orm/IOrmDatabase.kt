package net.yet.orm

import android.content.ContentValues
import android.database.Cursor
import net.yet.util.Util
import net.yet.util.database.EQ
import net.yet.util.database.IDatabaseExt
import net.yet.util.database.LiteBase
import net.yet.util.database.WhereNode
import net.yet.util.empty
import net.yet.util.log.xlog
import java.lang.reflect.Field

/**
 * Created by entaoyang@163.com on 16/5/15.
 */

interface IOrmDatabase : IDatabaseExt {

	val liteBase: LiteBase get

	fun findInfo(cls: Class<*>): TableInfo

	fun tableName(modelClass: Class<*>): String {
		val info = findInfo(modelClass)
		return info.tableName
	}

	fun dropTable(cls: Class<*>) {
		dropTable(findInfo(cls).tableName)
	}


	fun select(modelClass: Class<*>): OrmQuery {
		return OrmQuery(liteBase, modelClass)
	}

	fun existModelPK(cls: Class<*>, pk: String): Boolean {
		return select(cls).wherePk(pk).exist()
	}

	fun existModelPK(cls: Class<*>, pk: Long): Boolean {
		return select(cls).wherePk(pk).exist()
	}

	fun countModel(cls: Class<*>): Int {
		return countTable(tableName(cls))
	}


	fun queryModel(modelCls: Class<*>, w: WhereNode, orderBy: String? = null): Cursor {
		val tableName = findInfo(modelCls).tableName
		return queryTable(tableName, w, orderBy)
	}


	fun deleteAllModel(cls: Class<*>): Int {
		return deleteAll(tableName(cls))
	}

	fun deleteModelByPk(modelClass: Class<*>, pk: Long): Int {
		val info = findInfo(modelClass)
		return delete(info.tableName, info.pkName EQ pk)
	}

	fun deleteModelByPk(modelClass: Class<*>, pk: String): Int {
		val info = findInfo(modelClass)
		return delete(info.tableName, info.pkName EQ pk)
	}

	fun deleteModel(modelClass: Class<*>, where: WhereNode): Int {
		val info = findInfo(modelClass)
		return delete(info.tableName, where)
	}

	fun deleteModelByPk(model: Any): Int {
		val tableInfo = findInfo(model.javaClass)
		val pkVal = getPkSqliteValue(tableInfo, model)
		return when (pkVal) {
			null -> {
				xlog.e("PK is NULL!")
				0
			}
			is String -> delete(tableInfo.tableName, tableInfo.pkName EQ pkVal.toString())
			is Number -> delete(tableInfo.tableName, tableInfo.pkName EQ pkVal.toLong())
			else -> {
				0
			}
		}
	}


	fun updateModelColumns(model: Any, includeClumns: Array<String>, where: WhereNode): Int {
		val tableInfo = findInfo(model.javaClass)
		val values = makeValues(model, tableInfo, Util.asSet(*includeClumns))
		return update(tableInfo.tableName, values, where)
	}

	fun updateModelByPk(model: Any): Int {
		val tableInfo = findInfo(model.javaClass)
		if (tableInfo.pkName == null) {
			xlog.e("没有找到主键" + model.javaClass.name)
			return 0
		}
		val values = makeValues(model, tableInfo)
		val pkValue = values.get(tableInfo.pkName)
		if (pkValue == null) {
			xlog.e("主键没有值" + model.javaClass.name)
			return 0
		}
		values.remove(tableInfo.pkName)
		if (pkValue is Number) {
			return update(tableInfo.tableName, values, tableInfo.pkName EQ pkValue.toLong())
		} else {
			return update(tableInfo.tableName, values, tableInfo.pkName EQ pkValue.toString())
		}
	}


	fun updateModelByRowId(model: Any, rowid: Long): Int {
		return updateModel(model, "_ROWID_" EQ rowid)
	}

	fun updateModel(model: Any, w: WhereNode): Int {
		val tableInfo = findInfo(model.javaClass)
		val values = makeValues(model, tableInfo)
		return update(tableInfo.tableName, values, w)
	}

	fun save(model: Any): Int {
		return this.insertOrUpdate(model)
	}

	//根据PK来插入, 或更新
	fun insertOrUpdate(model: Any): Int {
		val tableInfo = findInfo(model.javaClass)
		val pkVal = getPkSqliteValue(tableInfo, model) ?: return insertModel(model).toInt()
		val exist = when (pkVal) {
			is String -> existModelPK(model.javaClass, pkVal)
			is Number -> existModelPK(model.javaClass, pkVal.toLong())
			else -> false
		}
		if (exist) {
			xlog.d("exist, do update")
			return updateModelByPk(model)
		} else {
			xlog.d("not exist, do insert")
			val id = insertModel(model)
			return if (id > 0) 1 else 0
		}
	}

	fun insertModel(model: Any): Long {
		return insertOrreplaceModel(model, false)
	}

	fun replaceModel(model: Any): Long {
		return insertOrreplaceModel(model, true)
	}

	private fun insertOrreplaceModel(model: Any, replace: Boolean): Long {
		val tableInfo = findInfo(model.javaClass)
		val values = makeValues(model, tableInfo)
		var assignId = false
		if (tableInfo.pkAutoInc) {//整形自增
			val pkValue = values.get(tableInfo.pkName)
			if (pkValue == null || 0 == (pkValue as Number).toInt()) {
				values.remove(tableInfo.pkName)
				assignId = true
			}
		}
		val id = if (replace) {
			replace(tableInfo.tableName, values)
		} else {
			insert(tableInfo.tableName, values)
		}
		if (assignId && id != -1L) {
			try {
				val type = tableInfo.pkField.type
				if (type == Integer.TYPE || type == Int::class.java) {
					tableInfo.pkField.set(model, id.toInt())
				} else if (type == java.lang.Long.TYPE || type == Long::class.java) {
					tableInfo.pkField.set(model, id)
				}
			} catch (e: IllegalAccessException) {
				e.printStackTrace()
			}

		}
		return id
	}


	private fun getPkSqliteValue(tableInfo: TableInfo, obj: Any): Any? {
		if (tableInfo.pkField != null) {
			return toSqliteValue(tableInfo, obj, tableInfo.pkField)
		}
		return null
	}

	private fun toSqliteValue(tableInfo: TableInfo, model: Any, field: Field): Any? {
		try {
			field.isAccessible = true
			val value: Any? = field.get(model)//原始值
			if (value != null) {
				//非空时, 先找转换器, 如果没有转换器, 则使用默认的转换.
				val typeSerializer = tableInfo.findTypeSerializer(field)
				if (typeSerializer != null) {
					return typeSerializer.toSqliteValue(field.type, value)
				} else {
					return ValueTypeUtil.toSqliteValue(field.type, value)
				}
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		}
		return null
	}

	private fun makeValues(model: Any, tableInfo: TableInfo, columns: Set<String> = emptySet()): ContentValues {
		val values = ContentValues(12)
		for ((fieldName, field) in tableInfo.nameFieldMap) {
			if (!columns.empty()) {
				if (fieldName !in columns) {
					continue
				}
			}
			val value = toSqliteValue(tableInfo, model, field)
			when (value) {
				null -> values.putNull(fieldName)
				is String -> values.put(fieldName, value)
				is Long -> values.put(fieldName, value)
				is Double -> values.put(fieldName, value)
				is ByteArray -> values.put(fieldName, value)
				else -> {
					xlog.e("错误的数据类型 ", fieldName, " 只能是String,long,double,byte[]之一")
				}
			}
		}
		return values
	}

}
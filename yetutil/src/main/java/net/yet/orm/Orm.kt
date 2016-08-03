package net.yet.orm

import android.content.ContentValues
import android.database.Cursor
import net.yet.ext.empty
import net.yet.file.UserFile
import net.yet.orm.serial.TypeSerializer
import net.yet.util.JsonUtil
import net.yet.util.database.LiteBase
import net.yet.util.database.Query
import net.yet.util.database.WhereNode
import net.yet.util.xlog
import java.io.Closeable
import java.util.*

/**
 * model类必须声明一个主键, 可以是字符串或数值类型.
 * 自增只在int和long类型的主键值是0的时候有效
 *
 *
 * 不支持外键. 主要是单表操作
 * 在使用之前, 在Application的onCreate中调用
 * Orm.init(application, 1, null);
 * Orm.finish();
 *
 *
 * 不想被存储的变量, 可以使用final,transient来修饰, 或者使用Exclude注释
 *
 *
 * Created by yangentao on 2015/11/7.
 * entaoyang@163.com
 */
class Orm(val lb: LiteBase = LiteBase("orm.db")) : IOrmDatabase, Closeable {

	override val liteBase: LiteBase get() = lb
	val path: String get() = lb.path

	constructor(username: String) : this(
			if (username.empty()) {
				LiteBase("orm.db")
			} else {
				LiteBase(UserFile.file(username, "orm.db"))
			}
	) {

	}

	override fun close() {
		lb.close()
	}

	fun addTypeSerializer(fieldType: Class<*>, typeSerializer: TypeSerializer<Any, Any>) {
		TableInfo.addTypeSerializer(fieldType, typeSerializer)
	}

	inline fun trans(block: (Orm) -> Unit): Boolean {
		return lb.trans {
			block(this)
		}
	}

	val inTransaction: Boolean get() = lb.inTransaction

	override fun execSQL(sql: String, vararg args: Any) {
		lb.execSQL(sql, *args)
	}

	override fun rawQuery(sql: String, vararg args: String): Cursor? {
		return lb.rawQuery(sql, *args)
	}

	override fun select(vararg cols: String): Query {
		return lb.select(*cols)
	}

	override fun insert(table: String, values: ContentValues): Long {
		return lb.insert(table, values)
	}

	override fun replace(table: String, values: ContentValues): Long {
		return lb.replace(table, values)
	}

	override fun update(table: String, values: ContentValues, whereClause: String?, vararg whereArgs: String): Int {
		return lb.update(table, values, whereClause, *whereArgs)
	}

	override fun delete(table: String, whereClause: String?, vararg whereArgs: String): Int {
		return lb.delete(table, whereClause, *whereArgs)
	}


	fun <T : Any> findPk(cls: Class<T>, pk: Long): T? {
		return select(cls).wherePk(pk).findOne<T>()
	}

	fun <T : Any> findPk(cls: Class<T>, pk: String): T? {
		return select(cls).wherePk(pk).findOne<T>()
	}

	fun <T : Any> findOne(cls: Class<T>, w: WhereNode): T? {
		return select(cls).where(w).findOne<T>()
	}


	fun <T : Any> findAll(cls: Class<T>, orderBy: String? = null): ArrayList<T> {
		return select(cls).orderBy(orderBy).findAll<T>()
	}

	fun <T : Any> findAll(cls: Class<T>, w: WhereNode, orderBy: String? = null): ArrayList<T> {
		return select(cls).where(w).orderBy(orderBy).findAll<T>()
	}


	fun dumpTable(cls: Class<*>) {
		val ls: List<Any> = findAll(cls)
		for (m in ls) {
			xlog.d(JsonUtil.toJson(m))
		}
	}

	override fun findInfo(cls: Class<*>): TableInfo {
		return TableInfo.findOrCreateTable(lb, cls)
	}
}

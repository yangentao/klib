package net.yet.sqlite

import android.content.ContentValues
import net.yet.annotation.*
import net.yet.orm.SqliteType
import net.yet.sqlite.convert.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */


class PropInfo(val prop: KMutableProperty<*>, val convert: DataConvert) {
	val name: String = tableAndFieldNameOf(prop)
	val length: Int = prop.findAnnotation<Length>()?.value ?: 0
	val notNull: Boolean = prop.findAnnotation<NotNull>() != null
	val unique: Boolean = prop.findAnnotation<Unique>() != null
	val index: Boolean = prop.findAnnotation<Index>() != null
	val autoInc: Boolean = prop.findAnnotation<AutoInc>() != null
	val isPrimaryKey: Boolean = prop.findAnnotation<PrimaryKey>() != null


	init {
	}


	companion object {


		private val map = HashMap<KClass<*>, DataConvert>()
		fun findConvert(cls: KClass<out DataConvert>): DataConvert {
			var c = map[cls]
			if (c == null) {
				c = cls.createInstance()
				map[cls] = c
			}
			return c
		}
	}
}

class ModelInfo(val cls: KClass<*>) {
	var tableName: String = tableNameOf(cls)
	var pk: PropInfo? = null

	var allProp = ArrayList<PropInfo>()
	var namePropMap = HashMap<String, PropInfo>(16)

	init {
		val propList = cls.declaredMemberProperties.filter {
			acceptProp(it)
		}.map { it as KMutableProperty<*> }

		for (p in propList) {
			val c = findConvertOf(p)
			if (c != null) {
				val pi = PropInfo(p, c)
				namePropMap[tableAndFieldNameOf(p)] = pi
				allProp.add(pi)
				if (pi.isPrimaryKey) {
					if (pk != null) {
						throw  RuntimeException("multi primay key declared @ ${tableName}")
					}
					pk = pi
				}
			}
		}
	}

	private fun findConvertOf(p: KMutableProperty<*>): DataConvert? {
		val a = p.findAnnotation<Convert>()
		return if (a != null) {
			PropInfo.findConvert(a.value)
		} else {
			val t = p.returnType
			if (intConvert.accessModelType(t)) {
				intConvert
			} else if (textConvert.accessModelType(t)) {
				textConvert
			} else if (realConvert.accessModelType(t)) {
				realConvert
			} else if (blobConvert.accessModelType(t)) {
				blobConvert
			} else {
				null
			}
		}
	}

	fun createInstance(): Any {
		return cls.createInstance()
	}

	fun acceptProp(p: KProperty<*>): Boolean {
		if ((p is KMutableProperty<*>) and !p.isAbstract and !p.isConst and !p.isLateinit and (p.javaField != null)) {
			val e = p.findAnnotation<Exclude>()
			return e == null
		}
		return false
	}

	fun toContentValues(model:Any):ContentValues{
		val cv = ContentValues(allProp.size + 4)
		for (pi in allProp) {
			when (pi.convert.sqlType) {
				SqliteType.TEXT -> {
					val v: String? = pi.convert.toSqlText(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.name)
					} else {
						cv.put(pi.name, v)
					}
				}
				SqliteType.INTEGER -> {
					val v: Long? = pi.convert.toSqlInteger(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.name)
					} else {
						cv.put(pi.name, v)
					}
				}
				SqliteType.REAL -> {
					val v: Double? = pi.convert.toSqlReal(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.name)
					} else {
						cv.put(pi.name, v)
					}
				}
				SqliteType.BLOB -> {
					val v: ByteArray? = pi.convert.toSqlBlob(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.name)
					} else {
						cv.put(pi.name, v)
					}
				}
			}
		}
		return cv
	}


	companion object {
		private val intConvert = IntegerDataConvert()
		private val realConvert = RealDataConvert()
		private val textConvert = TextDataConvert()
		private val blobConvert = BlobDataConvert()
		private val map = HashMap<KClass<*>, ModelInfo>(16)

		fun find(cls: KClass<*>): ModelInfo {
			synchronized(map) {
				var mi = map[cls]
				if (mi == null) {
					mi = ModelInfo(cls)
					map[cls] = mi
				}
				return mi
			}
		}
	}
}
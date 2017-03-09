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
	//table.field
	val fullName: String = tableAndFieldNameOf(prop)
	//field
	val shortName:String = fieldNameOf(prop)
	val length: Int = prop.findAnnotation<Length>()?.value ?: 0
	val notNull: Boolean = prop.findAnnotation<NotNull>() != null
	val unique: Boolean = prop.findAnnotation<Unique>() != null
	val index: Boolean = prop.findAnnotation<Index>() != null
	val autoInc: Boolean = prop.findAnnotation<AutoInc>() != null
	val isPrimaryKey: Boolean = prop.findAnnotation<PrimaryKey>() != null


	init {
	}

	fun defineColumn(): String {
		val sb = StringBuilder(64)
		sb.append(shortName).append(" ").append(convert.sqlType.toString())
		if (length > 0) {
			sb.append("($length) ")
		}
		if (isPrimaryKey) {
			sb.append(" PRIMARY KEY ")
			if (autoInc) {
				sb.append(" AUTOINCREMENT ")
			}
		} else {
			if (notNull) {
				sb.append(" NOT NULL ")
			}
			if (unique) {
				sb.append(" UNIQUE ")
			}
		}
		return sb.toString()
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
	val tableName: String = tableNameOf(cls)
	val pk: PropInfo?
	val allProp = ArrayList<PropInfo>()
	val namePropMap = HashMap<String, PropInfo>(16)
	val uniques = ArrayList<String>()
	val autoAlterTable:Boolean

	init {
		val propList = cls.declaredMemberProperties.filter {
			acceptProp(it)
		}.map { it as KMutableProperty<*> }

		val u = cls.findAnnotation<Uniques>()
		if (u != null) {
			uniques.addAll(u.value)
		}
		val at = cls.findAnnotation<AutoAlterTable>()
		autoAlterTable = at?.value ?: true

		var findPK = false
		for (p in propList) {
			val c = findConvertOf(p)
			if (c != null) {
				val pi = PropInfo(p, c)
				namePropMap[pi.shortName] = pi
				allProp.add(pi)
				if (pi.isPrimaryKey) {
					if (findPK) {
						throw  RuntimeException("multi primay key declared @ ${tableName}")
					}
					findPK = true
				}
			}
		}
		pk = allProp.find { it.isPrimaryKey }
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

	val hasPK: Boolean get() = pk != null

	fun getPKValue(model: Any): Any? {
		return pk?.prop?.getter?.call(model)
	}

	fun toContentValues(model: Any): ContentValues {
		val cv = ContentValues(allProp.size + 4)
		for (pi in allProp) {
			when (pi.convert.sqlType) {
				SqliteType.TEXT -> {
					val v: String? = pi.convert.toSqlText(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
					}
				}
				SqliteType.INTEGER -> {
					val v: Long? = pi.convert.toSqlInteger(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
					}
				}
				SqliteType.REAL -> {
					val v: Double? = pi.convert.toSqlReal(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
					}
				}
				SqliteType.BLOB -> {
					val v: ByteArray? = pi.convert.toSqlBlob(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
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
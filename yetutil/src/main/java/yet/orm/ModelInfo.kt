package yet.orm

import android.content.ContentValues
import yet.anno.*
import yet.database.SQLType
import yet.ext.*
import yet.orm.convert.*
import yet.util.log.logd
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */

class PropInfo(val prop: KMutableProperty<*>, val convert: DataConvert) {
	//table.field
	val fullName: String = prop.customNamePrefixClass
	//field
	val shortName: String = prop.customName
	val length: Int = prop.findAnnotation<Length>()?.value ?: 0
	val notNull: Boolean = prop.hasAnnotation<NotNull>()
	val autoInc: Boolean = prop.hasAnnotation<AutoInc>()
	val isPrimaryKey: Boolean = prop.hasAnnotation<PrimaryKey>()
	val unique: Boolean = prop.hasAnnotation<Unique>()
	val uniqueName: String = prop.findAnnotation<Unique>()?.name ?: ""
	val index: Boolean = prop.hasAnnotation<Index>()
	val indexName: String = prop.findAnnotation<Index>()?.name ?: ""

	init {
	}

	fun getValue(model: Any): Any? {
		return prop.getter.call(model)
	}

	fun setValue(model: Any, value: Any?) {
		prop.setter.call(model, value)
	}

	fun defineColumn(definePK: Boolean): String {
		val sb = StringBuilder(64)
		sb.append(shortName).append(" ").append(convert.sqlType.toString())
		if (length > 0) {
			sb.append("($length) ")
		}
		if (definePK && isPrimaryKey) {
			sb.append(" PRIMARY KEY ")
			if (autoInc) {
				sb.append(" AUTOINCREMENT ")
			}
		}
		if (notNull) {
			sb.append(" NOT NULL ")
		}
		if (unique) {
			if (uniqueName.isEmpty()) {
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
	val tableName: String = cls.customName
	val allPK: List<PropInfo>
	val pk: PropInfo? get() {
		return allPK.firstOrNull()
	}
	val allProp = ArrayList<PropInfo>()
	val shortNamePropMap = HashMap<String, PropInfo>(16)
	val uniques = ArrayList<String>()
	val autoAlterTable: Boolean

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

		for (p in propList) {
			val c = findConvertOf(p)
			if (c != null) {
				val pi = PropInfo(p, c)
				shortNamePropMap[pi.shortName] = pi
				allProp.add(pi)
			} else {
				logd("Convert is NULL: ", p.customNamePrefixClass)
			}
		}
		allPK = allProp.filter { it.isPrimaryKey }
	}

	private fun findConvertOf(p: KMutableProperty<*>): DataConvert? {
		val a = p.findAnnotation<Convert>()
		return if (a != null) {
			val c = PropInfo.findConvert(a.value)
			if (!c.acceptProperty(p)) {
				throw TypeDismatchException(a.value.simpleName + " does not accept " + p.returnType + " " + p.customNamePrefixClass)
			}
			c
		} else {
			if (intConvert.acceptProperty(p)) {
				intConvert
			} else if (textConvert.acceptProperty(p)) {
				textConvert
			} else if (realConvert.acceptProperty(p)) {
				realConvert
			} else if (blobConvert.acceptProperty(p)) {
				blobConvert
			} else if (hashSetConvert.acceptProperty(p)) {
				hashSetConvert
			} else if (arrayListConvert.acceptProperty(p)) {
				arrayListConvert
			} else {
				null
			}
		}
	}

	fun createInstance(): Any {
		return cls.createInstance()
	}

	fun acceptProp(p: KProperty<*>): Boolean {
		if (p !is KMutableProperty<*>) {
			return false
		}
		if (p.isAbstract || p.isConst || p.isLateinit) {
			return false
		}
		if (!p.isPublic) {
			return false
		}
		if (p.javaField == null) {
			return false
		}
		if (p.hasAnnotation<Exclude>()) {
			return false
		}
		return true
	}

	val hasPK: Boolean get() = pk != null

	fun getPKValue(model: Any): Any? {
		return pk?.prop?.getter?.call(model)
	}

	fun toContentValues(model: Any): ContentValues {
		val cv = ContentValues(allProp.size + 4)
		for (pi in allProp) {
			when (pi.convert.sqlType) {
				SQLType.TEXT -> {
					val v: String? = pi.convert.toSqlText(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
					}
				}
				SQLType.INTEGER -> {
					val v: Long? = pi.convert.toSqlInteger(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
					}
				}
				SQLType.REAL -> {
					val v: Double? = pi.convert.toSqlReal(model, pi.prop)
					if (v == null) {
						cv.putNull(pi.shortName)
					} else {
						cv.put(pi.shortName, v)
					}
				}
				SQLType.BLOB -> {
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
		private val hashSetConvert = HashSetConvert()
		private val arrayListConvert = ArrayListConvert()
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
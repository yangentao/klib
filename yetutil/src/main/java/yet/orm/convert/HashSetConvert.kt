package yet.orm.convert

import yet.database.SQLType
import yet.ext.firstGenericType
import yet.ext.isTypeHashSet
import yet.util.log.logd
import yet.yson.TypeTake
import yet.yson.Yson
import yet.yson.YsonArray
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */

//HashSet<String>, Char, Boolean, Byte, Short , Int, Long, Float, Double
open class HashSetConvert : DataConvert() {


	final override val sqlType: SQLType = SQLType.TEXT

	override fun acceptProperty(p: KMutableProperty<*>): Boolean {
		if (!p.isTypeHashSet) {
			logd("不是hashSet")
			return false
		}
		val argType = p.firstGenericType ?: return false
		val b = argType in map.keys
//		val pcls = FieldUtil.getFirstFieldGenericParamType(p.javaField) ?: return false
//		val b = pcls in map.keys
		if (!b) {
			logd("不支持的hashSet参数类型")
		}
		return b
	}

	override fun toSqlText2(property: KMutableProperty<*>, value: Any): String? {
		return Yson.toYson(value).toString()
	}

	override fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		if (value.length > 1) {
//			val pcls = FieldUtil.getFirstFieldGenericParamType(property.javaField)
			val argType = property.returnType.arguments.firstOrNull()?.type?.classifier!!
			val t = map[argType]
			if (t == null) {
				fromSqlNull(model, property)
				return
			}
			val yar = YsonArray(value)
			val v = when (argType) {
				String::class -> Yson.toModelGeneric<HashSet<String>>(yar, t)
				java.lang.Character::class -> Yson.toModelGeneric<HashSet<Char>>(yar, t)
				java.lang.Boolean::class -> Yson.toModelGeneric<HashSet<Boolean>>(yar, t)
				java.lang.Byte::class -> Yson.toModelGeneric<HashSet<Byte>>(yar, t)
				java.lang.Short::class -> Yson.toModelGeneric<HashSet<Short>>(yar, t)
				java.lang.Integer::class -> Yson.toModelGeneric<HashSet<Int>>(yar, t)
				java.lang.Long::class -> Yson.toModelGeneric<HashSet<Long>>(yar, t)
				java.lang.Float::class -> Yson.toModelGeneric<HashSet<Float>>(yar, t)
				java.lang.Double::class -> Yson.toModelGeneric<HashSet<Double>>(yar, t)
				else -> null
			}
			if (v == null) {
				fromSqlNull(model, property)
			} else {
				property.setter.call(model, v)
			}
		} else {
			fromSqlNull(model, property)
		}
	}

	companion object {
		val map: HashMap<KClass<*>, KType> = hashMapOf(
				String::class to object : TypeTake<HashSet<String>>() {}.type,
				java.lang.Character::class to object : TypeTake<HashSet<Char>>() {}.type,
				java.lang.Boolean::class to object : TypeTake<HashSet<Boolean>>() {}.type,
				java.lang.Byte::class to object : TypeTake<HashSet<Byte>>() {}.type,
				java.lang.Short::class to object : TypeTake<HashSet<Short>>() {}.type,
				java.lang.Integer::class to object : TypeTake<HashSet<Int>>() {}.type,
				java.lang.Long::class to object : TypeTake<HashSet<Long>>() {}.type,
				java.lang.Float::class to object : TypeTake<HashSet<Float>>() {}.type,
				java.lang.Double::class to object : TypeTake<HashSet<Double>>() {}.type

		)
	}

}
package yet.orm.convert

import com.google.gson.reflect.TypeToken
import yet.database.SQLType
import yet.ext.firstGenericType
import yet.ext.isTypeHashSet
import yet.util.JsonUtil
import yet.util.log.logd
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

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
		val argType = property.firstGenericType ?: return null

//		val pcls = FieldUtil.getFirstFieldGenericParamType(property.javaField)
		val t = map[argType]
		return JsonUtil.toJsonGeneric(value, t)
	}

	override fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		if (value.length > 1) {
//			val pcls = FieldUtil.getFirstFieldGenericParamType(property.javaField)
			val argType = property.returnType.arguments.firstOrNull()?.type?.classifier!!
			val t = map[argType]
			val v = when (argType) {
				String::class -> JsonUtil.fromJsonGeneric<HashSet<String>>(value, t)
				java.lang.Character::class -> JsonUtil.fromJsonGeneric<HashSet<Char>>(value, t)
				java.lang.Boolean::class -> JsonUtil.fromJsonGeneric<HashSet<Boolean>>(value, t)
				java.lang.Byte::class -> JsonUtil.fromJsonGeneric<HashSet<Byte>>(value, t)
				java.lang.Short::class -> JsonUtil.fromJsonGeneric<HashSet<Short>>(value, t)
				java.lang.Integer::class -> JsonUtil.fromJsonGeneric<HashSet<Int>>(value, t)
				java.lang.Long::class -> JsonUtil.fromJsonGeneric<HashSet<Long>>(value, t)
				java.lang.Float::class -> JsonUtil.fromJsonGeneric<HashSet<Float>>(value, t)
				java.lang.Double::class -> JsonUtil.fromJsonGeneric<HashSet<Double>>(value, t)
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
		val map: HashMap<KClass<*>, Type> = hashMapOf(
				String::class to object : TypeToken<HashSet<String>>() {}.type,
				java.lang.Character::class to object : TypeToken<HashSet<Char>>() {}.type,
				java.lang.Boolean::class to object : TypeToken<HashSet<Boolean>>() {}.type,
				java.lang.Byte::class to object : TypeToken<HashSet<Byte>>() {}.type,
				java.lang.Short::class to object : TypeToken<HashSet<Short>>() {}.type,
				java.lang.Integer::class to object : TypeToken<HashSet<Int>>() {}.type,
				java.lang.Long::class to object : TypeToken<HashSet<Long>>() {}.type,
				java.lang.Float::class to object : TypeToken<HashSet<Float>>() {}.type,
				java.lang.Double::class to object : TypeToken<HashSet<Double>>() {}.type

		)
	}

}
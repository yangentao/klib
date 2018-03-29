package yet.orm.convert

import com.google.gson.reflect.TypeToken
import yet.database.SQLType
import yet.ext.isTypeArrayList
import yet.ref.FieldUtil
import yet.util.JsonUtil
import java.lang.reflect.Type
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */

//ArrayList<String>, Char, Boolean, Byte, Short , Int, Long, Float, Double
open class ArrayListConvert : DataConvert() {


	final override val sqlType: SQLType = SQLType.TEXT

	override fun acceptProperty(p: KMutableProperty<*>): Boolean {
		if (!p.isTypeArrayList) {
			return false
		}
		val pcls = FieldUtil.getFirstFieldGenericParamType(p.javaField) ?: return false
		return pcls in map.keys
	}

	override fun toSqlText2(property: KMutableProperty<*>, value: Any): String? {
		val pcls = FieldUtil.getFirstFieldGenericParamType(property.javaField)
		val t = map[pcls]
		return JsonUtil.toJsonGeneric(value, t)
	}

	override fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		if (value.length > 1) {
			val pcls = FieldUtil.getFirstFieldGenericParamType(property.javaField)
			val t = map[pcls]
			val v = when (pcls) {
				String::class.java -> JsonUtil.fromJsonGeneric<ArrayList<String>>(value, t)
				java.lang.Character::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Char>>(value, t)
				java.lang.Boolean::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Boolean>>(value, t)
				java.lang.Byte::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Byte>>(value, t)
				java.lang.Short::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Short>>(value, t)
				java.lang.Integer::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Int>>(value, t)
				java.lang.Long::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Long>>(value, t)
				java.lang.Float::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Float>>(value, t)
				java.lang.Double::class.java -> JsonUtil.fromJsonGeneric<ArrayList<Double>>(value, t)
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
		val map: HashMap<Class<*>, Type> = hashMapOf(
				String::class.java to object : TypeToken<ArrayList<String>>() {}.type,
				java.lang.Character::class.java to object : TypeToken<ArrayList<Char>>() {}.type,
				java.lang.Boolean::class.java to object : TypeToken<ArrayList<Boolean>>() {}.type,
				java.lang.Byte::class.java to object : TypeToken<ArrayList<Byte>>() {}.type,
				java.lang.Short::class.java to object : TypeToken<ArrayList<Short>>() {}.type,
				java.lang.Integer::class.java to object : TypeToken<ArrayList<Int>>() {}.type,
				java.lang.Long::class.java to object : TypeToken<ArrayList<Long>>() {}.type,
				java.lang.Float::class.java to object : TypeToken<ArrayList<Float>>() {}.type,
				java.lang.Double::class.java to object : TypeToken<ArrayList<Double>>() {}.type

		)
	}

}
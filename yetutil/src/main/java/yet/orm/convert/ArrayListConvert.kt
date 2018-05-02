package yet.orm.convert

import yet.database.SQLType
import yet.ext.firstGenericType
import yet.ext.isTypeArrayList
import yet.yson.TypeTake
import yet.yson.Yson
import yet.yson.YsonArray
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType

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
		return p.firstGenericType in map.keys
	}

	override fun toSqlText2(property: KMutableProperty<*>, value: Any): String? {
		return Yson.toYson(value).toString()
	}

	override fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		if (value.length <= 1) {
			fromSqlNull(model, property)
			return
		}
		val yar = YsonArray(value)
		val pt = property.firstGenericType
		val t = map[pt]
		if (t == null) {
			fromSqlNull(model, property)
			return
		}
		val v = when (pt) {
			String::class -> Yson.toModelGeneric<ArrayList<String>>(yar, t)
			java.lang.Character::class -> Yson.toModelGeneric<ArrayList<Char>>(yar, t)
			java.lang.Boolean::class -> Yson.toModelGeneric<ArrayList<Boolean>>(yar, t)
			java.lang.Byte::class -> Yson.toModelGeneric<ArrayList<Byte>>(yar, t)
			java.lang.Short::class -> Yson.toModelGeneric<ArrayList<Short>>(yar, t)
			java.lang.Integer::class -> Yson.toModelGeneric<ArrayList<Int>>(yar, t)
			java.lang.Long::class -> Yson.toModelGeneric<ArrayList<Long>>(yar, t)
			java.lang.Float::class -> Yson.toModelGeneric<ArrayList<Float>>(yar, t)
			java.lang.Double::class -> Yson.toModelGeneric<ArrayList<Double>>(yar, t)
			else -> null
		}
		if (v == null) {
			fromSqlNull(model, property)
		} else {
			property.setter.call(model, v)
		}


	}

	companion object {
		val map: HashMap<KClass<*>, KType> = hashMapOf(
				String::class to object : TypeTake<ArrayList<String>>() {}.type,
				java.lang.Character::class to object : TypeTake<ArrayList<Char>>() {}.type,
				java.lang.Boolean::class to object : TypeTake<ArrayList<Boolean>>() {}.type,
				java.lang.Byte::class to object : TypeTake<ArrayList<Byte>>() {}.type,
				java.lang.Short::class to object : TypeTake<ArrayList<Short>>() {}.type,
				java.lang.Integer::class to object : TypeTake<ArrayList<Int>>() {}.type,
				java.lang.Long::class to object : TypeTake<ArrayList<Long>>() {}.type,
				java.lang.Float::class to object : TypeTake<ArrayList<Float>>() {}.type,
				java.lang.Double::class to object : TypeTake<ArrayList<Double>>() {}.type

		)
	}

}
package net.yet.sqlite.convert

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.yet.json.GSON
import net.yet.orm.SqliteType
import net.yet.sqlite.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */

abstract class DataConvert {

	abstract val sqlType: SqliteType

	open fun toSqlText(model: Any, property: KMutableProperty<*>): String? {
		throw RuntimeException("Not implement")
	}

	open fun toSqlInteger(model: Any, property: KMutableProperty<*>): Long? {
		throw RuntimeException("Not implement")
	}

	open fun toSqlReal(model: Any, property: KMutableProperty<*>): Double? {
		throw RuntimeException("Not implement")
	}

	open fun toSqlBlob(model: Any, property: KMutableProperty<*>): ByteArray? {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlNull(model: Any, property: KMutableProperty<*>) {
		if (property.returnType.isMarkedNullable) {
			property.setter.call(model, null)
		} else {
			//TODO keep it's value , no change
		}
	}

	open fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlInteger(model: Any, property: KMutableProperty<*>, value: Long) {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlReal(model: Any, property: KMutableProperty<*>, value: Double) {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlBlob(model: Any, property: KMutableProperty<*>, value: ByteArray) {
		throw RuntimeException("Not implement")
	}

}

open class TextDataConvert : DataConvert() {
	final override val sqlType: SqliteType = SqliteType.TEXT

	fun accessModelType(t: KType): Boolean {
		return t.isString || t.isChar || t.isKClass(JSONObject::class) || t.isKClass(JSONArray::class) || t.isKClass(JsonObject::class) || t.isKClass(JsonArray::class)
	}

	override fun toSqlText(model: Any, property: KMutableProperty<*>): String? {
		val v = property.getter.call(model)
		return v?.toString()
	}

	override fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		val t = property.returnType
		if (t.isString) {
			property.setter.call(model, value)
		} else if (t.isChar) {
			val ch = value.firstOrNull()
			if (ch != null) {
				property.setter.call(model, ch)
			} else {
				fromSqlNull(model, property)
			}
		} else if (t.isKClass(JSONObject::class)) {
			val jo = JSONObject(value)
			property.setter.call(model, jo)
		} else if (t.isKClass(JSONArray::class)) {
			val ja = JSONArray(value)
			property.setter.call(model, ja)
		} else if (t.isKClass(JsonObject::class)) {
			val jo = GSON.parseObject(value)
			property.setter.call(model, jo)
		} else if (t.isKClass(JSONArray::class)) {
			val ja = GSON.parseArray(value)
			property.setter.call(model, ja)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from String:$value")
		}
	}

}

open class IntegerDataConvert : DataConvert() {
	final override val sqlType: SqliteType = SqliteType.INTEGER

	fun accessModelType(t: KType): Boolean {
		return t.isByte || t.isShort || t.isInt || t.isLong || t.isBoolean || t.isKClass(Date::class) || t.isKClass(java.sql.Date::class)
	}

	override fun toSqlInteger(model: Any, property: KMutableProperty<*>): Long? {
		val v = property.getter.call(model)
		if (v == null) {
			return null
		} else if (v is Boolean) {
			return if (v) 1L else 0L
		} else if (v is Number) {
			return v.toLong()
		} else if (v is Date) {
			return v.time
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} (value=$v) cannot convert to Long")
		}
	}

	override fun fromSqlInteger(model: Any, property: KMutableProperty<*>, value: Long) {
		val t = property.returnType
		if (t.isBoolean) {
			property.setter.call(model, value != 0L)
		} else if (t.isByte) {
			property.setter.call(model, value.toByte())
		} else if (t.isShort) {
			property.setter.call(model, value.toShort())
		} else if (t.isInt) {
			property.setter.call(model, value.toInt())
		} else if (t.isLong) {
			property.setter.call(model, value)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from Long:$value")
		}
	}
}

open class RealDataConvert : DataConvert() {
	final override val sqlType: SqliteType = SqliteType.REAL

	fun accessModelType(t: KType): Boolean {
		return t.isFloat || t.isDouble
	}

	override fun toSqlReal(model: Any, property: KMutableProperty<*>): Double? {
		val v = property.getter.call(model)
		if (v == null) {
			return null
		} else if (v is Number) {
			return v.toDouble()
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} (value=$v) cannot convert to Double")
		}
	}

	override fun fromSqlReal(model: Any, property: KMutableProperty<*>, value: Double) {
		val t = property.returnType
		if (t.isFloat) {
			property.setter.call(model, value.toFloat())
		} else if (t.isDouble) {
			property.setter.call(model, value.toDouble())
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from Double:$value")
		}
	}
}

open class BlobDataConvert : DataConvert() {
	final override val sqlType: SqliteType = SqliteType.BLOB

	fun accessModelType(t: KType): Boolean {
		return t.isByteArray
	}

	override fun toSqlBlob(model: Any, property: KMutableProperty<*>): ByteArray? {
		val v = property.getter.call(model)
		if (v == null) {
			return null
		} else if (v is ByteArray) {
			return v
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} (value=$v) cannot convert to ByteArray")
		}
	}

	override fun fromSqlBlob(model: Any, property: KMutableProperty<*>, value: ByteArray) {
		if (property.returnType.isByteArray) {
			property.setter.call(model, value)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from ByteArray:$value")
		}
	}
}
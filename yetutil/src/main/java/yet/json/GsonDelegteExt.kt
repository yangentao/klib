package yet.json

import com.google.gson.*
import yet.ext.*
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2017-04-12.
 */

operator fun <V> JsonObject.setValue(thisRef: Any?, property: KProperty<*>, value: V) {
	this.putAny(property.customName, value)
}

@Suppress("UNCHECKED_CAST")
operator fun <V> JsonObject.getValue(thisRef: Any?, property: KProperty<*>): V {
	val v = get(property.customName)
	if (v == null || v.isJsonNull) {
		val strDef = property.defaultValue
		if (strDef != null) {
			return strToV(strDef, property)
		}
		val t = property.returnType
		if (t.isMarkedNullable) {
			return null as V
		} else {
			if (t.isTypeString) {
				return "" as V
			}
			if (t.isTypeInt) {
				return 0 as V
			}
			if (t.isTypeLong) {
				return 0L as V
			}
			if (t.isTypeBoolean) {
				return false as V
			}
			if (t.isTypeByte) {
				return 0.toByte() as V
			}
			if (t.isTypeShort) {
				return 0.toShort() as V
			}
			if (t.isTypeFloat) {
				return 0.0f as V
			}
			if (t.isTypeDouble) {
				return 0.0 as V
			}
			if (t.isClass(JsonObject::class)) {
				return JsonObject() as V
			}
			if (t.isClass(JsonArray::class)) {
				return JsonArray() as V
			}
			throw IllegalArgumentException("不支持的类型${property.customNamePrefixClass}")
		}
	}
	if (v is JsonObject) {
		if (property.isTypeString) {
			return v.toString() as V
		}
		if (property.isClass(JsonObject::class)) {
			return v as V
		}
	}
	if (v is JsonArray) {
		if (property.isTypeString) {
			return v.toString() as V
		}
		if (property.isClass(JsonArray::class)) {
			return v as V
		}
	}
	if (v is JsonPrimitive) {
		if (v.isBoolean) {
			if (property.isTypeBoolean) {
				return v.asBoolean as V
			}
			if (property.isTypeInt) {
				val n = if (v.asBoolean) 1 else 0
				return n as V
			}
			if (property.isTypeLong) {
				val n = if (v.asBoolean) 1L else 0L
				return n as V
			}
		}
		if (v.isNumber) {
			val num: Number = v.asNumber
			if (property.isTypeByte) {
				return num.toByte() as V
			}
			if (property.isTypeShort) {
				return num.toShort() as V
			}
			if (property.isTypeInt) {
				return num.toInt() as V
			}
			if (property.isTypeLong) {
				return num.toLong() as V
			}
			if (property.isTypeFloat) {
				return num.toFloat() as V
			}
			if (property.isTypeDouble) {
				return num.toDouble() as V
			}
			if (property.isTypeBoolean) {
				val b = num.toInt() != 0
				return b as V
			}
			return strToV(v.asNumber.toString(), property)
		}
		if (v.isString) {
			return strToV(v.asString, property)
		}
	}
	throw IllegalArgumentException("不支持的类型${property.customNamePrefixClass}")
}
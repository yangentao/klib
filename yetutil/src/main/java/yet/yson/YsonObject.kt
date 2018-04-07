package yet.yson

import yet.anno.defaultValue
import yet.anno.nameProp
import yet.ext.defaultValueOfProperty
import yet.ext.strToV
import kotlin.reflect.KProperty

class YsonObject(val data: LinkedHashMap<String, YsonValue> = LinkedHashMap(32)) : YsonValue(), MutableMap<String, YsonValue> by data {

	constructor(capcity: Int) : this(LinkedHashMap<String, YsonValue>(capcity))

	constructor(json: String) : this() {
		val p = YsonParser(json)
		val v = p.parse(true)
		if (v is YsonObject) {
			data.putAll(v.data)
		}
	}

	override fun yson(buf: StringBuilder) {
		buf.append("{")
		var first = true
		for ((k, v) in data) {
			if (!first) {
				buf.append(",")
			}
			first = false
			buf.append("\"").append(escapeJson(k)).append("\":")
			v.yson(buf)
		}
		buf.append("}")
	}

	override fun preferBufferSize(): Int {
		return 256
	}

	override fun toString(): String {
		return yson()
	}

	operator fun <V> setValue(thisRef: Any?, property: KProperty<*>, value: V) {
		this.put(property.nameProp, Yson.toYson(value))
	}

	@Suppress("UNCHECKED_CAST")
	operator fun <V> getValue(thisRef: Any?, property: KProperty<*>): V {
		val retType = property.returnType
		val v = this.getOrDefault(property.nameProp, YsonNull.inst)
		if (v !is YsonNull) {
			val pv = YsonDecoder.decodeByType(v, retType, null)
			return pv as V
		}
		if (retType.isMarkedNullable) {
			return null as V
		}
		val defVal = property.defaultValue
		if (defVal != null) {
			return strToV(defVal, property)
		}
		return defaultValueOfProperty(property)
	}

	fun str(key: String, value: String?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, YsonString(value))
		}
	}

	fun str(key: String): String? {
		val a = get(key) as? YsonString
		return a?.data
	}

	fun int(key: String, value: Int?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, YsonInt(value))
		}
	}

	fun int(key: String): Int? {
		val a = get(key) as? YsonInt
		return a?.data?.toInt()
	}

	fun long(key: String, value: Long?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, YsonInt(value))
		}
	}

	fun long(key: String): Long? {
		val a = get(key) as? YsonInt
		return a?.data
	}

	fun real(key: String, value: Double?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, YsonReal(value))
		}
	}

	fun real(key: String): Double? {
		val a = get(key) as? YsonReal
		return a?.data
	}

	fun bool(key: String, value: Boolean?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, YsonBool(value))
		}
	}

	fun bool(key: String): Boolean? {
		val a = get(key) as? YsonBool
		return a?.data
	}

	fun obj(key: String, value: YsonObject?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, value)
		}
	}

	fun obj(key: String): YsonObject? {
		return get(key) as? YsonObject
	}

	fun arr(key: String, value: YsonArray?) {
		if (value == null) {
			put(key, YsonNull.inst)
		} else {
			put(key, value)
		}
	}

	fun arr(key: String): YsonArray? {
		return get(key) as? YsonArray
	}


	fun any(key: String, value: Any?) {
		put(key, from(value))
	}

	fun any(key: String): Any? {
		return get(key)
	}
}
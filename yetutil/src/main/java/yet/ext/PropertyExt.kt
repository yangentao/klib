package yet.ext

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import yet.anno.*
import yet.json.GSON
import yet.util.MyDate
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-13.
 */

/*
 * class A{
 *      val set:HashSet<String> = HashSet()
 * }
 *
 * A::set.firstGenericType => String::class
 *
 */
val KProperty<*>.firstGenericType: KClass<*>? get() = this.returnType.arguments.firstOrNull()?.type?.classifier as? KClass<*>


val KProperty<*>.isTypeString: Boolean get() = this.returnType.isTypeString
val KProperty<*>.isTypeInt: Boolean get() = this.returnType.isTypeInt
val KProperty<*>.isTypeLong: Boolean get() = this.returnType.isTypeLong
val KProperty<*>.isTypeByte: Boolean get() = this.returnType.isTypeByte
val KProperty<*>.isTypeShort: Boolean get() = this.returnType.isTypeShort
val KProperty<*>.isTypeChar: Boolean get() = this.returnType.isTypeChar
val KProperty<*>.isTypeBoolean: Boolean get() = this.returnType.isTypeBoolean
val KProperty<*>.isTypeFloat: Boolean get() = this.returnType.isTypeFloat
val KProperty<*>.isTypeDouble: Boolean get() = this.returnType.isTypeDouble
val KProperty<*>.isTypeByteArray: Boolean get() = this.returnType.isTypeByteArray
val KProperty<*>.isTypeHashSet: Boolean get() = this.returnType.isClass(HashSet::class)
val KProperty<*>.isTypeArrayList: Boolean get() = this.returnType.isClass(ArrayList::class)

fun KProperty<*>.isClass(cls: KClass<*>): Boolean {
	return this.returnType.isClass(cls)
}

val KProperty<*>.isTypeEnum: Boolean get() {
	return this.javaField?.type?.isEnum ?: false
}

fun KProperty<*>.getValue(): Any? {
	if (this.getter.parameters.isEmpty()) {
		return this.getter.call()
	}
	return null
}

fun KProperty<*>.getValue(inst: Any): Any? {
	if (this.getter.parameters.isEmpty()) {
		return this.getter.call()
	}
	return this.getter.call(inst)
}
fun KProperty<*>.getBindValue(): Any? {
	if (this.getter.parameters.isEmpty()) {
		return this.getter.call()
	}
	return null
}
fun KMutableProperty<*>.setValue(inst: Any, value: Any?) {
	this.setter.call(inst, value)
}

val KProperty<*>.isPublic: Boolean get() = this.visibility == KVisibility.PUBLIC


val KProperty<*>.customNamePrefixClass: String get() {
	var tabName = this.javaField?.declaringClass?.kotlin?.customName
	return tabName!! + "." + this.customName
}

val KProperty<*>.customName: String get() {
	return this.findAnnotation<Name>()?.value ?: this.name
}


val KProperty<*>.isExcluded: Boolean get() {
	return this.hasAnnotation<Exclude>()
}
val KMutableProperty<*>.isPrimaryKey: Boolean get() {
	return this.hasAnnotation<PrimaryKey>()
}

val KProperty<*>.defaultValue: String? get() {
	return this.findAnnotation<DefaultValue>()?.value
}

val KProperty<*>.labelValue: String? get() {
	return this.findAnnotation<Label>()?.value
}
val KProperty<*>.labelValue_: String get() {
	return this.labelValue!!
}





@Suppress("UNCHECKED_CAST")
fun <V> strToV(v: String, property: KProperty<*>): V {
	val retType = property.returnType
	if (retType.isTypeString) {
		return v as V
	}
	if (retType.isTypeBoolean) {
		return v.toBoolean() as V
	}

	if (retType.isTypeInt) {
		return v.toIntOrNull() as V
	}
	if (retType.isTypeLong) {
		return v.toLongOrNull() as V
	}
	if (retType.isTypeDouble) {
		return v.toDoubleOrNull() as V
	}
	if (retType.isTypeFloat) {
		return v.toFloatOrNull() as V
	}
	if (retType.isTypeShort) {
		return v.toShortOrNull() as V
	}
	if (retType.isTypeByte) {
		return v.toByteOrNull() as V
	}
	if (retType.isClass(JsonObject::class)) {
		return GSON.parseObject(v) as V
	}
	if (retType.isClass(JsonArray::class)) {
		return GSON.parseArray(v) as V
	}
	if (retType.isClass(java.sql.Date::class)) {
		val md = MyDate.parse(MyDate.FORMAT_DATE, v)
		if (md != null) {
			return java.sql.Date(md.time) as V
		} else {
			return null as V
		}
	}
	if (retType.isClass(java.util.Date::class)) {
		val md = MyDate.parse(MyDate.FORMAT_DATE, v)
		if (md != null) {
			return java.util.Date(md.time) as V
		} else {
			return null as V
		}
	}
	throw IllegalArgumentException("不支持的类型${property.customNamePrefixClass}")
}

@Suppress("UNCHECKED_CAST")
fun <V> defaultValueOfProperty(p: KProperty<*>): V {
	val retType = p.returnType
	if (retType.isTypeBoolean) {
		return false as V
	}
	if (retType.isTypeString) {
		return "" as V
	}
	if (retType.isTypeInt) {
		return 0 as V
	}
	if (retType.isTypeLong) {
		return 0L as V
	}
	if (retType.isTypeFloat) {
		return 0.0f as V
	}
	if (retType.isTypeDouble) {
		return 0.0 as V
	}

	if (retType.isTypeShort) {
		return 0.toShort() as V
	}
	if (retType.isTypeByte) {
		return 0.toByte() as V
	}

	if (retType.isClass(JsonObject::class)) {
		return JsonObject() as V
	}
	if (retType.isClass(JsonArray::class)) {
		return JsonArray() as V
	}
	if (retType.isClass(java.sql.Date::class)) {
		return java.sql.Date(0L) as V
	}
	if (retType.isClass(java.sql.Time::class)) {
		return java.sql.Time(0L) as V
	}
	throw IllegalArgumentException("不支持的类型modelMap: ${p.customNamePrefixClass}")
}
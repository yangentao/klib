package yet.anno

import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-04-06.
 */


val KClass<*>.nameClass: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.simpleName!!
	}

val KClass<*>.nameClassSQL: String
	get() {
		return "`" + this.nameClass + "`"
	}

val KClass<*>.labelClass: String
	get() {
		return this.findAnnotation<Label>()?.value ?: this.simpleName!!
	}


val KProperty<*>.fullNameProp: String
	get() {
		var tabName = this.javaField?.declaringClass?.kotlin?.nameClass
		val fname = this.findAnnotation<Name>()?.value ?: this.name
		return tabName!! + "." + fname
	}
val KProperty<*>.fullNamePropSQL: String
	get() {
		var tabName = this.javaField?.declaringClass?.kotlin?.nameClass
		val fname = this.findAnnotation<Name>()?.value ?: this.name
		return "`" + tabName!! + "`.`" + fname + "`"
	}

val KProperty<*>.nameProp: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name
	}

val KParameter.nameParam: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name ?: throw IllegalStateException("参数没有名字")
	}

val KFunction<*>.nameFun: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name
	}

val KProperty<*>.isExcluded: Boolean
	get() {
		return this.findAnnotation<Exclude>() != null
	}
val KProperty<*>.isPrimaryKey: Boolean
	get() {
		return this.findAnnotation<PrimaryKey>() != null
	}

val KProperty<*>.defaultValue: String?
	get() {
		return this.findAnnotation<DefaultValue>()?.value
	}

val KProperty<*>.labelProp: String?
	get() {
		return this.findAnnotation<Label>()?.value
	}
val KProperty<*>.labelProp_: String
	get() {
		return this.findAnnotation<Label>()?.value ?: this.nameProp
	}


inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation(): Boolean = null != this.findAnnotation<T>()

val KFunction<*>.labelFun: String?
	get() {
		return this.findAnnotation<Label>()?.value
	}
val KFunction<*>.labelFun_: String
	get() {
		return this.findAnnotation<Label>()?.value ?: this.nameFun
	}
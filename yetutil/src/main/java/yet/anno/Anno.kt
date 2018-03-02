package yet.anno

import yet.orm.convert.DataConvert
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Label(val value: String)

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultValue(val value: String)

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Name(val value: String, val forDB: String = "", val forJson: String = "")

//字段长度--字符串
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Length(val value: Int)

//是否非空
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotNull

//是否唯一约束
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Unique(val name: String = "")


//是否在该列建索引
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Index(val name: String = "")

//主键, 如果用于多列,则会生成联合主键, 联合主键忽略自增注释
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PrimaryKey

//自增, 仅用于整形主键
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoInc


@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Exclude(val value: ExcludeFor = ExcludeFor.ALL)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoAlterTable(val value: Boolean = true)

//是否唯一约束--联合--用于类
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Uniques(vararg val value: String)

enum class ExcludeFor {
	ALL, SQL, JSON
}


@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Convert(val value: KClass<out DataConvert>)



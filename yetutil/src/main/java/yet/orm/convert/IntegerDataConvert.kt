package yet.orm.convert

import yet.database.SQLType
import yet.ext.*
import yet.orm.TypeDismatchException
import yet.ref.*
import java.util.*
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */


open class IntegerDataConvert : DataConvert() {
	final override val sqlType: SQLType = SQLType.INTEGER

	override fun acceptProperty(p: KMutableProperty<*>): Boolean {
		return p.isTypeByte || p.isTypeShort || p.isTypeInt || p.isTypeLong || p.isTypeBoolean || p.isClass(Date::class) || p.isClass(java.sql.Date::class)
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
		if (property.isTypeBoolean) {
			property.setter.call(model, value != 0L)
		} else if (property.isTypeByte) {
			property.setter.call(model, value.toByte())
		} else if (property.isTypeShort) {
			property.setter.call(model, value.toShort())
		} else if (property.isTypeInt) {
			property.setter.call(model, value.toInt())
		} else if (property.isTypeLong) {
			property.setter.call(model, value)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from Long:$value")
		}
	}
}
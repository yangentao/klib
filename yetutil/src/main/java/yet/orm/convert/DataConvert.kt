package yet.orm.convert

import yet.database.SQLType
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */

abstract class DataConvert {

	abstract val sqlType: SQLType

	open fun acceptProperty(p: KMutableProperty<*>): Boolean {
		return true
	}

	open fun toSqlText(model: Any, property: KMutableProperty<*>): String? {
		val v = property.getter.call(model) ?: return null
		return toSqlText2(property, v)
	}

	open fun toSqlText2(property: KMutableProperty<*>, value: Any): String? {
		throw RuntimeException("Not implement")
	}

	open fun toSqlInteger(model: Any, property: KMutableProperty<*>): Long? {
		val v = property.getter.call(model) ?: return null
		return toSqlInteger2(property, v)
	}

	open fun toSqlInteger2(property: KMutableProperty<*>, value: Any): Long? {
		throw RuntimeException("Not implement")
	}

	open fun toSqlReal(model: Any, property: KMutableProperty<*>): Double? {
		val v = property.getter.call(model) ?: return null
		return toSqlReal2(property, v)
	}

	open fun toSqlReal2(property: KMutableProperty<*>, value: Any): Double? {
		throw RuntimeException("Not implement")
	}

	open fun toSqlBlob(model: Any, property: KMutableProperty<*>): ByteArray? {
		val v = property.getter.call(model) ?: return null
		return toSqlBlob2(property, v)
	}

	open fun toSqlBlob2(property: KMutableProperty<*>, value: Any): ByteArray? {
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
		val a = fromSqlText2(property, value)
		if (a == null) {
			fromSqlNull(model, property)
		} else {
			property.setter.call(model, a)
		}
	}

	open fun fromSqlText2(property: KMutableProperty<*>, value: String): Any? {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlInteger(model: Any, property: KMutableProperty<*>, value: Long) {
		val a = fromSqlInteger2(property, value)
		if (a == null) {
			fromSqlNull(model, property)
		} else {
			property.setter.call(model, a)
		}
	}

	open fun fromSqlInteger2(property: KMutableProperty<*>, value: Long): Long? {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlReal(model: Any, property: KMutableProperty<*>, value: Double) {
		val a = fromSqlReal2(property, value)
		if (a == null) {
			fromSqlNull(model, property)
		} else {
			property.setter.call(model, a)
		}
	}

	open fun fromSqlReal2(property: KMutableProperty<*>, value: Double): Any? {
		throw RuntimeException("Not implement")
	}

	open fun fromSqlBlob(model: Any, property: KMutableProperty<*>, value: ByteArray) {
		val a = fromSqlBlob2(property, value)
		if (a == null) {
			fromSqlNull(model, property)
		} else {
			property.setter.call(model, a)
		}
	}

	open fun fromSqlBlob2(property: KMutableProperty<*>, value: ByteArray): Any? {
		throw RuntimeException("Not implement")
	}

}


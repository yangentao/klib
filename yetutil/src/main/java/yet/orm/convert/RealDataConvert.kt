package yet.orm.convert

import yet.database.SQLType
import yet.ext.isTypeDouble
import yet.ext.isTypeFloat
import yet.orm.TypeDismatchException
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */

open class RealDataConvert : DataConvert() {
	final override val sqlType: SQLType = SQLType.REAL

	override fun acceptProperty(p: KMutableProperty<*>): Boolean {
		return return p.isTypeFloat || p.isTypeDouble
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
		if (property.isTypeFloat) {
			property.setter.call(model, value.toFloat())
		} else if (property.isTypeDouble) {
			property.setter.call(model, value)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from Double:$value")
		}
	}
}

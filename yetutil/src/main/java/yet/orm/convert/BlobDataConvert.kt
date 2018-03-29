package yet.orm.convert

import yet.database.SQLType
import yet.ext.isTypeByteArray
import yet.orm.TypeDismatchException
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */

open class BlobDataConvert : DataConvert() {
	final override val sqlType: SQLType = SQLType.BLOB

	override fun acceptProperty(p: KMutableProperty<*>): Boolean {
		return p.isTypeByteArray
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
		if (property.isTypeByteArray) {
			property.setter.call(model, value)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from ByteArray:$value")
		}
	}
}
package yet.orm.convert

import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import yet.database.SQLType
import yet.ext.isClass
import yet.ext.isTypeChar
import yet.ext.isTypeEnum
import yet.ext.isTypeString
import yet.orm.TypeDismatchException
import yet.ref.EnumUtil
import yet.yson.YsonArray
import yet.yson.YsonObject
import java.io.File
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-11.
 */


open class TextDataConvert : DataConvert() {
	final override val sqlType: SQLType = SQLType.TEXT

	override fun acceptProperty(p: KMutableProperty<*>): Boolean {
		return p.isTypeString ||
				p.isTypeChar ||
				p.isClass(JSONObject::class) ||
				p.isClass(JSONArray::class) ||
				p.isClass(YsonObject::class) ||
				p.isClass(YsonArray::class) ||
				p.isClass(Uri::class) ||
				p.isClass(File::class) ||
				p.isClass(UUID::class) ||
				p.isTypeEnum
	}

	override fun toSqlText(model: Any, property: KMutableProperty<*>): String? {
		val v = property.getter.call(model)
		return v?.toString()
	}

	override fun fromSqlText(model: Any, property: KMutableProperty<*>, value: String) {
		val p = property
		val v: Any? = if (p.isTypeString) {
			value
		} else if (p.isTypeChar) {
			value.firstOrNull()
		} else if (p.isClass(JSONObject::class)) {
			JSONObject(value)
		} else if (p.isClass(JSONArray::class)) {
			JSONArray(value)
		} else if (p.isClass(YsonObject::class)) {
			YsonObject(value)
		} else if (p.isClass(YsonArray::class)) {
			YsonArray(value)
		} else if (p.isClass(Uri::class)) {
			Uri.parse(value)
		} else if (p.isClass(File::class)) {
			File(value)
		} else if (p.isClass(UUID::class)) {
			UUID.fromString(value)
		} else if (p.isTypeEnum) {
			EnumUtil.valueOf(property.javaField!!.type, value)
		} else {
			throw TypeDismatchException("Property ${model.javaClass.simpleName}.${property.name} cannot assign from String:$value")
		}
		if (v != null) {
			property.setter.call(model, v)
		} else {
			fromSqlNull(model, property)
		}
	}

}

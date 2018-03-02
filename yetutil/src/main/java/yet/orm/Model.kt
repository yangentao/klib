package yet.orm

import com.google.gson.JsonObject
import yet.anno.Exclude
import yet.ext.*
import yet.json.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */


abstract class Model {

	fun toJson(vararg ps: KProperty<*>): JsonObject {
		val jo = JsonObject()
		if (ps.isEmpty()) {
			for (p in _columnProperties) {
				jo.putAny(p.customName, p.getter.call(this))
			}
		} else {
			for (p in ps) {
				jo.putAny(p.customName, p.getter.call(this))
			}
		}
		return jo
	}

	fun fromJson(jo: JsonObject, vararg ps: KMutableProperty<*>) {
		if (ps.isNotEmpty()) {
			for (p in ps) {
				val name = p.customName
				val value = jo.getAny(name)
				if (value != null) {
					if (p.isTypeInt) {
						p.setValue(this, jo.optInt(name, 0))
					} else if (p.isTypeLong) {
						p.setValue(this, jo.optLong(name, 0L))
					} else if (p.isTypeFloat) {
						p.setValue(this, jo.optDouble(name, 0.0).toFloat())
					} else if (p.isTypeDouble) {
						p.setValue(this, jo.optDouble(name, 0.0))
					} else if (p.isTypeString) {
						p.setValue(this, jo.optString(name, ""))
					} else if (p.isTypeBoolean) {
						p.setValue(this, jo.optBool(name, false))
					}
				}
			}
		} else {
			for (p in _columnProperties) {
				val name = p.customName
				val value = jo.getAny(name)
				if (value != null) {
					if (p.isTypeInt) {
						p.setValue(this, jo.optInt(name, 0))
					} else if (p.isTypeLong) {
						p.setValue(this, jo.optLong(name, 0L))
					} else if (p.isTypeFloat) {
						p.setValue(this, jo.optDouble(name, 0.0).toFloat())
					} else if (p.isTypeDouble) {
						p.setValue(this, jo.optDouble(name, 0.0))
					} else if (p.isTypeString) {
						p.setValue(this, jo.optString(name, ""))
					} else if (p.isTypeBoolean) {
						p.setValue(this, jo.optBool(name, false))
					}
				}
			}
		}
	}


	open fun save(): Boolean {
		return Session.peek.save(this) > 0
	}

	open fun delete(): Int {
		return Session.peek.deleteByPK(this)
	}

	override fun toString(): String {
		return toJson().toString()
	}

	@Exclude
	val _primaryKeyColumn: KMutableProperty<*>? by lazy {
		findModelPrimaryKeyProperty(this::class)
	}

	@Exclude
	val _columnProperties: List<KMutableProperty<*>> by lazy {
		findModelProperties(this::class)
	}
}
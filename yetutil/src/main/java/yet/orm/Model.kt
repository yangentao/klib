package yet.orm

import android.support.annotation.Keep
import yet.anno.Exclude
import yet.ext.*
import yet.yson.YsonObject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2017-03-08.
 */


@Keep
abstract class Model {

	fun toJson(vararg ps: KProperty<*>): YsonObject {
		val jo = YsonObject()
		if (ps.isEmpty()) {
			for (p in _columnProperties) {
				jo.any(p.customName, p.getter.call(this))
			}
		} else {
			for (p in ps) {
				jo.any(p.customName, p.getter.call(this))
			}
		}
		return jo
	}

	fun fromJson(jo: YsonObject, vararg ps: KMutableProperty<*>) {
		if (ps.isNotEmpty()) {
			for (p in ps) {
				val name = p.customName
				val value = jo.any(name)
				if (value != null) {
					if (p.isTypeInt) {
						p.setValue(this, jo.int(name) ?: 0)
					} else if (p.isTypeLong) {
						p.setValue(this, jo.long(name) ?: 0L)
					} else if (p.isTypeFloat) {
						p.setValue(this, (jo.real(name) ?: 0.0).toFloat())
					} else if (p.isTypeDouble) {
						p.setValue(this, jo.real(name) ?: 0.0)
					} else if (p.isTypeString) {
						p.setValue(this, jo.str(name) ?: "")
					} else if (p.isTypeBoolean) {
						p.setValue(this, jo.bool(name) ?: false)
					}
				}
			}
		} else {
			for (p in _columnProperties) {
				val name = p.customName
				val value = jo.any(name)
				if (value != null) {
					if (p.isTypeInt) {
						p.setValue(this, jo.int(name) ?: 0)
					} else if (p.isTypeLong) {
						p.setValue(this, jo.long(name) ?: 0L)
					} else if (p.isTypeFloat) {
						p.setValue(this, (jo.real(name) ?: 0.0).toFloat())
					} else if (p.isTypeDouble) {
						p.setValue(this, jo.real(name) ?: 0.0)
					} else if (p.isTypeString) {
						p.setValue(this, jo.str(name) ?: "")
					} else if (p.isTypeBoolean) {
						p.setValue(this, jo.bool(name) ?: false)
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
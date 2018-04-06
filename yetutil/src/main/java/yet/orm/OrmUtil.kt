package yet.orm

import yet.ext.isExcluded
import yet.ext.isPrimaryKey
import yet.ext.isPublic
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties

/**
 * Created by entaoyang@163.com on 2017-04-06.
 */


internal fun findModelProperties(cls: KClass<*>): List<KMutableProperty<*>> {
	return cls.declaredMemberProperties.filter {
		if (it !is KMutableProperty<*>) {
			false
		} else if (it.isAbstract || it.isConst || it.isLateinit) {
			false
		} else if (!it.isPublic) {
			false
		} else !it.isExcluded
	}.map { it as KMutableProperty<*> }
}

internal fun findModelPrimaryKeyProperty(cls:KClass<*>): KMutableProperty<*>? {
	return cls.declaredMemberProperties.find {
		it is KMutableProperty<*> && it.isPrimaryKey
	} as? KMutableProperty<*>
}
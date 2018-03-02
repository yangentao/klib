package yet.ext

import yet.anno.Name
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Created by entaoyang@163.com on 2017-04-16.
 */

val KClass<*>.customName: String get() {
	return this.findAnnotation<Name>()?.value ?: this.simpleName!!
}
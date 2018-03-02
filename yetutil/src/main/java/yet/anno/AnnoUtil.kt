package yet.anno

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

/**
 * Created by entaoyang@163.com on 2017-04-06.
 */

inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation(): Boolean {
	return this.findAnnotation<T>() != null
}

package yet.ui.widget.listview2

import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2017-03-14.
 */

class ModelTypes(vararg val modelTypes: KClass<*>) {

	val count: Int get() {
		return modelTypes.size
	}

	fun viewTypeOf(model: Any): Int {
		return modelTypes.indexOf(model::class)
	}
}
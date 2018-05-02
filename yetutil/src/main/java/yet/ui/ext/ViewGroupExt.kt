package yet.ui.ext

import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2018-04-16.
 */


fun <T : View> ViewGroup.findChild(cls: KClass<T>): T? {
	for (i in 0 until this.childCount) {
		val c = getChildAt(i)
		if (c::class == cls) {
			return c as T
		}
	}
	return null
}

val ViewGroup.childViews: List<View>
	get() {
		val ls = ArrayList<View>()
		for (i in 0 until this.childCount) {
			ls += this.getChildAt(i)
		}
		return ls
	}
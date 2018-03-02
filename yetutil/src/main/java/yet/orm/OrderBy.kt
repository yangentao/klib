package yet.orm

import yet.ext.customNamePrefixClass
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2017-04-06.
 */

class OrderBy {
	val orderArr = arrayListOf<String>()


	fun asc(p: KProperty<*>): OrderBy {
		orderArr.add(p.customNamePrefixClass + " ASC")
		return this
	}

	fun desc(p: KProperty<*>): OrderBy {
		orderArr.add(p.customNamePrefixClass + " DESC")
		return this
	}
}
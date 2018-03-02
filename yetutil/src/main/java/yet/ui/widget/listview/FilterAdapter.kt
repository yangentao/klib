package yet.ui.widget.listview

import yet.util.JsonUtil
import java.util.*

/**
 * Created by entaoyang@163.com on 2017-04-18.
 */


abstract class FilterAdapter<T> : XBaseAdapter<T>() {
	protected var backup: ArrayList<T>? = null
	protected val indexMap = HashMap<Int, Int>(256)
	val inFilter: Boolean get() = backup != null
	var accepter: ((item: T, searchText: String) -> Boolean)? = null

	fun filter(searchText: String?) {
		indexMap.clear()
		if (searchText == null || searchText.isEmpty()) {
			resetFilter()
			return
		}
		if (backup == null) {
			backup = ArrayList(all)
		}
		val ls = ArrayList<T>(backup!!.size)
		for (i in backup!!.indices) {
			val item = backup!![i]
			if (accept(item, searchText)) {
				indexMap.put(ls.size, i)
				ls.add(item)
			}
		}
		setItemsInternal(ls)
	}

	fun resetFilter() {
		if (backup != null) {
			setItemsInternal(backup!!)
			backup = null
		}
	}

	open fun accept(item: T, searchText: String): Boolean {
		if (accepter != null) {
			return accepter!!.invoke(item, searchText)
		}
		return JsonUtil.toJson(item).contains(searchText)
	}
}

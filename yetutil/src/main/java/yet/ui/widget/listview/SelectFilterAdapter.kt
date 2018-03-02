package yet.ui.widget.listview

import java.util.*

/**
 * Created by entaoyang@163.com on 2017-04-18.
 */


abstract class SelectFilterAdapter<T> : FilterAdapter<T>() {
	private val indexSet = TreeSet<Int>()

	override fun setItems(items: List<T>) {
		clearSelect()
		super.setItems(items)
	}

	fun selectAll() {
		indexSet.clear()
		val ls: List<T> = if (inFilter) backup!! else all
		indexSet += ls.indices
	}

	fun clearSelect() {
		indexSet.clear()
	}

	fun isSelected(position: Int): Boolean {
		val p = if (inFilter) indexMap[position] else position
		return indexSet.contains(p)
	}

	val isAllSelected: Boolean
		get() {
			if(count == 0) {
				return false
			}
			if (inFilter) {
				return selectedCount == backup!!.size
			}
			return selectedCount == count
		}

	val selectedCount: Int
		get() = indexSet.size

	val selectedIndexs: Set<Int>
		get() = TreeSet(indexSet)

	val selectedItems: ArrayList<T>
		get() {
			val ls = ArrayList<T>(indexSet.size + 10)
			if (inFilter) {
				for (n in indexSet) {
					ls.add(backup!![n])
				}
			} else {
				for (n in indexSet) {
					ls.add(getItem(n))
				}
			}
			return ls
		}

	fun selectPosition(position: Int, select: Boolean) {
		val p: Int = if (inFilter) indexMap[position]!! else position
		if (select) {
			indexSet.add(p)
		} else {
			indexSet.remove(p)
		}
	}

	fun selectItem(item: T) {
		if (inFilter) {
			val n = backup!!.indexOf(item)
			selectPosition(n, true)
		} else {
			val n = all.indexOf(item)
			selectPosition(n, true)
		}
	}
}

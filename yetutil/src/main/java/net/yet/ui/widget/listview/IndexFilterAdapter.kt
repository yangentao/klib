package net.yet.ui.widget.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import net.yet.util.back
import net.yet.util.debugMustInMainThread
import net.yet.util.fore
import java.util.*

abstract class IndexFilterAdapter<T> : BaseAdapter() {
	protected var items: ArrayList<T> = ArrayList()

	var filtedItems: ArrayList<T> = ArrayList()
	private var filterBlock: ((T) -> Boolean)? = null
	val inFilter: Boolean get() = filterBlock != null

	val posIndexMap = HashMap<Int, Int>(256)

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val type = getItemViewType(position)
		var view = convertView ?: newView(parent.context, position, parent, type)
		bindView(position, view, parent, getItem(position), type)
		return view
	}

	abstract fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int)

	abstract fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View

	fun indexOfPosition(position: Int): Int {
		if (inFilter) {
			return posIndexMap[position]!!
		} else {
			return position
		}
	}

	/**
	 * 会自动调用notifyDataSetChanged(), 因此,必须在主线程调用

	 * @param items
	 */
	fun setItems(items: List<T>) {
		debugMustInMainThread()
		this.items.clear()
		this.items.addAll(items)
		refilter()
	}

	fun setItemArray(vararg items: T) {
		setItems(listOf(*items))
	}

	fun setFilter(block: (T) -> Boolean) {
		debugMustInMainThread()
		filterBlock = block
		refilter()
	}

	fun refilter() {
		debugMustInMainThread()
		filtedItems.clear()
		posIndexMap.clear()
		val fb = filterBlock
		if (fb != null) {
			for (i in items.indices) {
				val item = items[i]
				if (fb(item)) {
					posIndexMap[filtedItems.size] = i
					filtedItems.add(item)
				}
			}
		}
		notifyDataSetChanged()
		onItemsRefreshed()
	}

	fun resetFilter() {
		debugMustInMainThread()
		filterBlock = null
		refilter()
	}


	override fun getCount(): Int {
		if (inFilter) {
			return filtedItems.size
		}
		return items.size
	}

	override fun getItem(position: Int): T {
		if (inFilter) {
			if (position in filtedItems.indices) {
				return filtedItems[position]
			}
		} else {
			if (position in items.indices) {
				return items[position]
			}
		}
		throw  IndexOutOfBoundsException("adapter position out of bound $position " + this.javaClass.name)
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	// 异步加载支持
	protected open fun onRequestItems(): List<T> {
		return ArrayList()
	}

	protected open fun onItemsRefreshed() {
	}

	// 请求数据集, 后台线程回调onRequestItems
	fun requestItems() {
		back {
			val data = onRequestItems()
			fore {
				setItems(data)
			}
		}
	}


}

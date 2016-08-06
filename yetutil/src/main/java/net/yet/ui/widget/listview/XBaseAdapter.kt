package net.yet.ui.widget.listview

import android.widget.BaseAdapter
import net.yet.util.back
import net.yet.util.debugMustInMainThread
import net.yet.util.fore
import java.util.*

abstract class XBaseAdapter<T> : BaseAdapter() {
	internal var items: ArrayList<T> = ArrayList()

	private var filtedItems: ArrayList<T> = ArrayList()
	private var filterBlock: ((T) -> Boolean)? = null
	val inFilter: Boolean get() = filterBlock != null

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
		if (filterBlock != null) {
			filtedItems.clear()
			this.items.filterTo(filtedItems, filterBlock!!)
		}
		if (inFilter) {
			val ls = onOrderItems(filtedItems)
			if (ls !== filtedItems) {
				filtedItems = ls
			}
		} else {
			val ls = onOrderItems(this.items)
			if (ls !== this.items) {
				this.items = ls
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

	open fun onOrderItems(items: ArrayList<T>): ArrayList<T> {
		return items
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

package net.yet.ui.widget.listview

import android.widget.BaseAdapter
import net.yet.util.back
import net.yet.util.fore
import java.util.*

abstract class XBaseAdapter<T> : BaseAdapter() {
	internal  var items: ArrayList<T> = ArrayList<T>()
	private var itemsBack: ArrayList<T> = ArrayList<T>()




	/**
	 * 会自动调用notifyDataSetChanged(), 因此,必须在主线程调用

	 * @param items
	 */
	fun setItems(items: List<T>) {
		this.items.clear()
		this.items.addAll(items)
		notifyDataSetChanged()
		onItemsRefreshed()
	}

	fun setItemArray(vararg items: T) {
		this.items.clear()
		this.items.addAll(items)
		notifyDataSetChanged()
		onItemsRefreshed()
	}


	override fun getCount(): Int {
		return items.size
	}

	override fun getItem(position: Int): T {
		if (position in items.indices) {
			return items[position]
		}
		throw  IndexOutOfBoundsException("adapter position out of bound $position " + this.javaClass.name)
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	// 异步加载支持
	protected open fun onRequestItems(): List<T> {
		return ArrayList<T>()
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

	fun filter(block: (T) -> Boolean) {
		if (itemsBack.isEmpty()) {
			itemsBack.addAll(items)
		}
		setItems(itemsBack.filter { block(it) })
	}

	fun resetFilter() {
		items.clear()
		items.addAll(itemsBack)
		itemsBack.clear()
	}

}

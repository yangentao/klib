package yet.ui.widget.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import yet.util.back
import yet.util.debugMustInMainThread
import yet.util.fore
import yet.util.mainThread
import java.util.*

abstract class XBaseAdapter<T> : BaseAdapter() {
	internal var all: ArrayList<T> = ArrayList()

	/**
	 * 会自动调用notifyDataSetChanged(), 因此,必须在主线程调用

	 * @param items
	 */
	open fun setItems(items: List<T>) {
		setItemsInternal(items)
	}

	protected fun setItemsInternal(items: List<T>) {
		mainThread {
			val orderItems = onOrderItems(items)
			this.all.clear()
			this.all.addAll(orderItems)
			notifyDataSetChanged()
			onItemsRefreshed()
		}
	}

	fun setItemArray(vararg items: T) {
		setItems(listOf(*items))
	}

	open fun onOrderItems(items: List<T>): List<T> {
		return items
	}

	open fun onItemsRefreshed() {

	}


	override fun getCount(): Int {
		return all.size
	}

	override fun getItem(position: Int): T {
		return all[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	// 异步加载支持
	protected open fun onRequestItems(): List<T> {
		return ArrayList()
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

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var v = convertView
		if (v== null) {
			v= newView(parent.context, position)
		}
		bindView(position, v, getItem(position))
		return v
	}

	abstract fun bindView(position: Int, itemView: View, item: T)

	abstract fun newView(context: Context, position: Int): View


}

package yet.ui.widget.listview

import android.widget.BaseAdapter
import yet.util.back
import yet.util.debugMustInMainThread
import yet.util.fore
import java.util.*

abstract class CommonAdapter : BaseAdapter() {
	internal var all: ArrayList<Any> = ArrayList()

	fun setItems(items: List<out Any>) {
		debugMustInMainThread()
		this.all.clear()
		this.all.addAll(items)
		notifyDataSetChanged()
		onItemsRefreshed()
	}


	override fun getCount(): Int {
		return all.size
	}

	override fun getItem(position: Int): Any {
		return all[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	// 异步加载支持
	protected open fun onRequestItems(): List<out Any> {
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

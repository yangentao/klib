package yet.ui.widget.listview2

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import yet.util.*

abstract class ArrayListAdapter : BaseAdapter() {
	var all: ArrayList<Any> = ArrayList()


	open fun setItems(items: List<out Any>) {
		mainThread {
			all.clear()
			all.addAll(items)
			notifyDataSetChanged()
			onItemsRefreshed()
		}
	}

	open fun onItemsRefreshed() {

	}


	override fun getCount(): Int {
		return all.size
	}

	override fun getItemViewType(position: Int): Int {
		return 0
	}

	override fun getViewTypeCount(): Int {
		return 1
	}

	override fun getItem(position: Int): Any {
		return all[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var v = convertView ?: newView(parent.context, position)
		bindView(v, position)
		return v
	}

	abstract fun bindView(itemView: View, position: Int)

	abstract fun newView(context: Context, position: Int): View


	// 请求数据集, 后台线程回调onRequestItems
	fun requestItems() {
		back {
			val data = onRequestItems()
			fore {
				setItems(data)
			}
		}
	}

	// 异步加载支持
	open fun onRequestItems(): List<out Any> {
		return ArrayList()
	}
}

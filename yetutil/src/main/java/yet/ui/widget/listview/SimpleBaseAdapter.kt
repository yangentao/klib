package yet.ui.widget.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import yet.util.mainThread

open class SimpleBaseAdapter<T> : BaseAdapter() {
	protected var all: ArrayList<T> = ArrayList()
	var inFilter: Boolean = false
		private set

	private var backup: ArrayList<T> = ArrayList()

	var onNewView: (context: Context, position: Int) -> View = { c, p -> TextView(c) }
	var onBindView: (itemView: View, item: T, position: Int) -> Unit = { v, item, p ->
		var tv = v as? TextView
		tv?.text = item.toString()
	}

	var typeCount: Int = 1
	var onViewType: (item: T, position: Int) -> Int = { item, p -> 0 }

	/**
	 * 会自动调用notifyDataSetChanged(), 因此,必须在主线程调用

	 * @param items
	 */
	open fun setItems(items: List<T>) {
		mainThread {
			this.all.clear()
			this.all.addAll(items)
			notifyDataSetChanged()
		}
	}

	fun filter(block: (T) -> Boolean) {
		if (!inFilter) {
			backup.clear()
			backup.addAll(all)
		}
		inFilter = true
		val ls = all.filter(block)
		setItems(ls)
	}

	fun clearFilter() {
		if (inFilter) {
			setItems(backup)
			backup.clear()
		}
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

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var v = convertView
		if (v == null) {
			v = onNewView(parent.context, position)
		}
		onBindView(v, getItem(position), position)
		return v
	}

	override fun getItemViewType(position: Int): Int {
		return onViewType(getItem(position), position)
	}

	override fun getViewTypeCount(): Int {
		return typeCount
	}


}

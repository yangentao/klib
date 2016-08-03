package net.yet.ui.widget.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup

abstract class TypedAdapter<T> : XBaseAdapter<T>() {

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var convertView = convertView
		val type = getItemViewType(position)
		if (convertView == null) {
			convertView = newView(parent.context, position, parent, type)
		}
		bindView(position, convertView, parent, getItem(position), type)
		return convertView
	}

	abstract fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int)

	abstract fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View

}

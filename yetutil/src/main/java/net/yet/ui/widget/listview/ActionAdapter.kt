package net.yet.ui.widget.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import net.yet.R
import net.yet.theme.Colors
import net.yet.ui.ext.backColor
import net.yet.ui.widget.recycler.ActionItemView

abstract class ActionAdapter<T> : XBaseAdapter<T>() {
	private val actionColor = Colors.TRANS
	private val actionPress = Colors.Fade

	private val clickListener = View.OnClickListener { v ->
		val position = v.getTag(R.id.yet_adapter_position) as Int
		val index = v.getTag(R.id.yet_adapter_action_index) as Int
		onItemAction(position, index, getItem(position))
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		var v = convertView
		val type = getItemViewType(position)
		if (v == null) {
			v = newView(parent.context, position, parent, type)
			if (v is ActionItemView) {
				for (i in 0..v.actionCount - 1) {
					val v = v.getActionView(i)
					v.backColor(actionColor, actionPress)
					v.setOnClickListener(clickListener)
				}
			}
		}
		bindView(position, v, parent, getItem(position), type)
		v.setTag(R.id.yet_adapter_position, position)
		if (v is ActionItemView) {
			for (i in 0..v.actionCount - 1) {
				val v = v.getActionView(i)
				v.setTag(R.id.yet_adapter_position, position)
				v.setTag(R.id.yet_adapter_action_index, i)
			}
		}
		return v
	}

	abstract fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int)

	abstract fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View

	protected open fun onItemAction(position: Int, actionIndex: Int, item: T) {

	}

}

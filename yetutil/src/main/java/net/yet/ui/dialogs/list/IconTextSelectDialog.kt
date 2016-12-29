package net.yet.ui.dialogs.list

import android.content.Context
import android.view.View

import net.yet.theme.IconSize
import net.yet.theme.TextSize
import net.yet.ui.widget.listview.itemview.IconTextView

abstract class IconTextSelectDialog<T> : ListSelectDialog<T, IconTextView>() {
	var iconSize = IconSize.Big
	var textSize = TextSize.Normal

	var hideIcon = false


	override fun onNewItemView(context: Context, item: T): IconTextView {
		val v = IconTextView(context, iconSize)
		v.setTextSize(textSize)
		if (hideIcon) {
			v.iconView.visibility = View.GONE
		}
		return v
	}


}

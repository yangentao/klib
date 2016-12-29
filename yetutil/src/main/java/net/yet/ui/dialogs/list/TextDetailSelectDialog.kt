package net.yet.ui.dialogs.list

import android.content.Context

import net.yet.ui.widget.listview.itemview.TextDetailView

abstract class TextDetailSelectDialog<T> : ListSelectDialog<T, TextDetailView>() {
	override fun onNewItemView(context: Context, item: T): TextDetailView {
		return TextDetailView(context)
	}

}

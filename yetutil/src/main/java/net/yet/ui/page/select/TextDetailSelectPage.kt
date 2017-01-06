package net.yet.ui.page.select

import android.content.Context

import net.yet.ui.widget.listview.itemview.TextDetailView

abstract class TextDetailSelectPage<T> : SelectPage<T, TextDetailView>() {
	override fun onNewItemView(context: Context, item: T): TextDetailView {
		return TextDetailView(context)
	}

}

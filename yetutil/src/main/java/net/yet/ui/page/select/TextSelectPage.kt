package net.yet.ui.page.select

import android.content.Context

import net.yet.ui.widget.listview.itemview.TextItemView

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class TextSelectPage<T> : SelectPage<T, TextItemView>() {
	override fun onNewItemView(context: Context, item: T): TextItemView {
		return TextItemView(context)
	}


}

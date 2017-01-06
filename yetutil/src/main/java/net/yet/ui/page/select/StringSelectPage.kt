package net.yet.ui.page.select

import net.yet.ui.widget.listview.itemview.TextItemView

/**
 * 简单字符串选择
 */
open class StringSelectPage : TextSelectPage<String>() {

	override fun onBindItemView(itemView: TextItemView, item: String) {
		itemView.text = item
	}
}

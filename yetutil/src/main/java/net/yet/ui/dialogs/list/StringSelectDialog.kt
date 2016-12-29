package net.yet.ui.dialogs.list

import net.yet.ui.widget.listview.itemview.TextItemView

/**
 * 简单字符串选择
 */
class StringSelectDialog : TextSelectDialog<String>() {
	override fun onBindItemView(itemView: TextItemView, item: String) {
		itemView.text = item
	}
}

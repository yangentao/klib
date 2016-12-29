package net.yet.ui.dialogs.list

import net.yet.ui.widget.listview.itemview.TextItemView

/**
 * 简单字符串选择
 */
class StringSelectDialog : TextSelectDialog<String>() {
	var onSelectIndex: (Int) -> Unit = {}
	var onSelectValue: (String) -> Unit = {}


	override fun onBindItemView(itemView: TextItemView, item: String) {
		itemView.text = item
	}

	override fun onSelect(position: Int, item: String) {
		onSelectIndex(position)
		onSelectValue(item)

	}
}

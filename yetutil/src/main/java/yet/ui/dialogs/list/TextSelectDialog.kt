package yet.ui.dialogs.list

import android.content.Context

import yet.ui.widget.listview.itemview.TextItemView

abstract class TextSelectDialog<T> : ListSelectDialog<T, TextItemView>() {

	override fun onNewItemView(context: Context, item: T): TextItemView {
		return TextItemView(context)
	}


}

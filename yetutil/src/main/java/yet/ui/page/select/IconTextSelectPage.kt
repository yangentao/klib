package yet.ui.page.select

import android.content.Context
import android.view.View

import yet.theme.IconSize
import yet.theme.TextSize
import yet.ui.widget.listview.itemview.IconTextView

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class IconTextSelectPage<T> : SelectPage<T, IconTextView>() {
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

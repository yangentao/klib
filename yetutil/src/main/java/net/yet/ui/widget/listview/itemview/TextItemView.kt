package net.yet.ui.widget.listview.itemview

import android.content.Context
import android.widget.TextView
import net.yet.theme.Space
import net.yet.ui.ext.gravityLeftCenter
import net.yet.ui.ext.padding
import net.yet.ui.ext.textColorMajor
import net.yet.ui.ext.textSizeA

/**
 * Created by entaoyang@163.com on 16/3/13.
 */
class TextItemView(context: Context) : TextView(context) {
	init {
		padding(Space.Normal, Space.Small, Space.Normal, Space.Small).gravityLeftCenter().textSizeA().textColorMajor()
	}
}

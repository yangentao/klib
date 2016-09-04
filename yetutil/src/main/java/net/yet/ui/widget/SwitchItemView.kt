package net.yet.ui.widget

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.theme.Colors
import net.yet.ui.ext.*

/**
 * Created by entaoyang@163.com on 2016-09-04.
 */

class SwitchItemView(context: Context) : LinearLayout(context) {
	var textView: TextView

	init {
		this.genId()
		this.orientationVertical().backColor(Colors.WHITE, Colors.Fade)
		textView = context.createTextViewB()
		textView.padding(20, 10, 20, 10).gravityCenter().backColor(Colors.WHITE)
		this.addViewParam(textView) { widthFill().heightWrap().margins(0, 0, 0, 3) }
	}

	var text: String
		get() {
			return textView.text.toString()
		}
		set(text) {
			textView.text(text)
		}

}
package net.yet.weatherapp.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import net.yet.theme.Colors
import net.yet.ui.ext.*

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */

open class GridItemView(context: Context) : TextView(context) {
	init {
		genId()
		gravityCenter()
		textColorMinor().textSizeB()
		this.backColor(Colors.TRANS, Colors.Fade)
	}

	fun setValues(title: String, image: Drawable) {
		this.text = title
		this.setCompoundDrawables(null, image, null, null)
	}
}
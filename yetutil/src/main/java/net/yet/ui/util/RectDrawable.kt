package net.yet.ui.util

import android.graphics.drawable.GradientDrawable
import net.yet.ui.ext.dp

/**
 * Created by entaoyang@163.com on 16/7/12.
 */


class RectDrawable(fillColor: Int? = null) {
	val drawable: GradientDrawable = GradientDrawable()

	val value: GradientDrawable
		get() = drawable

	init {
		drawable.setShape(GradientDrawable.RECTANGLE)
		if (fillColor != null) {
			drawable.setColor(fillColor)
		}
	}

	fun color(color: Int): RectDrawable {
		drawable.setColor(color)
		return this
	}

	fun corner(corner: Int): RectDrawable {
		drawable.setCornerRadius(dp(corner).toFloat())
		return this
	}

	fun corners(topLeft: Int, topRight: Int, bottomRight: Int, bottomLeft: Int): RectDrawable {
		val f1 = dp(topLeft).toFloat()
		val f2 = dp(topRight).toFloat()
		val f3 = dp(bottomRight).toFloat()
		val f4 = dp(bottomLeft).toFloat()

		val arr = floatArrayOf(f1, f1, f2, f2, f3, f3, f4, f4)
		drawable.setCornerRadii(arr)
		return this
	}

	fun stroke(width: Int, color: Int): RectDrawable {
		drawable.setStroke(dp(width), color)
		return this
	}

	fun size(w: Int, h: Int = w): RectDrawable {
		drawable.setSize(dp(w), dp(h))
		return this
	}
}
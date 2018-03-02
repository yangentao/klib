package yet.ui.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

import yet.util.Util
import yet.util.app.App

/**
 * TODO 阴影
 */
object ShapeUtil {
	fun line(strokeWidth: Int, strokeColor: Int): Drawable {
		return linePx(App.dp2px(strokeWidth), strokeColor)
	}

	fun linePx(strokeWidth: Int, strokeColor: Int): Drawable {
		val gd = GradientDrawable()
		gd.shape = GradientDrawable.LINE
		gd.setStroke(strokeWidth, strokeColor)
		return gd
	}

	fun round(corner: Int, fillColor: Int): Drawable {
		return round(corner, fillColor, null, null)
	}

	fun round(corner: Int?, fillColor: Int?, strokeWidth: Int?, strokeColor: Int?): Drawable {
		return roundPx(if (corner == null) null else App.dp2px(corner), fillColor,
				if (strokeWidth == null) null else App.dp2px(strokeWidth), strokeColor)
	}

	fun round(corner: Int?, fillColor: String?, strokeWidth: Int?, strokeColor: String?): Drawable {
		val fC = if (fillColor == null) null else Util.argb(fillColor)
		val sC = if (strokeColor == null) null else Util.argb(strokeColor)
		return roundPx(if (corner == null) null else App.dp2px(corner), fC,
				if (strokeWidth == null) null else App.dp2px(strokeWidth), sC)
	}

	fun roundPx(corner: Int?, fillColor: String?, strokeWidth: Int?, strokeColor: String?): Drawable {
		val fC = if (fillColor == null) null else Util.argb(fillColor)
		val sC = if (strokeColor == null) null else Util.argb(strokeColor)
		return roundPx(if (corner == null) null else corner, fC, if (strokeWidth == null) null else strokeWidth, sC)
	}

	fun roundPx(corner: Int, fillColor: Int): Drawable {
		return roundPx(corner, fillColor, null, null)
	}

	fun roundPx(corner: Int?, fillColor: Int?, strokeWidth: Int?, strokeColor: Int?): Drawable {
		val gd = GradientDrawable()
		if (corner != null) {
			gd.cornerRadius = corner.toFloat()
		}
		if (fillColor != null) {
			gd.setColor(fillColor)
		}
		if (strokeWidth != null && strokeColor != null) {
			gd.setStroke(strokeWidth, strokeColor)
		}
		return gd
	}

	fun rect(fillColor: Int?, strokeWidth: Int?, strokeColor: Int?): Drawable {
		return rectPx(fillColor, if (strokeWidth == null) null else App.dp2px(strokeWidth), strokeColor)
	}

	fun rectPx(fillColor: Int?, strokeWidth: Int?, strokeColor: Int?): Drawable {
		val gd = GradientDrawable()
		if (fillColor != null) {
			gd.setColor(fillColor)
		}
		if (strokeWidth != null && strokeColor != null) {
			gd.setStroke(strokeWidth, strokeColor)
		}
		return gd
	}

	fun oval(width: Int, color: Int): Drawable {
		var width = width
		width = App.dp2px(width)
		val d = ovalPx(width, color)
		d.setBounds(0, 0, width, width)//
		return d
	}

	private fun ovalPx(width: Int, color: Int): Drawable {
		return ovalPx(width, width, color)
	}

	private fun ovalPx(width: Int, height: Int, color: Int): Drawable {
		return ovalPx(width, height, color, null, null)
	}

	fun ovalPx(width: Int, height: Int, color: Int, strokeWidth: Int?, strokeColor: Int?): Drawable {
		val drawable = GradientDrawable()
		drawable.setColor(color)
		drawable.shape = GradientDrawable.OVAL
		if (strokeWidth != null) {
			drawable.setStroke(strokeWidth, strokeColor!!)
		}
		drawable.setSize(width, height)
		return drawable
	}

	fun ovalDP(width: Int, height: Int, color: Int, strokeWidth: Int?, strokeColor: Int?): Drawable {
		var width = width
		var height = height
		var strokeWidth = strokeWidth
		width = App.dp2px(width)
		height = App.dp2px(height)
		if (strokeWidth != null) {
			strokeWidth = App.dp2px(strokeWidth)
		}
		return ovalPx(width, height, color, strokeWidth, strokeColor)
	}
}

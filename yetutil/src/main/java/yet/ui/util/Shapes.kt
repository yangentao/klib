package yet.ui.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import yet.ui.ext.dp
import yet.ui.ext.dp2

/**
 * Created by entaoyang@163.com on 16/6/3.
 */




fun OvalDrawable(width: Int, height: Int, color: Int, strokeWidth: Int? = null, strokeColor: Int? = null): GradientDrawable {
	return OvalDrawablePx(dp(width), dp(height), color, dp2(strokeWidth), strokeColor)
}

fun OvalDrawablePx(width: Int, height: Int, color: Int, strokeWidth: Int? = null, strokeColor: Int? = null): GradientDrawable {
	val drawable = GradientDrawable()
	drawable.setShape(GradientDrawable.OVAL)
	drawable.setColor(color)
	if (strokeWidth != null && strokeColor != null) {
		drawable.setStroke(strokeWidth, strokeColor)
	}
	drawable.setSize(width, height)
	return drawable
}


fun RoundDrawable(corner: Int?, fillColor: Int?, strokeWidth: Int? = null, strokeColor: Int? = null): GradientDrawable {
	return RoundDrawablePx(dp2(corner), fillColor, dp2(strokeWidth), strokeColor)
}

fun RoundDrawablePx(corner: Int?, fillColor: Int?, strokeWidth: Int? = null, strokeColor: Int? = null): GradientDrawable {
	val gd = GradientDrawable()
	gd.setShape(GradientDrawable.RECTANGLE)
	if (corner != null) {
		gd.setCornerRadius(corner.toFloat())
	}
	if (fillColor != null) {
		gd.setColor(fillColor)
	}
	if (strokeWidth != null && strokeColor != null) {
		gd.setStroke(strokeWidth, strokeColor)
	}
	return gd
}

fun makeRoundEdgeRectDrawable(width: Int, height: Int, color: Int, border: Int? = null, borderColor: Int? = null): Drawable {
	val d = RoundDrawable(height / 2, color, 1, borderColor)
	d.setSize(dp(width), dp(height))
	return d
}


package net.yet.ui.ext

import android.widget.Switch
import net.yet.theme.Colors
import net.yet.ui.util.StateImage
import net.yet.ui.util.makeRoundEdgeRectDrawable
import net.yet.util.app.OS

/**
 * Created by entaoyang@163.com on 16/6/3.
 */


fun <T : Switch> T.themeSwitch(): T {
	if (OS.GE50) {
		return this
	}
	if (OS.API >= 16) {
		this.thumbTextPadding = dp(10)
	}

	val w1 = 30
	val h1 = 30

	val d1 = makeRoundEdgeRectDrawable(w1, h1, 0xFFCCCCCC.toInt())
	val d2 = makeRoundEdgeRectDrawable(w1, h1, 0xFF4A90E2.toInt())
	val d3 = makeRoundEdgeRectDrawable(w1, h1, Colors.LightGray, 1, Colors.GrayMajor)

	this.thumbDrawable = StateImage(d1).checked(d2).enabled(d3, false).value


	val w = 50
	val h = 30
	val dd1 = makeRoundEdgeRectDrawable(w, h, Colors.WHITE, 1, Colors.LightGray)
	val dd2 = makeRoundEdgeRectDrawable(w, h, Colors.Safe)
	val dd3 = makeRoundEdgeRectDrawable(w, h, Colors.LightGray, 1, Colors.WHITE)
	this.trackDrawable = StateImage(dd1).checked(dd2).enabled(dd3, false).value
	return this
}

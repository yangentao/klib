package yet.ui.ext

import android.widget.Button
import android.widget.CheckBox
import yet.theme.Colors
import yet.ui.res.ResConst

/**
 * Created by entaoyang@163.com on 16/4/28.
 */

fun <T : Button> T.styleGreen(corner: Int = 2): T {
	this.textSizeB().textColor(Colors.WHITE, Colors.Fade).backDrawable(ResConst.greenButton(corner))
	return this
}

fun <T : Button> T.themeGreen(corner: Int = 2): T {
	return this.styleGreen(corner)
}

fun <T : Button> T.styleRed(corner: Int = 2): T {
	this.textSizeB().textColor(Colors.WHITE).backDrawable(ResConst.redButton(corner))
	return this
}

fun <T : Button> T.themeRed(corner: Int = 2): T {
	return this.styleRed(corner)
}

fun <T : Button> T.styleWhite(corner: Int = 2): T {
	this.textSizeB().textColorMajor().backDrawable(ResConst.whiteButton(corner))
	return this
}

fun <T : Button> T.themeWhite(corner: Int = 2): T {
	return this.styleWhite(corner)
}

fun <T : CheckBox> T.styleSwitch(): T {
//	val w = 50
//	val h = 30
//	val dd1 = makeRoundEdgeRectDrawable(w, h, C.WHITE, 1, C.lightGrayColor)
//	val dd2 = makeRoundEdgeRectDrawable(w, h, C.safeColor)
////		val dd3 = makeRoundEdgeRectDrawable(w, h, C.lightGrayColor, 1, C.WHITE)
////		cb.checkMarkDrawable = StateImage(dd1).checked(dd2).enabled(dd3, false).value
////		cb.checkMarkDrawable = StateImage(dd1).checked(dd2).value
//	val draw = StateImage(dd1).checked(dd2).value
////		cb.setBackgroundDrawable(StateImage(dd1).checked(dd2).value)
////		cb.checkMarkDrawable = Res.drawable("checkbox", true)
//	cb.buttonDrawable = draw
//	cb.backColorPage()
//	cb.compoundDrawablePadding = 0
//	this.textSizeB().textColorMajor().backDrawable(ShapeDef.whiteButton())
	return this
}

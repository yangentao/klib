package net.yet.ui.ext

import android.graphics.drawable.Drawable
import android.widget.RadioButton
import android.widget.RadioGroup
import net.yet.R
import net.yet.ext.size
import net.yet.ui.res.ImageStated
import net.yet.ui.res.Res

/**
 * Created by entaoyang@163.com on 2016-11-07.
 */

// IMAGE--Title------CHECK
fun <T : RadioButton> T.styleImageTextCheck(leftRes: Int, rightNormal: Int = R.drawable.checkbox, rightChecked: Int = R.drawable.checkbox_checked): T {
	val rightImg = ImageStated(Res.drawable(rightNormal)).checked(Res.drawable(rightChecked)).value.size(15)
	this.buttonDrawable = null
	this.setCompoundDrawables(Res.drawable(leftRes).size(27), null, rightImg.size(25), null)
	this.compoundDrawablePadding = dp(15)
	return this
}

fun <T : RadioButton> T.styleImageTextCheck(leftDraw: Drawable?, rightNormal: Drawable, rightChecked: Drawable): T {
	val rightImg = ImageStated(rightNormal).checked(rightChecked).value.size(15)
	this.buttonDrawable = null
	this.setCompoundDrawables(leftDraw?.size(27), null, rightImg.size(25), null)
	this.compoundDrawablePadding = dp(15)
	return this
}


fun <T : RadioGroup> T.addRadioButton(title: String, block: RadioGroup.LayoutParams.() -> RadioGroup.LayoutParams): RadioButton {
	val view = this.context.createRadioButton()
	view.text = title
	val lp = radioParam()
	lp.heightButton().widthFill()
	lp.block()
	this.addView(view, lp)
	return view
}


fun radioParam(): RadioGroup.LayoutParams {
	return RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
}

fun radioParam(f: RadioGroup.LayoutParams.() -> RadioGroup.LayoutParams): RadioGroup.LayoutParams {
	var lp = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
	lp.f()
	return lp
}
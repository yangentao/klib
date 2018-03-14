package yet.ui.ext

import android.graphics.drawable.Drawable
import android.widget.RadioButton
import android.widget.RadioGroup
import net.yet.R
import yet.ext.size
import yet.ui.res.ImageStated
import yet.ui.res.Img
import yet.ui.viewcreator.createRadioButton

/**
 * Created by entaoyang@163.com on 2016-11-07.
 */

// IMAGE--Title------CHECK
fun <T : RadioButton> T.styleImageTextCheckRes(leftRes: Int): T {
	return this.styleImageTextCheckRes(leftRes, R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
}

fun <T : RadioButton> T.styleImageTextCheckRes(leftRes: Int, rightNormal: Int, rightChecked: Int): T {
	val rightImg = ImageStated(Img.res(rightNormal)).checked(Img.res(rightChecked)).value.size(15)
	this.buttonDrawable = null
	this.setCompoundDrawables(Img.res(leftRes).size(27), null, rightImg.size(25), null)
	this.compoundDrawablePadding = dp(15)
	return this
}

fun <T : RadioButton> T.styleImageTextCheck(leftDraw: Drawable?): T {
	return styleImageTextCheck(leftDraw, Img.res(R.drawable.yet_checkbox), Img.res(R.drawable.yet_checkbox_checked))
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
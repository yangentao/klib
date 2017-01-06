package net.yet.ui.ext

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.*
import net.yet.theme.Colors
import net.yet.ui.res.Img
import net.yet.ui.res.ResConst
import net.yet.ui.widget.EditTextX

/**
 * Created by entaoyang@163.com on 2016-07-22.
 */

//fun Fragment.createConstraintLayout():ConstraintLayout {
//	return ConstraintLayout(this.activity).genId()
//}
//fun View.createConstraintLayout():ConstraintLayout {
//	return ConstraintLayout(this.context).genId()
//}
//fun Context.createConstraintLayout():ConstraintLayout {
//	return ConstraintLayout(this).genId()
//}

fun View.createRadioButton(): RadioButton {
	return RadioButton(this.context).genId().gravityLeftCenter()
}

fun Fragment.createRadioButton(): RadioButton {
	return RadioButton(this.activity).genId().gravityLeftCenter()
}

fun Context.createRadioButton(): RadioButton {
	return RadioButton(this).genId().gravityLeftCenter()
}

fun View.createRadioGroup(): RadioGroup {
	return RadioGroup(this.context).genId()
}

fun Fragment.createRadioGroup(): RadioGroup {
	return RadioGroup(this.activity).genId()
}

fun Context.createRadioGroup(): RadioGroup {
	return RadioGroup(this).genId()
}

fun View.createImageButton(): ImageButton {
	return ImageButton(this.context).genId()
}

fun Fragment.createImageButton(): ImageButton {
	return ImageButton(this.activity).genId()
}

fun Context.createImageButton(): ImageButton {
	return ImageButton(this).genId()
}


fun View.createListView(): ListView {
	val lv = ListView(this.context).genId()
	lv.cacheColorHint = 0
	lv.selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
	return lv
}

fun Fragment.createListView(): ListView {
	val lv = ListView(this.activity).genId()
	lv.cacheColorHint = 0
	lv.selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
	return lv
}

fun Context.createListView(): ListView {
	val lv = ListView(this).genId()
	lv.cacheColorHint = 0
	lv.selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
	return lv
}


fun View.createLinearVertical(): LinearLayout {
	return LinearLayout(this.context).genId().vertical()
}

fun Fragment.createLinearVertical(): LinearLayout {
	return LinearLayout(this.activity).genId().vertical()
}

fun Context.createLinearVertical(): LinearLayout {
	return LinearLayout(this).genId().vertical()
}

fun View.createLinearHorizontal(): LinearLayout {
	return LinearLayout(this.context).genId().horizontal()
}

fun Fragment.createLinearHorizontal(): LinearLayout {
	return LinearLayout(this.activity).genId().horizontal()
}

fun Context.createLinearHorizontal(): LinearLayout {
	return LinearLayout(this).genId().horizontal()
}


fun View.createImageView(): ImageView {
	val b = ImageView(this.context).genId()
	b.adjustViewBounds = true
	b.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return b
}

fun Fragment.createImageView(): ImageView {
	val b = ImageView(this.activity).genId()
	b.adjustViewBounds = true
	b.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return b
}

fun Context.createImageView(): ImageView {
	val b = ImageView(this).genId()
	b.adjustViewBounds = true
	b.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return b
}


fun View.createView(): View {
	return View(this.context).genId()
}

fun Context.createView(): View {
	return View(this).genId()
}

fun Fragment.createView(): View {
	return View(this.activity).genId()
}

fun View.createTextView(): TextView {
	return this.createTextViewB()
}

fun Context.createTextView(): TextView {
	return this.createTextViewB()
}

fun Fragment.createTextView(): TextView {
	return this.createTextViewB()
}

fun View.createTextViewA(): TextView {
	return TextView(this.context).genId().gravityLeftCenter().textSizeA()
}

fun Context.createTextViewA(): TextView {
	return TextView(this).genId().gravityLeftCenter().textSizeA()
}

fun Fragment.createTextViewA(): TextView {
	return TextView(this.activity).genId().gravityLeftCenter().textSizeA()
}

fun View.createTextViewB(): TextView {
	return TextView(this.context).genId().gravityLeftCenter().textSizeB().textColorMajor()
}

fun Fragment.createTextViewB(): TextView {
	return TextView(this.activity).genId().gravityLeftCenter().textSizeB().textColorMajor()
}

fun Context.createTextViewB(): TextView {
	return TextView(this).genId().gravityLeftCenter().textSizeB().textColorMajor()
}

fun View.createTextViewC(): TextView {
	return TextView(this.context).genId().gravityLeftCenter().textSizeC().textColorMinor()
}

fun Fragment.createTextViewC(): TextView {
	return TextView(this.activity).genId().gravityLeftCenter().textSizeC().textColorMinor()
}

fun Context.createTextViewC(): TextView {
	return TextView(this).genId().gravityLeftCenter().textSizeC().textColorMinor()
}

fun View.createTextViewD(): TextView {
	return TextView(this.context).genId().gravityLeftCenter().textSizeD().textColorMinor()
}

fun Fragment.createTextViewD(): TextView {
	return TextView(this.activity).genId().gravityLeftCenter().textSizeD().textColorMinor()
}

fun Context.createTextViewD(): TextView {
	return TextView(this).genId().gravityLeftCenter().textSizeD().textColorMinor()
}


fun View.createRelativeLayout(): RelativeLayout {
	return RelativeLayout(this.context).genId()
}

fun Context.createRelativeLayout(): RelativeLayout {
	return RelativeLayout(this).genId()
}

fun Fragment.createRelativeLayout(): RelativeLayout {
	return RelativeLayout(this.activity).genId()
}

fun View.createFrameLayout(): FrameLayout {
	return FrameLayout(this.context).genId()
}

fun Context.createFrameLayout(): FrameLayout {
	return FrameLayout(this).genId()
}

fun Fragment.createFrameLayout(): FrameLayout {
	return FrameLayout(this.activity).genId()
}


fun View.createEditText(): EditText {
	val ed = EditText(this.context).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(5, 2, 5, 2)
	return ed
}

fun Context.createEditText(): EditText {
	val ed = EditText(this).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(8, 2, 8, 2)
	return ed
}

fun Fragment.createEditText(): EditText {
	val ed = EditText(this.activity).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(5, 2, 5, 2)
	return ed
}

fun View.createEditTextX(): EditTextX {
	val ed = EditTextX(this.context).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(8, 2, 8, 2)
	return ed
}

fun Context.createEditTextX(): EditTextX {
	val ed = EditTextX(this).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(8, 2, 8, 2)
	return ed
}

fun Fragment.createEditTextX(): EditTextX {
	val ed = EditTextX(this.activity).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(10, 2, 10, 2)
	return ed
}


fun View.createEditArea(): EditText {
	val ed = EditText(this.context).genId()
	ed.textSizeB().gravityTopLeft().backDrawable(ResConst.input()).padding(10, 5, 10, 5)
	return ed
}

fun Context.createEditArea(): EditText {
	val ed = EditText(this).genId()
	ed.textSizeB().gravityTopLeft().backDrawable(ResConst.input()).padding(10, 5, 10, 5)
	return ed
}

fun Fragment.createEditArea(): EditText {
	val ed = EditText(this.activity).genId()
	ed.textSizeB().gravityTopLeft().backDrawable(ResConst.input()).padding(10, 5, 10, 5)
	return ed
}


fun View.createButton(text: String = ""): Button {
	return Button(this.context).genId().text(text).textSizeB().padding(3)
}

fun Fragment.createButton(text: String = ""): Button {
	return Button(this.activity).genId().text(text).textSizeB().padding(3)
}

fun Context.createButton(text: String = ""): Button {
	return Button(this).genId().text(text).textSizeB().padding(3)
}

fun View.createCheckbox(): CheckBox {
	return CheckBox(this.context).genId()
}

fun Fragment.createCheckbox(): CheckBox {
	return CheckBox(this.activity).genId()
}

fun Context.createCheckbox(): CheckBox {
	return CheckBox(this).genId()
}




























package yet.ui.ext

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*
import yet.theme.Colors
import yet.ui.res.Img
import yet.ui.res.ResConst
import yet.ui.widget.EditTextX

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

//RadioButton
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRadioButton(param: P, block: RadioButton.() -> Unit): RadioButton {
	val v = this.createRadioButton()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRadioButton(index: Int, param: P, block: RadioButton.() -> Unit): RadioButton {
	val v = this.createRadioButton()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRadioButtonBefore(ankor: View, param: P, block: RadioButton.() -> Unit): RadioButton {
	val n = this.indexOfChild(ankor)
	return this.addRadioButton(n, param, block)
}

fun View.createRadioButton(): RadioButton {
	return this.context.createRadioButton()
}

fun Fragment.createRadioButton(): RadioButton {
	return this.activity.createRadioButton()
}

fun Context.createRadioButton(): RadioButton {
	return RadioButton(this).genId().gravityLeftCenter()
}


//RadioGroup
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRadioGroup(param: P, block: RadioGroup.() -> Unit): RadioGroup {
	val v = this.createRadioGroup()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRadioGroup(index: Int, param: P, block: RadioGroup.() -> Unit): RadioGroup {
	val v = this.createRadioGroup()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRadioGroupBefore(ankor: View, param: P, block: RadioGroup.() -> Unit): RadioGroup {
	return this.addRadioGroup(this.indexOfChild(ankor), param, block)
}

fun View.createRadioGroup(): RadioGroup {
	return this.context.createRadioGroup()
}

fun Fragment.createRadioGroup(): RadioGroup {
	return this.activity.createRadioGroup()
}

fun Context.createRadioGroup(): RadioGroup {
	return RadioGroup(this).genId()
}

//image button
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addImageButton(param: P, block: ImageButton.() -> Unit): ImageButton {
	val v = this.createImageButton()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addImageButton(index: Int, param: P, block: ImageButton.() -> Unit): ImageButton {
	val v = this.createImageButton()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addImageButtonBefore(ankor: View, param: P, block: ImageButton.() -> Unit): ImageButton {
	return this.addImageButton(this.indexOfChild(ankor), param, block)
}

fun View.createImageButton(): ImageButton {
	return this.context.createImageButton()
}

fun Fragment.createImageButton(): ImageButton {
	return this.activity.createImageButton()
}

fun Context.createImageButton(): ImageButton {
	return ImageButton(this).genId()
}

//List View
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addListView(param: P, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addListView(index: Int, param: P, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addListViewBefore(ankor: View, param: P, block: ListView.() -> Unit): ListView {
	return this.addListView(this.indexOfChild(ankor), param, block)
}


fun View.createListView(): ListView {
	return this.context.createListView()
}

fun Fragment.createListView(): ListView {
	return this.activity.createListView()
}

fun Context.createListView(): ListView {
	val lv = ListView(this).genId()
	lv.cacheColorHint = 0
	lv.selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
	return lv
}


//LinearLayout
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayout(param: P, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.createLinearLayout()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayout(index: Int, param: P, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.createLinearLayout()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutBefore(ankor: View, param: P, block: LinearLayout.() -> Unit): LinearLayout {
	return this.addLinearLayout(this.indexOfChild(ankor), param, block)
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutHor(param: P, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearHorizontal()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutHor(index: Int, param: P, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearHorizontal()
	this.addView(v, index, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutVer(param: P, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearVertical()
	this.addView(v, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutHorBefore(ankor: View, param: P, block: LinearLayout.() -> Unit): LinearLayout {
	return this.addLinearLayoutHor(this.indexOfChild(ankor), param, block)
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutVer(index: Int, param: P, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearVertical()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addLinearLayoutVerBefore(ankor: View, param: P, block: LinearLayout.() -> Unit): LinearLayout {
	return this.addLinearLayoutVer(this.indexOfChild(ankor), param, block)
}

fun View.createLinearLayout(): LinearLayout {
	return this.context.createLinearLayout()
}

fun Fragment.createLinearLayout(): LinearLayout {
	return this.activity.createLinearLayout()
}

fun Context.createLinearLayout(): LinearLayout {
	return LinearLayout(this).genId()
}

fun View.createLinearVertical(): LinearLayout {
	return this.context.createLinearVertical()
}

fun Fragment.createLinearVertical(): LinearLayout {
	return this.activity.createLinearVertical()
}

fun Context.createLinearVertical(): LinearLayout {
	return LinearLayout(this).genId().vertical()
}

fun View.createLinearHorizontal(): LinearLayout {
	return this.context.createLinearHorizontal()
}

fun Fragment.createLinearHorizontal(): LinearLayout {
	return this.activity.createLinearHorizontal()
}

fun Context.createLinearHorizontal(): LinearLayout {
	return LinearLayout(this).genId().horizontal()
}


//Image View

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addImageView(param: P, block: ImageView.() -> Unit): ImageView {
	val v = this.createImageView()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addImageView(index: Int, param: P, block: ImageView.() -> Unit): ImageView {
	val v = this.createImageView()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addImageViewBefore(ankor: View, param: P, block: ImageView.() -> Unit): ImageView {
	return this.addImageView(this.indexOfChild(ankor), param, block)
}

fun View.createImageView(): ImageView {
	return this.context.createImageView()
}

fun Fragment.createImageView(): ImageView {
	return this.activity.createImageView()
}

fun Context.createImageView(): ImageView {
	val b = ImageView(this).genId()
	b.adjustViewBounds = true
	b.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return b
}


//View
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addView(param: P, block: View.() -> Unit): View {
	val v = this.createView()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addView(index: Int, param: P, block: View.() -> Unit): View {
	val v = this.createView()
	this.addView(v, index, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addViewBefore(ankor: View, param: P, block: View.() -> Unit): View {
	return this.addView(this.indexOfChild(ankor), param, block)
}

fun View.createView(): View {
	return this.context.createView()
}

fun Fragment.createView(): View {
	return this.activity.createView()
}

fun Context.createView(): View {
	return View(this).genId()
}


//TextView
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTextView(param: P, block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTextView(index: Int, param: P, block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTextViewBefore(ankor: View, param: P, block: TextView.() -> Unit): TextView {
	return this.addTextView(this.indexOfChild(ankor), param, block)
}
fun View.createTextView(): TextView {
	return this.context.createTextView()
}

fun Context.createTextView(): TextView {
	return TextView(this).genId().gravityLeftCenter().textSizeB().textColorMajor()
}

fun Fragment.createTextView(): TextView {
	return this.activity.createTextView()
}

fun View.createTextViewA(): TextView {
	return this.createTextView().textSizeA()
}

fun Context.createTextViewA(): TextView {
	return this.createTextView().textSizeA()
}

fun Fragment.createTextViewA(): TextView {
	return this.createTextView().textSizeA()
}

fun View.createTextViewB(): TextView {
	return this.createTextView().textSizeB()
}

fun Fragment.createTextViewB(): TextView {
	return this.createTextView().textSizeB()
}

fun Context.createTextViewB(): TextView {
	return this.createTextView().textSizeB()
}

fun View.createTextViewC(): TextView {
	return this.createTextView().textSizeC()
}

fun Fragment.createTextViewC(): TextView {
	return this.createTextView().textSizeC()
}

fun Context.createTextViewC(): TextView {
	return this.createTextView().textSizeC()
}

fun View.createTextViewD(): TextView {
	return this.createTextView().textSizeD()
}

fun Fragment.createTextViewD(): TextView {
	return this.createTextView().textSizeD()
}

fun Context.createTextViewD(): TextView {
	return this.createTextView().textSizeD()
}


//RelativeLayout
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRelativeLayout(param: P, block: RelativeLayout.() -> Unit): RelativeLayout {
	val v = this.createRelativeLayout()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRelativeLayout(index: Int, param: P, block: RelativeLayout.() -> Unit): RelativeLayout {
	val v = this.createRelativeLayout()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addRelativeLayoutBefore(ankor: View, param: P, block: RelativeLayout.() -> Unit): RelativeLayout {
	return this.addRelativeLayout(this.indexOfChild(ankor), param, block)
}

fun View.createRelativeLayout(): RelativeLayout {
	return this.context.createRelativeLayout()
}

fun Fragment.createRelativeLayout(): RelativeLayout {
	return this.activity.createRelativeLayout()
}

fun Context.createRelativeLayout(): RelativeLayout {
	return RelativeLayout(this).genId()
}


//FrameLayout
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addFrameLayout(param: P, block: FrameLayout.() -> Unit): FrameLayout {
	val v = this.createFrameLayout()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addFrameLayout(index: Int, param: P, block: FrameLayout.() -> Unit): FrameLayout {
	val v = this.createFrameLayout()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addFrameLayoutBefore(ankor: View, param: P, block: FrameLayout.() -> Unit): FrameLayout {
	return this.addFrameLayout(this.indexOfChild(ankor), param, block)
}
fun View.createFrameLayout(): FrameLayout {
	return this.context.createFrameLayout()
}

fun Fragment.createFrameLayout(): FrameLayout {
	return this.activity.createFrameLayout()
}

fun Context.createFrameLayout(): FrameLayout {
	return FrameLayout(this).genId()
}

//EditText
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditText(param: P, block: EditText.() -> Unit): EditText {
	val v = this.createEditText()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditText(index: Int, param: P, block: EditText.() -> Unit): EditText {
	val v = this.createEditText()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditTextBefore(ankor: View, param: P, block: EditText.() -> Unit): EditText {
	return this.addEditText(this.indexOfChild(ankor), param, block)
}

fun View.createEditText(): EditText {
	return this.context.createEditText()
}

fun Fragment.createEditText(): EditText {
	return this.activity.createEditText()
}

fun Context.createEditText(): EditText {
	val ed = EditText(this).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(8, 2, 8, 2)
	return ed
}


//EditTextX
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditTextX(param: P, block: EditTextX.() -> Unit): EditTextX {
	val v = this.createEditTextX()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditTextX(index: Int, param: P, block: EditTextX.() -> Unit): EditTextX {
	val v = this.createEditTextX()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditTextXBefore(ankor: View, param: P, block: EditTextX.() -> Unit): EditTextX {
	return this.addEditTextX(this.indexOfChild(ankor), param, block)
}
fun View.createEditTextX(): EditTextX {
	return this.context.createEditTextX()
}

fun Fragment.createEditTextX(): EditTextX {
	return this.activity.createEditTextX()
}

fun Context.createEditTextX(): EditTextX {
	val ed = EditTextX(this).genId().singleLine()
	ed.textSizeB().gravityLeftCenter().backDrawable(ResConst.input()).padding(8, 2, 8, 2)
	return ed
}


//EditArea
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditArea(param: P, block: EditText.() -> Unit): EditText {
	val v = this.createEditArea()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditArea(index: Int, param: P, block: EditText.() -> Unit): EditText {
	val v = this.createEditArea()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addEditAreaBefore(ankor: View, param: P, block: EditText.() -> Unit): EditText {
	return this.addEditArea(this.indexOfChild(ankor), param, block)
}
fun View.createEditArea(): EditText {
	return this.context.createEditArea()
}

fun Fragment.createEditArea(): EditText {
	return this.activity.createEditArea()
}

fun Context.createEditArea(): EditText {
	val ed = EditText(this).genId()
	ed.textSizeB().gravityTopLeft().backDrawable(ResConst.input()).padding(10, 5, 10, 5).multiLine()
	return ed
}


//Button
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addButton(param: P, block: Button.() -> Unit): Button {
	val v = this.createButton()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addButton(index: Int, param: P, block: Button.() -> Unit): Button {
	val v = this.createButton()
	this.addView(v, index, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addButtonBefore(ankor: View, param: P, block: Button.() -> Unit): Button {
	return this.addButton(this.indexOfChild(ankor), param, block)
}
fun View.createButton(text: String = ""): Button {
	return this.context.createButton(text)
}

fun Fragment.createButton(text: String = ""): Button {
	return this.activity.createButton(text)
}

fun Context.createButton(text: String = ""): Button {
	return Button(this).genId().text(text).textSizeB().padding(3)
}

//check box
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addCheckBox(param: P, block: CheckBox.() -> Unit): CheckBox {
	val v = this.createCheckBox()
	this.addView(v, param)
	v.block()
	return v
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addCheckBox(index: Int, param: P, block: CheckBox.() -> Unit): CheckBox {
	val v = this.createCheckBox()
	this.addView(v, index, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addCheckBoxBefore(ankor: View, param: P, block: CheckBox.() -> Unit): CheckBox {
	return this.addCheckBox(this.indexOfChild(ankor), param, block)
}

fun View.createCheckBox(): CheckBox {
	return this.context.createCheckBox()
}

fun Fragment.createCheckBox(): CheckBox {
	return this.activity.createCheckBox()
}

fun Context.createCheckBox(): CheckBox {
	return CheckBox(this).genId()
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addViewBefore(child: View, ankor: View, param: P) {
	this.addView(child, this.indexOfChild(ankor), param)
}






























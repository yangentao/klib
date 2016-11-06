package net.yet.ui.ext

import android.widget.*
import net.yet.ui.widget.EditTextX

/**
 * Created by entaoyang@163.com on 2016-11-04.
 */

fun <T : RelativeLayout> T.addRelative(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): RelativeLayout {
	val view = this.context.createRelativeLayout()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}
fun <T : RelativeLayout> T.addLinearHor(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): LinearLayout {
	val view = this.context.createLinearHorizontal()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addLinearVer(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): LinearLayout {
	val view = this.context.createLinearVertical()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addButton(title: String, block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): Button {
	val view = this.context.createButton(title)
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addCheckbox(title: String, block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): CheckBox {
	val view = this.context.createCheckbox()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addEditText(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): EditText {
	val view = this.context.createEditText()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addEditArea(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): EditText {
	val view = this.context.createEditArea()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addEditTextX(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): EditTextX {
	val view = this.context.createEditTextX()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}


fun <T : RelativeLayout> T.addImageButton(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): ImageButton {
	val view = this.context.createImageButton()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addImageView(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): ImageView {
	val view = this.context.createImageView()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addTextView(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	val view = this.context.createTextView()
	val lp = relativeParam(block)
	this.addView(view, lp)
	return view
}

fun <T : RelativeLayout> T.addTextViewLarge(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeLarge()
}

fun <T : RelativeLayout> T.addTextViewBig(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeBig()
}

fun <T : RelativeLayout> T.addTextViewTitle(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeTitle()
}

fun <T : RelativeLayout> T.addTextViewNormal(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeNormal()
}

fun <T : RelativeLayout> T.addTextViewSmall(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeSmall()
}

fun <T : RelativeLayout> T.addTextViewTiny(block: RelativeLayout.LayoutParams.() -> RelativeLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeTiny()
}
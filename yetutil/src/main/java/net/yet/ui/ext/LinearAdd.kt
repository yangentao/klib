package net.yet.ui.ext

import android.widget.*
import net.yet.ui.widget.EditTextX

/**
 * Created by entaoyang@163.com on 2016-11-04.
 */

fun <T : LinearLayout> T.addRadioGroup(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): RadioGroup {
	val view = this.context.createRadioGroup()
	val lp = linearParam()
	lp.block()
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addRelative(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): RelativeLayout {
	val view = this.context.createRelativeLayout()
	val lp = linearParam()
	lp.block()
	this.addView(view, lp)
	return view
}
fun <T : LinearLayout> T.addLinearHor(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): LinearLayout {
	val view = this.context.createLinearHorizontal()
	val lp = linearParam()
	lp.block()
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addLinearVer(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): LinearLayout {
	val view = this.context.createLinearVertical()
	val lp = linearParam()
	lp.block()
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addButton(title: String, block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): Button {
	val view = this.context.createButton(title)
	val lp = linearParam()
	lp.heightButton()
	lp.block()
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addCheckbox(title: String, block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): CheckBox {
	val view = this.context.createCheckbox()
	val lp = linearParam(block)
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addEditText(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): EditText {
	val view = this.context.createEditText()
	val lp = linearParam()
	lp.heightEdit()
	lp.block()
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addEditArea(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): EditText {
	val view = this.context.createEditArea()
	val lp = linearParam(block)
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addEditTextX(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): EditTextX {
	val view = this.context.createEditTextX()
	val lp = linearParam()
	lp.heightEdit()
	lp.block()
	this.addView(view, lp)
	return view
}


fun <T : LinearLayout> T.addImageButton(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): ImageButton {
	val view = this.context.createImageButton()
	val lp = linearParam(block)
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addImageView(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): ImageView {
	val view = this.context.createImageView()
	val lp = linearParam(block)
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addTextView(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	val view = this.context.createTextView()
	val lp = linearParam(block)
	this.addView(view, lp)
	return view
}

fun <T : LinearLayout> T.addTextViewLarge(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeLarge()
}

fun <T : LinearLayout> T.addTextViewBig(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeBig()
}

fun <T : LinearLayout> T.addTextViewTitle(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeTitle()
}

fun <T : LinearLayout> T.addTextViewNormal(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeNormal()
}

fun <T : LinearLayout> T.addTextViewSmall(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeSmall()
}

fun <T : LinearLayout> T.addTextViewTiny(block: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): TextView {
	return addTextView(block = block).textSizeTiny()
}
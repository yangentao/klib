//@file:JvmMultifileClass
//@file:JvmName("UIKit")
package net.yet.ui.ext

import android.view.View
import android.view.ViewGroup
import net.yet.theme.InputSize

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

fun <T : ViewGroup.LayoutParams> T.set(view: View) {
	view.layoutParams = this
}


fun layoutParam(): ViewGroup.LayoutParams {
	return ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}


fun <T : ViewGroup.LayoutParams> T.size(w: Int, h: Int = w): T {
	return widthDp(w).heightDp(h)
}

fun <T : ViewGroup.LayoutParams> T.width_(w: Int): T {
	this.width = dp(w)
	return this
}

fun <T : ViewGroup.LayoutParams> T.width(w: Int): T {
	this.width = dp(w)
	return this
}

fun <T : ViewGroup.LayoutParams> T.widthDp(w: Int): T {
	this.width = dp(w)
	return this
}

fun <T : ViewGroup.LayoutParams> T.widthPx(w: Int): T {
	this.width = w
	return this
}


fun <T : ViewGroup.LayoutParams> T.widthWrap(): T {
	this.width = ViewGroup.LayoutParams.WRAP_CONTENT
	return this
}

fun <T : ViewGroup.LayoutParams> T.widthFill(): T {
	this.width = ViewGroup.LayoutParams.MATCH_PARENT
	return this
}

fun <T : ViewGroup.LayoutParams> T.height(h: Int): T {
	this.height = dp(h)
	return this
}

fun <T : ViewGroup.LayoutParams> T.height_(h: Int): T {
	this.height = dp(h)
	return this
}

fun <T : ViewGroup.LayoutParams> T.heightDp(h: Int): T {
	this.height = dp(h)
	return this
}

fun <T : ViewGroup.LayoutParams> T.heightWrap(): T {
	this.height = ViewGroup.LayoutParams.WRAP_CONTENT
	return this
}

fun <T : ViewGroup.LayoutParams> T.heightFill(): T {
	this.height = ViewGroup.LayoutParams.MATCH_PARENT
	return this
}

fun <T : ViewGroup.LayoutParams> T.wrap(): T {
	this.height = ViewGroup.LayoutParams.WRAP_CONTENT
	this.width = ViewGroup.LayoutParams.WRAP_CONTENT
	return this
}

fun <T : ViewGroup.LayoutParams> T.fill(): T {
	this.width = ViewGroup.LayoutParams.MATCH_PARENT
	this.height = ViewGroup.LayoutParams.MATCH_PARENT
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.margins(left: Int, top: Int, right: Int, bottom: Int): T {
	this.setMargins(dp(left), dp(top), dp(right), dp(bottom))
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginLR(left: Int, right: Int): T {
	this.setMargins(dp(left), this.topMargin, dp(right), this.bottomMargin)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginTB(top: Int, bottom: Int): T {
	this.setMargins(this.leftMargin, dp(top), this.rightMargin, dp(bottom))
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.margins(m: Int): T {
	val v = dp(m)
	this.setMargins(v, v, v, v)
	return this
}


fun <T : ViewGroup.LayoutParams> T.heightButton(): T {
	return heightDp(InputSize.ButtonHeight)
}

fun <T : ViewGroup.LayoutParams> T.heightButtonSmall(): T {
	return heightDp(InputSize.ButtonHeightSmall)
}

fun <T : ViewGroup.LayoutParams> T.heightEdit(): T {
	return heightDp(InputSize.EditHeight)
}

fun <T : ViewGroup.LayoutParams> T.heightEditSmall(): T {
	return heightDp(InputSize.EditHeightSmall)
}



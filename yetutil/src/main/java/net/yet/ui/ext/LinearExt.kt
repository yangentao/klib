package net.yet.ui.ext

import android.app.Fragment
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import net.yet.theme.Colors
import net.yet.util.BmpUtil
import net.yet.util.app.App

/**
 * Created by entaoyang@163.com on 16/3/12.
 */

class Divider(var color: Int = Colors.LineGray) {
	var size: Int = 1
	var begin: Boolean = false
	var mid: Boolean = true
	var end: Boolean = false
	var pad: Int = 0

	fun size(n: Int): Divider {
		this.size = n
		return this
	}

	fun color(color: Int): Divider {
		this.color = color
		return this
	}

	fun begin(b: Boolean = true): Divider {
		this.begin = b
		return this
	}

	fun mid(b: Boolean = true): Divider {
		this.mid = b
		return this
	}

	fun end(b: Boolean = true): Divider {
		this.end = b
		return this
	}

	fun pad(n: Int): Divider {
		this.pad = n
		return this
	}

}


fun <T : LinearLayout> T.divider(ld: Divider): T {
	if (ld.size > 0) {
		val d = BmpUtil.lineDraw(ld.size, ld.size, ld.color)
		this.dividerDrawable = d
		this.dividerPadding = App.dp2px(ld.pad)
		var n = LinearLayout.SHOW_DIVIDER_NONE
		if (ld.begin) {
			n = n or LinearLayout.SHOW_DIVIDER_BEGINNING
		}
		if (ld.mid) {
			n = n or LinearLayout.SHOW_DIVIDER_MIDDLE
		}
		if (ld.end) {
			n = n or LinearLayout.SHOW_DIVIDER_END
		}
		this.showDividers = n
	}
	return this
}


//create
fun Context.linearLayout(): LinearLayout {
	val ll = LinearLayout(this)
	return ll
}

fun Fragment.linearLayout(): LinearLayout {
	return this.activity.linearLayout()
}

fun ViewGroup.linearLayout(): LinearLayout {
	return this.context.linearLayout()
}

//create hor
fun Context.linearHor(): LinearLayout {
	val ll = LinearLayout(this)
	ll.horizontal()
	return ll
}

fun Fragment.linearHor(): LinearLayout {
	return this.activity.linearHor()
}

fun ViewGroup.linearHor(): LinearLayout {
	return this.context.linearHor()
}

//create ver
fun Context.linearVer(): LinearLayout {
	val ll = LinearLayout(this)
	ll.vertical()
	return ll
}

fun Fragment.linearVer(): LinearLayout {
	return this.activity.linearVer()
}

fun ViewGroup.linearVer(): LinearLayout {
	return this.context.linearVer()
}

fun <T : LinearLayout> T.orientationVertical(): T {
	this.orientation = LinearLayout.VERTICAL
	return this
}

fun <T : LinearLayout> T.orientationHorizontal(): T {
	this.orientation = LinearLayout.HORIZONTAL
	return this
}

fun <T : LinearLayout> T.horizontal(): T {
	this.orientation = LinearLayout.HORIZONTAL
	return this
}

fun <T : LinearLayout> T.vertical(): T {
	this.orientation = LinearLayout.VERTICAL
	return this
}

fun <T : LinearLayout> T.gravity(n: Int): T {
	this.setGravity(n)
	return this
}

fun <T : LinearLayout> T.gravityCenterVertical(): T {
	this.setGravity(Gravity.CENTER_VERTICAL)
	return this
}

fun <T : LinearLayout> T.gravityCenterHorizontal(): T {
	this.setGravity(Gravity.CENTER_HORIZONTAL)
	return this
}

fun <T : LinearLayout> T.gravityLeftCenter(): T {
	this.setGravity(Gravity.LEFT or Gravity.CENTER)
	return this
}

fun <T : LinearLayout> T.gravityRightCenter(): T {
	this.setGravity(Gravity.RIGHT or Gravity.CENTER)
	return this
}

fun <T : LinearLayout> T.gravityCenter(): T {
	this.setGravity(Gravity.CENTER)
	return this
}

fun <T : LinearLayout> T.addViewParam(view: View, f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.addView(view, lp)
	return this
}

fun <T : LinearLayout> T.addViewParam(view: View, index: Int, f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.addView(view, index, lp)
	return this
}


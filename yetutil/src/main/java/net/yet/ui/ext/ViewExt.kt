package net.yet.ui.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import net.yet.ext.ColorDrawable
import net.yet.theme.Colors
import net.yet.ui.res.Img
import net.yet.ui.util.StateImage
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by entaoyang@163.com on 16/3/12.
 */

private val atomInt = AtomicInteger(1)

fun genViewId(): Int {
	while (true) {
		val n = atomInt.get()
		var newVal = n + 1
		if (newVal > 0x00FFFFFF) newVal = 1
		if (atomInt.compareAndSet(n, newVal)) {
			return n
		}
	}
}

fun < T : View> T.genId(): T {
	id = genViewId()
	return this
}

fun <T : View> T.gone(): T {
	visibility = View.GONE
	return this
}

fun <T : View> T.visiable(): T {
	visibility = View.VISIBLE
	return this
}

fun <T : View> T.invisiable(): T {
	visibility = View.INVISIBLE
	return this
}

fun <T : View> T.isGone(): Boolean {
	return visibility == View.GONE
}

fun <T : View> T.isVisiable(): Boolean {
	return visibility == View.VISIBLE
}

fun <T : View> T.isInvisiable(): Boolean {
	return visibility == View.INVISIBLE
}


fun < T : View> T.padding(left: Int, top: Int, right: Int, bottom: Int): T {
	this.setPadding(dp(left), dp(top), dp(right), dp(bottom));
	return this
}

fun < T : View> T.padding(p: Int): T {
	this.setPadding(dp(p), dp(p), dp(p), dp(p))
	return this
}


 fun <T : View> T.backColor(color: Int): T {
	setBackgroundColor(color)
	return this
}

fun < T : View> T.backColor(color: Int, fadeColor: Int): T {
	//    background = colorDrawable(color, fadeColor)
	setBackgroundDrawable(ColorDrawable(color, fadeColor))
	return this
}


fun < T : View> T.backColorWhite(): T {
	setBackgroundColor(Colors.WHITE)
	return this
}
fun <T : View> T.backColorWhiteFade(): T {
	backColor(Colors.WHITE, Colors.Fade)
	return this
}
fun <T : View> T.backColorTransFade(): T {
	backColor(Colors.TRANS, Colors.Fade)
	return this
}

fun <T : View> T.backColorPage(): T {
	setBackgroundColor(Colors.PageGray)
	return this
}

fun <T : View> T.backDrawable(d: Drawable): T {
	this.setBackgroundDrawable(d)
	return this
}
fun <T : View> T.backDrawable(normal: Drawable, pressed: Drawable): T {
	this.setBackgroundDrawable(StateImage(normal).pressed(pressed).value)
	return this
}
fun <T : View> T.backDrawable(name: String): T {
	this.setBackgroundDrawable(Img.namedStates(name, false))
	return this;
}


fun View.makeClickable(): View {
	this.isClickable = true
	return this
}
fun View.clickable(): View {
	this.isClickable = true
	return this
}

fun <T : View> T.setLinearParam(f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.layoutParams = lp
	return this;
}



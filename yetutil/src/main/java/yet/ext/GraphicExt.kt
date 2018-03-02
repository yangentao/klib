package yet.ext

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import yet.ui.ext.dp
import yet.ui.res.ColorList
import yet.ui.res.ColorStated

/**
 * Created by entaoyang@163.com on 2016-07-21.
 */

fun Drawable.limit(maxEdge: Int): Drawable {
	val h = this.intrinsicHeight
	val w = this.intrinsicWidth
	if (w > maxEdge || h > maxEdge) {
		if (w > h) {
			this.setBounds(0, 0, dp(maxEdge), dp(h * maxEdge / w))
		} else {
			this.setBounds(0, 0, dp(w * maxEdge / h), dp(maxEdge))
		}
	}
	return this
}

fun Drawable.size(w: Int, h: Int = w): Drawable {
//	if (this is GradientDrawable) {
//		this.setSize(w, h)
//	} else {
	this.setBounds(0, 0, dp(w), dp(h))
//	}
	return this
}

fun ColorDrawable(normal: Int, pressed: Int): Drawable {
	return ColorStated(normal).pressed(pressed).selected(pressed).focused(pressed).value
}

fun ColorList(normal: Int, pressed: Int): ColorStateList {
	return ColorList(normal).pressed(pressed).selected(pressed).focused(pressed).value
}

//#8f8, #800f, #ff884422
fun argb(colorString: String): Int {
	if (colorString[0] == '#') {
		val len = colorString.length
		if (len == 4 || len == 5) {
			val sb = StringBuffer(10)
			sb.append('#')
			for (i in 1..len - 1) {
				sb.append(colorString[i])
				sb.append(colorString[i])
			}
			return Color.parseColor(sb.toString())
		}
	}
	return Color.parseColor(colorString)
}

val Long.color: Int get() {
	if (this <= 0xffffff) {
		val L = 0xff000000 or this
		return L.toInt()
	}
	if (this <= 0xffffffff) {
		return toInt()
	}
	throw IllegalArgumentException("不是颜色值:${this.toString(16)}")
}
val Int.color: Int get() {
	return this.toLong().color
}
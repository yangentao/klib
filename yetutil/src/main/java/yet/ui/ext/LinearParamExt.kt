package yet.ui.ext

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout

/**
 * Created by entaoyang@163.com on 2016-07-21.
 */

fun <T : LinearLayout.LayoutParams> T.set(view: View) {
	view.layoutParams = this
}

fun lParam(): LinearLayout.LayoutParams {
	return linearParam()
}

fun linearParam(): LinearLayout.LayoutParams {
	return LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
}

fun linearParam(f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): LinearLayout.LayoutParams {
	var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
	lp.f()
	return lp
}


fun <T : LinearLayout.LayoutParams> T.weight_(w: Int): T {
	return weight(w.toFloat())
}

fun <T : LinearLayout.LayoutParams> T.weight(w: Int): T {
	return weight(w.toFloat())
}

fun <T : LinearLayout.LayoutParams> T.weight(w: Double): T {
	return weight(w.toFloat())
}

fun <T : LinearLayout.LayoutParams> T.weight(w: Float): T {
	weight = w
	return this
}


fun <T : LinearLayout.LayoutParams> T.gravityTop(): T {
	gravity = Gravity.TOP
	return this
}


fun <T : LinearLayout.LayoutParams> T.gravityTopCenter(): T {
	gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityBottom(): T {
	gravity = Gravity.BOTTOM
	return this
}


fun <T : LinearLayout.LayoutParams> T.gravityBottomCenter(): T {
	gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityLeft(): T {
	gravity = Gravity.LEFT
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityLeftCenter(): T {
	gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityRight(): T {
	gravity = Gravity.RIGHT
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityRightCenter(): T {
	gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityFill(): T {
	gravity = Gravity.FILL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityFillVertical(): T {
	gravity = Gravity.FILL_VERTICAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityFillHorizontal(): T {
	gravity = Gravity.FILL_HORIZONTAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityCenterVertical(): T {
	gravity = Gravity.CENTER_VERTICAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityCenterHorizontal(): T {
	gravity = Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : LinearLayout.LayoutParams> T.gravityCenter(): T {
	gravity = Gravity.CENTER
	return this
}

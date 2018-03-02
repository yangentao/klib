package yet.ui.ext

import android.view.Gravity
import android.widget.FrameLayout

/**
 * Created by entaoyang@163.com on 2016-10-29.
 */

fun fParam(): FrameLayout.LayoutParams {
	return frameParam()
}
fun frameParam(): FrameLayout.LayoutParams {
	val p = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
	return p
}


fun <T : FrameLayout.LayoutParams> T.gravityTop(): T {
	gravity = Gravity.TOP
	return this
}


fun <T : FrameLayout.LayoutParams> T.gravityTopCenter(): T {
	gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityBottom(): T {
	gravity = Gravity.BOTTOM
	return this
}


fun <T : FrameLayout.LayoutParams> T.gravityBottomCenter(): T {
	gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityLeft(): T {
	gravity = Gravity.LEFT
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityLeftCenter(): T {
	gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityRight(): T {
	gravity = Gravity.RIGHT
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityRightCenter(): T {
	gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityFill(): T {
	gravity = Gravity.FILL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityFillVertical(): T {
	gravity = Gravity.FILL_VERTICAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityFillHorizontal(): T {
	gravity = Gravity.FILL_HORIZONTAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityCenterVertical(): T {
	gravity = Gravity.CENTER_VERTICAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityCenterHorizontal(): T {
	gravity = Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : FrameLayout.LayoutParams> T.gravityCenter(): T {
	gravity = Gravity.CENTER
	return this
}

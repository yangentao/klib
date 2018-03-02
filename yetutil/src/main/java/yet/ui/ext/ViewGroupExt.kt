package yet.ui.ext

import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

fun <T : ViewGroup> T.createView(): View {
	return View(context).genId()
}

inline fun <reified T : ViewGroup> T.createTextView(): TextView {
	return createTextViewB()
}

inline fun <reified T : ViewGroup> T.createTextViewA(): TextView {
	val tv = TextView(context).genId().gravityLeftCenter().textSizeA()
	return tv
}

inline fun <reified T : ViewGroup> T.createTextViewB(): TextView {
	return TextView(context).genId().gravityLeftCenter().textSizeB().textColorMajor()
}

inline fun <reified T : ViewGroup> T.createTextViewC(): TextView {
	return TextView(context).genId().gravityLeftCenter().textSizeC().textColorMinor()
}

inline fun <reified T : ViewGroup> T.createTextViewD(): TextView {
	return TextView(context).genId().gravityLeftCenter().textSizeD().textColorMinor()
}

inline fun <reified T : ViewGroup> T.createLinearVertical(): LinearLayout {
	return LinearLayout(context).genId().vertical()
}

inline fun <reified T : ViewGroup> T.createLinearHorizontal(): LinearLayout {
	return LinearLayout(context).genId().horizontal()
}

inline fun <reified T : ViewGroup> T.createRelativeLayout(): RelativeLayout {
	return RelativeLayout(context).genId()
}

inline fun <reified T : ViewGroup> T.createFrameLayout(): FrameLayout {
	return FrameLayout(context).genId()
}

inline fun <reified T : ViewGroup> T.createImageView(): ImageView {
	val b = ImageView(context).genId()
	b.adjustViewBounds = true
	b.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return b
}
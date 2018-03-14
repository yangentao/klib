package yet.ui.viewcreator

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import yet.ui.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun Activity.frame(block: FrameLayout.() -> Unit): FrameLayout {
	val rl = FrameLayout(this).genId()
	rl.block()
	return rl
}

fun Fragment.frame(block: FrameLayout.() -> Unit): FrameLayout {
	val rl = FrameLayout(this.activity).genId()
	rl.block()
	return rl
}

//FrameLayout
fun <P : ViewGroup.LayoutParams> ViewGroup.frame(param: P, block: FrameLayout.() -> Unit): FrameLayout {
	val v = this.createFrame()
	this.addView(v, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.frame(index: Int, param: P, block: FrameLayout.() -> Unit): FrameLayout {
	val v = this.createFrame()
	this.addView(v, index, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.frameBefore(ankor: View, param: P, block: FrameLayout.() -> Unit): FrameLayout {
	return this.frame(this.indexOfChild(ankor), param, block)
}

fun View.createFrame(): FrameLayout {
	return this.context.createFrame()
}

fun Fragment.createFrame(): FrameLayout {
	return this.activity.createFrame()
}

fun Context.createFrame(): FrameLayout {
	return FrameLayout(this).genId()
}
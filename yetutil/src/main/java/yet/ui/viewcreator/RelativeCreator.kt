package yet.ui.viewcreator

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import yet.ui.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun Activity.relative(block: RelativeLayout.() -> Unit): RelativeLayout {
	val rl = RelativeLayout(this).genId()
	rl.block()
	return rl
}

fun Fragment.relative(block: RelativeLayout.() -> Unit): RelativeLayout {
	val rl = RelativeLayout(this.activity).genId()
	rl.block()
	return rl
}


//RelativeLayout
fun ViewGroup.relative(param: ViewGroup.LayoutParams, block: RelativeLayout.() -> Unit): RelativeLayout {
	val v = this.createRelative()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.relative(index: Int, param: ViewGroup.LayoutParams, block: RelativeLayout.() -> Unit): RelativeLayout {
	val v = this.createRelative()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.relativeBefore(ankor: View, param: ViewGroup.LayoutParams, block: RelativeLayout.() -> Unit): RelativeLayout {
	return this.relative(this.indexOfChild(ankor), param, block)
}

fun View.createRelative(): RelativeLayout {
	return this.context.createRelative()
}

fun Fragment.createRelative(): RelativeLayout {
	return this.activity.createRelative()
}

fun Context.createRelative(): RelativeLayout {
	return RelativeLayout(this).genId()
}

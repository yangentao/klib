package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import yet.ui.ext.genId

/**
 * Created by entaoyang@163.com on 2016-07-22.
 */

fun ViewGroup.addViewBefore(child: View, ankor: View, param: ViewGroup.LayoutParams) {
	this.addView(child, this.indexOfChild(ankor), param)
}


//View
fun ViewGroup.view(param: ViewGroup.LayoutParams, block: View.() -> Unit): View {
	val v = this.createView()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.view(index: Int, param: ViewGroup.LayoutParams, block: View.() -> Unit): View {
	val v = this.createView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.viewBefore(ankor: View, param: ViewGroup.LayoutParams, block: View.() -> Unit): View {
	return this.view(this.indexOfChild(ankor), param, block)
}

fun View.createView(): View {
	return this.context.createView()
}

fun Fragment.createView(): View {
	return this.activity.createView()
}

fun Context.createView(): View {
	return View(this).genId()
}


fun View.createScroll(): ScrollView {
	return this.context.createScroll()
}

fun Fragment.createScroll(): ScrollView {
	return this.activity.createScroll()
}

fun Context.createScroll(): ScrollView {
	return ScrollView(this).genId()
}


























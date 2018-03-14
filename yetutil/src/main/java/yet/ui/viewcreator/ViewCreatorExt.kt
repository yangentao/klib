package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import yet.ui.ext.genId

/**
 * Created by entaoyang@163.com on 2016-07-22.
 */

fun <P : ViewGroup.LayoutParams> ViewGroup.addViewBefore(child: View, ankor: View, param: P) {
	this.addView(child, this.indexOfChild(ankor), param)
}


//View
fun <P : ViewGroup.LayoutParams> ViewGroup.view(param: P, block: View.() -> Unit): View {
	val v = this.createView()
	this.addView(v, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.view(index: Int, param: P, block: View.() -> Unit): View {
	val v = this.createView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.viewBefore(ankor: View, param: P, block: View.() -> Unit): View {
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





























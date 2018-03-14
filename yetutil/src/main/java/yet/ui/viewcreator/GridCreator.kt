package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import yet.ui.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


fun <P : ViewGroup.LayoutParams> ViewGroup.grid(param: P, block: GridView.() -> Unit): GridView {
	val v = this.createGrid()
	this.addView(v, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.grid(index: Int, param: P, block: GridView.() -> Unit): GridView {
	val v = this.createGrid()
	this.addView(v, index, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.gridBefore(ankor: View, param: P, block: GridView.() -> Unit): GridView {
	return this.grid(this.indexOfChild(ankor), param, block)
}

fun View.createGrid(): GridView {
	return this.context.createGrid()
}

fun Fragment.createGrid(): GridView {
	return this.activity.createGrid()
}

fun Context.createGrid(): GridView {
	return GridView(this).genId()
}

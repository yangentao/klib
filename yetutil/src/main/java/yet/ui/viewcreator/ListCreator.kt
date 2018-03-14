package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import yet.theme.Colors
import yet.ui.ext.genId
import yet.ui.res.Img

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//List View
fun <P : ViewGroup.LayoutParams> ViewGroup.listView(param: P, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.listView(index: Int, param: P, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun <P : ViewGroup.LayoutParams> ViewGroup.listViewBefore(ankor: View, param: P, block: ListView.() -> Unit): ListView {
	return this.listView(this.indexOfChild(ankor), param, block)
}


fun View.createListView(): ListView {
	return this.context.createListView()
}

fun Fragment.createListView(): ListView {
	return this.activity.createListView()
}

fun Context.createListView(): ListView {
	val lv = ListView(this).genId()
	lv.cacheColorHint = 0
	lv.selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
	return lv
}

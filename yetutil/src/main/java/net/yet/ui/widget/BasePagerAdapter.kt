package net.yet.ui.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import java.util.*

abstract class BasePagerAdapter<T> : PagerAdapter() {

	private var items: List<T> = ArrayList()

	fun setItems(ls: List<T>) {
		this.items = ls
		this.notifyDataSetChanged()
	}

	override fun getCount(): Int {
		return items.size
	}

	override fun isViewFromObject(view: View, `object`: Any): Boolean {
		return view === `object`
	}

	fun getItem(position: Int): T {
		return items[position]
	}

	abstract fun newView(context: Context, position: Int, item: T): View

	open fun onDestroyItem(position: Int, item: T, view: View) {
	}

	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val item = getItem(position)
		val view = newView(container.context, position, item)
		container.addView(view)
		return view
	}

	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		val v = `object` as View
		container.removeView(v)
		onDestroyItem(position, getItem(position), v)
	}
}

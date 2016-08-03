package net.yet.ui.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.View
import net.yet.ui.ext.genId

abstract class AbstractViewPager<T>(context: Context) : ViewPager(context) {
	private var adapter = object : BasePagerAdapter<T>() {

		override fun newView(context: Context, position: Int, item: T): View {
			return this@AbstractViewPager.newView(context, position, item)
		}

		override fun onDestroyItem(position: Int, item: T, view: View) {
			this@AbstractViewPager.onDestroyItem(position, item, view)
		}
	}

	init {
		genId()
		this.setAdapter(adapter)
		this.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
			override fun onPageSelected(position: Int) {
				val item = getItem(position)
				this@AbstractViewPager.onPageSelected(position, item)
			}
		})
	}

	abstract fun onPageSelected(position: Int, item: T)

	fun setItems(items: List<T>) {
		adapter.setItems(items)
	}

	val count: Int
		get() = adapter.count

	fun getItem(position: Int): T {
		return adapter.getItem(position)
	}

	protected abstract fun newView(context: Context, position: Int, item: T): View

	protected fun onDestroyItem(position: Int, item: T, view: View) {
	}


}

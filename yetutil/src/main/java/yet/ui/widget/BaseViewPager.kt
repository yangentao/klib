package yet.ui.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.*
import yet.ui.ext.genId

abstract class BaseViewPager<T>(context: Context) : ViewPager(context) {

	private val gd: GestureDetector

	var onPageClick: (Int) -> Unit = {

	}
	var onLastPageClick: (Int) -> Unit = {

	}
	private val adapter = object : BasePagerAdapter<T>() {

		override fun newView(context: Context, position: Int, item: T): View {
			return this@BaseViewPager.newView(context, position, item)
		}

		override fun onDestroyItem(position: Int, item: T, view: View) {
			this@BaseViewPager.onDestroyItem(position, item, view)

		}

	}

	init {
		genId()
		gd = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

			override fun onSingleTapUp(e: MotionEvent): Boolean {
				val n = currentItem
				onPageClick(n)
				if (n == count - 1) {
					onLastPageClick(n)
				}
				return false
			}

		})
		this.setAdapter(adapter)
	}

	override fun onTouchEvent(ev: MotionEvent): Boolean {
		gd.onTouchEvent(ev)
		return super.onTouchEvent(ev)
	}


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

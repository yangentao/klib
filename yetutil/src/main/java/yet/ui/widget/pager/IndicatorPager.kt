package yet.ui.widget.pager

import android.content.Context
import android.database.DataSetObserver
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import yet.ext.AGRAY
import yet.ext.GRAY
import yet.ui.ext.*
import yet.ui.res.D
import yet.ui.res.Shapes
import yet.ui.res.sized
import yet.ui.viewcreator.createImageView
import yet.ui.viewcreator.imageView
import yet.ui.viewcreator.linearHor

class IndicatorPager(context: Context) : RelativeLayout(context) {
	val pointSize: Int = 8
	val adapter = ViewPagerAdapter()
	val viewPager = ViewPager(context).genId()
	val indicatorLayout: LinearLayout

	var onNewView: (Context, Int) -> View = { c, _ ->
		val v = c.createImageView()
		v.scaleCenterCrop()
		v
	}
	var onDestroyItem: (View, Int) -> Unit = { _, _ -> }

	var onPageClick: (View, Int) -> Unit = { _, _ -> }

	init {
		genId()
		addView(viewPager, RParam.WidthFill.HeightWrap)
		indicatorLayout = linearHor(RParam.Wrap.CenterHorizontal.ParentBottom) {
			padding(10)
		}


		adapter.onNewView = { c, n ->
			this@IndicatorPager.onNewView(c, n)
		}
		adapter.onPageClick = { v, n ->
			this@IndicatorPager.onPageClick(v, n)
		}
		adapter.onDestroyItem = { v, n ->
			this@IndicatorPager.onDestroyItem(v, n)
		}

		adapter.registerDataSetObserver(object : DataSetObserver() {
			override fun onChanged() {
				bind()
			}

			override fun onInvalidated() {

			}
		})
		viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
			override fun onPageScrollStateChanged(state: Int) {

			}

			override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
			}

			override fun onPageSelected(position: Int) {
				selectIndicator(position)
			}
		})
		viewPager.adapter = adapter

	}

	private fun bind() {
		indicatorLayout.removeAllViews()
		for (i in 0 until adapter.count) {
			indicatorLayout.imageView(LParam.size(pointSize).margins(pointSize / 2, 0, pointSize / 2, 0)) {
				val d1 = Shapes.oval { fillColor = AGRAY(180, 150) }
				val d2 = Shapes.oval { fillColor = GRAY(255) }
				val d = D.light(d1, d2).sized(pointSize)
				setImageDrawable(d)
				scaleCenterInside()
			}
		}
		if (adapter.count > 0) {
			selectIndicator(getCurrentPage())
		}
	}

	fun setCurrentPage(n: Int, smooth: Boolean) {
		viewPager.setCurrentItem(n, smooth)
	}

	fun getCurrentPage(): Int {
		return viewPager.currentItem
	}

	fun setDrawableItems(ls: List<Drawable>) {
		setImageItems(ls) { v, item ->
			v.setImageDrawable(item as Drawable)
		}
	}

	fun setImageResItems(ls: List<Int>) {
		setImageItems(ls) { v, item ->
			v.setImageResource(item as Int)
		}
	}


	fun setImageItems(ls: List<Any>, block: (ImageView, Any) -> Unit) {
		onNewView = { c, p ->
			val v = c.createImageView()
			v.scaleCenterCrop()
			block(v, getItem(p))
			v
		}
		setItems(ls)
	}

	fun setViewItems(ls: List<View>) {
		onNewView = { c, p ->
			getItem(p) as View
		}
		setItems(ls)
	}

	fun setItems(ls: List<Any>) {
		adapter.setItems(ls)
	}

	fun getCount(): Int {
		return adapter.count
	}

	fun getItem(position: Int): Any {
		return adapter.getItem(position)
	}

	private fun selectIndicator(p: Int) {
		for (i in 0 until indicatorLayout.childCount) {
			indicatorLayout.getChildAt(i).isSelected = i == p
		}
	}
}


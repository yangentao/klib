package yet.ui.grid

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.TextView
import yet.ui.ext.*
import yet.ui.list.AnyAdapter
import yet.ui.res.D
import yet.ui.res.limited
import yet.util.fore

/**
 * Created by entaoyang@163.com on 2016-08-27.
 */

open class SimpleGridView(context: Context) : LineGridView(context) {
	var heightMost = false
	var autoColumn = false


	//dp
	var preferColumnWidth: Int = 64

	val anyAdapter = AnyAdapter()


	var onItemClick: (item: Any) -> Unit = {}

	var onLayoutChanged: (w: Int, h: Int) -> Unit = { w, h -> }


	init {
		genId()
		padding(10)
		backColorWhite()
		numColumns = 3
		anyAdapter.onNewView = { c, n ->
			val v = TextView(c)
			v.textSizeD()
			v.textColorMinor()
			v.gravityCenter()
			v

		}
		super.setAdapter(anyAdapter)
		this.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			val s = anyAdapter.getItem(pos)
			onItemClick(s)
		}
	}

	fun bindRes(imgSize: Int, block: (Any) -> Pair<String, Int>) {
		anyAdapter.onBindView = { v, p ->
			val p = block.invoke(getItem(p))
			val iv = v as TextView
			iv.text = p.first
			iv.topImage(D.res(p.second).limited(imgSize), 0)
		}
	}

	fun bindImage(imgSize: Int, block: (Any) -> Pair<String, Drawable>) {
		anyAdapter.onBindView = { v, p ->
			val p = block.invoke(getItem(p))
			val iv = v as TextView
			iv.text = p.first
			iv.topImage(p.second.limited(imgSize), 0)
		}
	}

	fun setPairsRes(imgSize: Int, items: List<Pair<String, Int>>) {
		anyAdapter.onBindView = { v, p ->
			val item = getItem(p) as Pair<String, Int>
			val iv = v as TextView
			iv.text = item.first
			iv.topImage(D.res(item.second).limited(imgSize), 0)
		}
		anyAdapter.setItems(items)
	}

	fun setPairs(imgSize: Int, items: List<Pair<String, Drawable>>) {
		anyAdapter.onBindView = { v, p ->
			val item = getItem(p) as Pair<String, Drawable>
			val iv = v as TextView
			iv.text = item.first
			iv.topImage(item.second.limited(imgSize), 0)
		}
		anyAdapter.setItems(items)
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}

	fun getItem(pos: Int): Any {
		return anyAdapter.getItem(pos)
	}

	val itemCount: Int
		get() {
			return anyAdapter.count
		}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		super.onLayout(changed, l, t, r, b)
		if (changed) {
			val newWidth = r - l
			if (autoColumn && preferColumnWidth > 0) {
				val ww = newWidth - this.paddingLeft - this.paddingRight
				var cols = (ww + this.horizontalSpacing) / (dp(preferColumnWidth) + this.horizontalSpacing)
				if (cols < 1) {
					cols = 1
				}
				fore {
					this.numColumns = cols
				}
			}

			onLayoutChanged(newWidth, b - t)
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		if (!heightMost) {
			return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		}
		val heightSpec: Int = if (layoutParams.height == AbsListView.LayoutParams.WRAP_CONTENT) {
			View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
		} else {
			heightMeasureSpec
		}

		super.onMeasure(widthMeasureSpec, heightSpec)
	}
}
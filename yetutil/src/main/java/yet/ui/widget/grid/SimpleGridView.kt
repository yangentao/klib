package yet.ui.widget.grid

import android.content.Context
import android.view.View
import android.widget.*
import yet.ui.ext.*
import yet.ui.widget.listview.SimpleBaseAdapter
import yet.util.fore

/**
 * Created by entaoyang@163.com on 2016-08-27.
 */

open class SimpleGridView<T>(context: Context) : GridView(context) {
	var heightMost = false
	var autoColumn = false

	//dp
	var preferColumnWidth: Int = 64

	val myAdapter = SimpleBaseAdapter<T>()
	var onNewView: (context: Context, position: Int) -> View
		get() {
			return myAdapter.onNewView
		}
		set(value) {
			myAdapter.onNewView = value
		}
	var onBindView: (itemView: View, item: T, position: Int) -> Unit
		get() {
			return myAdapter.onBindView
		}
		set(value) {
			myAdapter.onBindView = value
		}

	var onItemClick: (item: T) -> Unit = {}

	var onLayoutChanged: (w: Int, h: Int) -> Unit = { w, h -> }


	init {
		genId()
		padding(10)
		numColumns = 3
		verticalSpacing = dp(10)
		super.setAdapter(myAdapter)
		this.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			val s = myAdapter.getItem(pos)
			onItemClick(s)
		}
	}

	@Suppress("UNCHECKED_CAST")
	fun setItems(items: List<T>) {
		myAdapter.setItems(items)
	}

	@Suppress("UNCHECKED_CAST")
	fun getItem(pos: Int): T {
		return myAdapter.getItem(pos)
	}

	val itemCount: Int
		get() {
			return myAdapter.count
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
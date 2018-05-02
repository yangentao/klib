package yet.ui.grid

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.*
import yet.ui.ext.*
import yet.ui.list.AnyAdapter
import yet.ui.res.sized

/**
 * Created by entaoyang@163.com on 2016-08-27.
 */

open class GridPanel(context: Context) : GridView(context) {

	var onNewCallback: (view: GridItemView) -> Unit = {}
	var onBindCallback: (view: GridItemView, item: Any) -> Unit = { v, item ->
		v.setValues("Item", ColorDrawable(Color.GREEN).sized(60, 60))
	}
	var onItemClickCallback: (Any) -> Unit = {}

	var heightMost = false
	val anyAdapter = AnyAdapter()

	init {
		genId()
		padding(10)
		numColumns = 3
		verticalSpacing = dp(10)

		anyAdapter.onBindView = { v, p ->
			this@GridPanel.onBindView(v as GridItemView, getItem(p))
		}
		anyAdapter.onNewView = { c, p ->
			this@GridPanel.onNewView(c)
		}

		this.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			val s = adapter.getItem(pos)
			onItemClick(s)
		}
		super.setAdapter(anyAdapter)
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}

	fun getItem(pos: Int): Any {
		return anyAdapter.getItem(pos)
	}

	val itemCount: Int
		get() {
			return adapter.count
		}

	open fun onItemClick(item: Any) {
		onItemClickCallback(item)
	}

	open fun onBindView(view: GridItemView, item: Any) {
		onBindCallback(view, item)
	}

	open fun onNewView(context: Context): GridItemView {
		val v = GridItemView(context)
		onNewCallback(v)
		return v
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
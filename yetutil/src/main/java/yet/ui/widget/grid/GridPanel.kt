package yet.ui.widget.grid

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.GridView
import yet.ui.ext.dp
import yet.ui.ext.genId
import yet.ui.ext.padding
import yet.ui.res.sized
import yet.ui.widget.listview.XBaseAdapter

/**
 * Created by entaoyang@163.com on 2016-08-27.
 */

open class GridPanel<T>(context: Context) : GridView(context) {

	var onNewCallback: (view: GridItemView) -> Unit = {}
	var onBindCallback: (view: GridItemView, item: T) -> Unit = { v, item ->
		v.setValues("Item", ColorDrawable(Color.GREEN).sized(60, 60))
	}
	var onItemClickCallback: (T) -> Unit = {}

	var heightMost = false

	init {
		genId()
		padding(10)
		numColumns = 3
		verticalSpacing = dp(10)
		val adapter = object : XBaseAdapter<T>() {
			override fun bindView(position: Int, itemView: View, item: T) {
				onBindView(itemView as GridItemView, item)
			}

			override fun newView(context: Context, position: Int): View {
				return onNewView(context)
			}

		}
		this.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			val s = adapter.getItem(pos)
			onItemClick(s)
		}
		super.setAdapter(adapter)
	}

	@Suppress("UNCHECKED_CAST")
	fun setItems(items: List<T>) {
		(adapter as XBaseAdapter<T>).setItems(items)
	}

	@Suppress("UNCHECKED_CAST")
	fun getItem(pos: Int): T {
		return (adapter as XBaseAdapter<T>).getItem(pos)
	}

	val itemCount: Int get() {
		return adapter.count
	}

	open fun onItemClick(item: T) {
		onItemClickCallback(item)
	}

	open fun onBindView(view: GridItemView, item: T) {
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
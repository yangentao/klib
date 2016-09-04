package net.yet.weatherapp.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import net.yet.ext.size
import net.yet.theme.Colors
import net.yet.ui.ext.genId
import net.yet.ui.ext.padding
import net.yet.ui.ext.textColor
import net.yet.ui.ext.textSizeC
import net.yet.ui.widget.listview.TypedAdapter

/**
 * Created by entaoyang@163.com on 2016-08-27.
 */

open class GridPanel<T>(context: Context) : GridView(context) {
	lateinit var adapter: TypedAdapter<T>

	var onNewCallback: (view: GridItemView) -> Unit = {}
	var onBindCallback: (view: GridItemView, item: T) -> Unit = { v, item ->
		v.setValues("Item", ColorDrawable(Color.GREEN).size(60, 60))
	}
	var onItemClickCallback: (T) -> Unit = {}

	init {
		genId()
		padding(10)
		numColumns = 4
		adapter = object : TypedAdapter<T>() {
			override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int) {
				onBindView(itemView as GridItemView, item)
			}

			override fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View {
				return onNewView(context)
			}

		}
		this.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			val s = adapter.getItem(pos)
			onItemClick(s)
		}
		setAdapter(adapter)
	}

	fun setItems(items: List<T>) {
		adapter.setItems(items)
	}

	fun getItem(pos: Int): T {
		return adapter.getItem(pos)
	}

	val itemCount: Int get() {
		return adapter.count
	}

	fun onItemClick(item: T) {
		onItemClickCallback(item)
	}

	fun onBindView(view: GridItemView, item: T) {
		onBindCallback(view, item)
	}

	fun onNewView(context: Context): GridItemView {
		val v = GridItemView(context)
		v.textColor(Colors.color("#ccc")).textSizeC()
		onNewCallback(v)
		return v
	}
}
package net.yet.weatherapp.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.LinearLayout
import net.yet.theme.Space
import net.yet.ui.ext.*
import net.yet.ui.page.TitledPage
import net.yet.ui.widget.listview.TypedAdapter

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */

abstract class GridPage<T> : TitledPage() {
	lateinit var gridView: GridView
	lateinit var adapter: TypedAdapter<T>

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		gridView = GridView(context)
		gridView.numColumns = 2
		gridView.padding(Space.Normal)
		gridView.horizontalSpacing = dp(2)
		gridView.verticalSpacing = dp(2)
		contentView.addViewParam(gridView) {
			widthFill().height(0).weight(1)
		}

		adapter = object : TypedAdapter<T>() {
			override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int) {
				var view = itemView as GridItemView
				onBindItemView(view, item)

			}

			override fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View {
				return GridItemView(context)
			}

			override fun onRequestItems(): List<T> {
				return this@GridPage.onRequestItems()
			}

		}
		gridView.adapter = adapter
		gridView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			onItemClick(adapter.getItem(pos))
		}

	}

	open fun onItemClick(item:T) {

	}

	abstract fun onBindItemView(view: GridItemView, item: T)

	fun requestItems() {
		adapter.requestItems()
	}

	open fun onRequestItems(): List<T> {
		return emptyList()
	}
}
package yet.ui.widget.grid

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import yet.ui.ext.*
import yet.ui.page.TitledPage
import yet.ui.widget.listview.XBaseAdapter

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */

abstract class GridPage<T> : TitledPage() {
	lateinit var gridView: LineGridView
	lateinit var adapter: XBaseAdapter<T>

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		gridView = LineGridView(context)
		gridView.numColumns = 2
		gridView.horizontalSpacing = 0
		gridView.verticalSpacing = 0
		contentView.addViewParam(gridView) {
			widthFill().height(0).weight(1)
		}

		adapter = object : XBaseAdapter<T>() {
			override fun bindView(position: Int, itemView: View, item: T) {
				var view = itemView as GridItemView
				onBindItemView(view, item)

			}

			override fun newView(context: Context, position: Int): View {
				val v = GridItemView(context)
				v.padding(10)
				return v
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

	open fun setItems(items: List<T>) {
		adapter.setItems(items)
	}

	open fun setItems(vararg items: T) {
		adapter.setItemArray(*items)
	}

	open fun onItemClick(item: T) {

	}


	abstract fun onBindItemView(view: GridItemView, item: T)

	fun requestItems() {
		adapter.requestItems()
	}

	open fun onRequestItems(): List<T> {
		return emptyList()
	}
}
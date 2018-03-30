package yet.ui.widget.listview

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ListView
import yet.theme.Colors
import yet.ui.res.D

/**
 * 包含Adapter的listView
 * 调用requestItems来请求数据

 * @param
 */
abstract class AdapterListView<T>(context: Context) : ListView(context) {

	protected var adapter: XBaseAdapter<T> = object : XBaseAdapter<T>() {

		override fun bindView(position: Int, itemView: View, item: T) {
			this@AdapterListView.bindView(position, itemView, item)
		}

		override fun newView(context: Context, position: Int): View {
			return this@AdapterListView.newView(context, position)
		}

		override fun getItemViewType(position: Int): Int {
			return this@AdapterListView.getItemViewType(position)
		}

		override fun getViewTypeCount(): Int {
			return this@AdapterListView.viewTypeCount
		}

		override fun onRequestItems(): List<T> {
			return this@AdapterListView.onRequestItems()
		}

	}

	init {
		cacheColorHint = 0
		selector = D.lightColor(Color.TRANSPARENT, Colors.Fade)
		onCreateHeaderFooter(context)
		setAdapter(adapter)
	}

	fun setItems(items: List<T>) {
		adapter.setItems(items)
	}

	fun getItem(position: Int): T {
		return adapter.getItem(position)
	}

	protected fun onCreateHeaderFooter(context: Context) {

	}

	fun requestItems() {
		adapter.requestItems()
	}

	open protected fun onRequestItems(): List<T> {
		return emptyList()
	}

	abstract fun newView(context: Context, position: Int): View

	abstract fun bindView(position: Int, itemView: View, item: T)

	open fun getItemViewType(position: Int): Int {
		return 0
	}

	open val viewTypeCount: Int
		get() = 1


}

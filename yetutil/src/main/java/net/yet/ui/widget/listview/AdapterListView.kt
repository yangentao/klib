package net.yet.ui.widget.listview

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import net.yet.theme.Colors
import net.yet.ui.res.Img

/**
 * 包含Adapter的listView
 * 调用requestItems来请求数据

 * @param
 */
abstract class AdapterListView<T>(context: Context) : ListView(context), ListViewClickListener, ListViewLongClickListener {

	protected var adapter: TypedAdapter<T> = object : TypedAdapter<T>() {

		override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int) {
			val view = unpackBindView(position, itemView, parent, item)
			this@AdapterListView.bindView(position, view, parent, item)
		}

		override fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View {
			val view = this@AdapterListView.newView(context, position, parent)
			return packNewView(context, view, position, parent)
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
		selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
		onCreateHeaderFooter(context)
		setAdapter(adapter)
		ListViewUtil.addClick(this, this)
		ListViewUtil.addLongClick(this, this)
		onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			if (onInterceptItemClick(parent, view, position, id)) {
				return@OnItemClickListener
			}
			ListViewUtil.click(this@AdapterListView, view, position, this@AdapterListView)
		}
		onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
			if (onInterceptItemLongClick(parent, view, position, id)) {
				return@OnItemLongClickListener true
			}
			ListViewUtil.longClick(this@AdapterListView, view, position, this@AdapterListView)
		}
	}

	protected fun onInterceptItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
		return false
	}

	protected fun onInterceptItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
		return false
	}

	protected fun packNewView(context: Context, view: View, position: Int, parent: ViewGroup): View {
		return view
	}

	protected fun unpackBindView(position: Int, itemView: View, parent: ViewGroup, item: T): View {
		return itemView
	}

	protected fun onCreateHeaderFooter(context: Context) {

	}

	fun requestItems() {
		adapter.requestItems()
	}

	protected abstract fun onRequestItems(): List<T>

	abstract fun newView(context: Context, position: Int, parent: ViewGroup): View

	abstract fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T)

	fun getItemViewType(position: Int): Int {
		return 0
	}

	val viewTypeCount: Int
		get() = 1

	override fun onItemClickAdapter(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClickHeader(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClickFooter(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClick(listView: ListView, view: View, position: Int) {

	}

	override fun onItemLongClickAdapter(listView: ListView, view: View, position: Int): Boolean {
		return false
	}

	override fun onItemLongClickHeader(listView: ListView, view: View, position: Int): Boolean {
		return false
	}

	override fun onItemLongClickFooter(listView: ListView, view: View, position: Int): Boolean {
		return false
	}

	override fun onItemLongClick(listView: ListView, view: View, position: Int): Boolean {
		return false
	}
}

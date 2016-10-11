package net.yet.ui.page

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.*
import net.yet.ui.ext.*
import net.yet.ui.widget.listview.ListViewClickListener
import net.yet.ui.widget.listview.ListViewLongClickListener
import net.yet.ui.widget.listview.ListViewUtil
import net.yet.ui.widget.listview.TypedAdapter
import net.yet.util.TaskUtil
import java.util.*

/**
 * @param
 */
abstract class ListPage<T> : TitledPage(), ListViewClickListener, ListViewLongClickListener {

	val adapter: TypedAdapter<T> = object : TypedAdapter<T>() {

		override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int) {
			val view = unpackBindView(position, itemView, parent, item)
			this@ListPage.bindView(position, view, parent, item)
		}

		override fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View {
			val view = this@ListPage.newView(context, position, parent, getItem(position))
			return packNewView(context, view, position, parent)
		}

		override fun getItemViewType(position: Int): Int {
			return this@ListPage.getItemViewType(position)
		}

		override fun getViewTypeCount(): Int {
			return this@ListPage.viewTypeCount
		}

		override fun onRequestItems(): List<T> {
			return this@ListPage.onRequestItems()
		}

		override fun onItemsRefreshed() {
			this@ListPage.onItemsRefreshed()
		}

		override fun onOrderItems(items: ArrayList<T>): ArrayList<T> {
			return this@ListPage.onOrderItems(items)
		}
	}

	/**
	 * @return listView的上级View
	 */
	lateinit var listViewParent: RelativeLayout
	//		private set
	lateinit var listView: ListView
	//		private set
	lateinit var emptyView: TextView
//		private set

	fun setFilter(block: (T) -> Boolean) {
		adapter.setFilter(block)
	}

	fun resetFilter() {
		adapter.resetFilter()
	}

	fun refilter() {
		adapter.refilter()
	}

	open fun onOrderItems(items: ArrayList<T>): ArrayList<T> {
		return items
	}

	protected open fun onItemsRefreshed() {

	}


	protected open fun packNewView(context: Context, view: View, position: Int, parent: ViewGroup): View {
		return view
	}


	protected open fun unpackBindView(position: Int, itemView: View, parent: ViewGroup, item: T): View {
		return itemView
	}

	fun setEmptyText(text: String) {
		emptyView.text = text
	}

	open protected fun onAddListView(context: Context, parent: RelativeLayout, listView: ListView) {
		parent.addView(listView, relativeParam().fill())
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		listViewParent = createRelativeLayout()
		contentView.addView(listViewParent, linearParam().widthFill().height(0).weight(1f))
		listView = context.createListView()
		onAddListView(context, listViewParent, listView)
		createEmptyView(context)//listView添加进去之后再调用createEmptyView, 用到了getParent方法
		onCreateListViewHeaderFooter(context, listView)
		listView.adapter = adapter
		listView.adapter.registerDataSetObserver(object : DataSetObserver() {
			override fun onChanged() {
				TaskUtil.fore(Runnable { onAdapterDataChanged() })
			}
		})
		listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			val headCount = listView.headerViewsCount
			if (position >= headCount && position < headCount + adapter.count) {
				if (onInterceptItemClick(parent, view, position - headCount, id)) {
					return@OnItemClickListener
				}
			}
			ListViewUtil.click(listView, view, position, this@ListPage)
		}
		listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
			val headCount = listView.headerViewsCount
			if (position >= headCount && position < headCount + adapter.count) {
				if (onInterceptItemLongClick(parent, view, position - headCount, id)) {
					return@OnItemLongClickListener true
				}
			}
			ListViewUtil.longClick(listView, view, position, this@ListPage)
		}

		contentView.backColorPage()
		listViewParent.backColorWhite()
	}

	private fun createEmptyView(context: Context) {
		emptyView = TextView(context)
		layoutParam().fill().set(emptyView)
		emptyView.gravityCenter().textSizeB().gone()
		(listView.parent as ViewGroup).addView(emptyView)
		listView.emptyView = emptyView
	}

	protected open fun onCreateListViewHeaderFooter(context: Context, listView: ListView) {
	}

	protected open fun onAdapterDataChanged() {
	}

	protected open fun onInterceptItemClick(parent: AdapterView<*>, view: View, adapterPosition: Int, id: Long): Boolean {
		return false
	}

	protected open fun onInterceptItemLongClick(parent: AdapterView<*>, view: View, adapterPosition: Int, id: Long): Boolean {
		return false
	}

	protected fun listPos(adapterPos: Int): Int {
		return adapterPos + headerCount()
	}

	protected fun headerCount(): Int {
		return listView.headerViewsCount
	}

	protected fun adapterPos(listPos: Int): Int {
		return listPos - headerCount()
	}

	fun notifyDataSetChanged() {
		adapter.notifyDataSetChanged()
	}

	fun getItem(position: Int): T {
		return adapter.getItem(position)
	}

	val itemCount: Int
		get() = adapter.count


	abstract fun newView(context: Context, position: Int, parent: ViewGroup, item: T): View


	abstract fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T)

	open fun getItemViewType(position: Int): Int {
		return 0
	}

	open val viewTypeCount: Int
		get() = 1

	/**
	 * 请求刷新listview数据
	 */
	fun requestItems() {
		adapter.requestItems()
	}

	/**
	 * 调用requestItems后执行, 返回结果集

	 * @return
	 */
	protected abstract fun onRequestItems(): List<T>

	open fun onItemClickAdapter2(listView: ListView, view: View, position: Int, item: T) {

	}

	override fun onItemClickAdapter(listView: ListView, view: View, position: Int) {
		onItemClickAdapter2(listView, view, position, getItem(position))
	}

	override fun onItemClickHeader(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClickFooter(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClick(listView: ListView, view: View, position: Int) {

	}

	open fun onItemLongClickAdapter2(listView: ListView, view: View, position: Int, item: T): Boolean {
		return false
	}

	override fun onItemLongClickAdapter(listView: ListView, view: View, position: Int): Boolean {
		return onItemLongClickAdapter2(listView, view, position, getItem(position))
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

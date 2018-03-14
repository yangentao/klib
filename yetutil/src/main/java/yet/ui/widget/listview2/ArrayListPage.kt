package yet.ui.page

import android.content.Context
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.*
import yet.ui.ext.*
import yet.ui.viewcreator.relative
import yet.ui.viewcreator.createListView
import yet.ui.widget.listview.*
import yet.ui.widget.listview2.ArrayListAdapter
import yet.ui.widget.listview2.ModelTypes
import yet.util.TaskUtil

/**
 * @param
 */
abstract class ArrayListPage : TitledPage(), ListViewClickListener, ListViewLongClickListener {

	val adapter: ArrayListAdapter = object : ArrayListAdapter() {

		override fun newView(context: Context, position: Int): View {
			val view = this@ArrayListPage.newView(context, position)
			return packNewView(context, view, position)
		}

		override fun bindView(itemView: View, position: Int) {
			val view = unpackBindView(itemView, position)
			this@ArrayListPage.bindView(view, position)
		}

		override fun getItemViewType(position: Int): Int {
			return this@ArrayListPage.getItemViewType(position)
		}

		override fun getViewTypeCount(): Int {
			return this@ArrayListPage.getViewTypeCount()
		}

		override fun onRequestItems(): List<out Any> {
			return this@ArrayListPage.onRequestItems()
		}

		override fun onItemsRefreshed() {
			this@ArrayListPage.onItemsRefreshed()
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

	open var modelTypes: ModelTypes? = null

	protected open fun onItemsRefreshed() {

	}


	protected open fun packNewView(context: Context, view: View, position: Int): View {
		return view
	}


	protected open fun unpackBindView(itemView: View, position: Int): View {
		return itemView
	}

	fun setEmptyText(text: String) {
		emptyView.text = text
	}

	open protected fun onAddListView(context: Context, parent: RelativeLayout, listView: ListView) {
		parent.addView(listView, relativeParam().fill())
	}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		listViewParent = contentView.relative(linearParam().widthFill().height(0).weight(1f)) { }
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
			ListViewUtil.click(listView, view, position, this@ArrayListPage)
		}
		listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
			val headCount = listView.headerViewsCount
			if (position >= headCount && position < headCount + adapter.count) {
				if (onInterceptItemLongClick(parent, view, position - headCount, id)) {
					return@OnItemLongClickListener true
				}
			}
			ListViewUtil.longClick(listView, view, position, this@ArrayListPage)
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

	fun getItem(position: Int): Any {
		return adapter.getItem(position)
	}

	val itemCount: Int
		get() = adapter.count


	abstract fun newView(context: Context, position: Int): View


	abstract fun bindView(itemView: View, position: Int)

	open fun getItemViewType(position: Int): Int {
		return modelTypes?.viewTypeOf(getItem(position)) ?: 0
	}

	open fun getViewTypeCount(): Int {
		return modelTypes?.count ?: 1
	}

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
	protected abstract fun onRequestItems(): List<out Any>

	open fun onItemClickAdapter2(listView: ListView, view: View, position: Int, item: Any) {

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

	open fun onItemLongClickAdapter2(listView: ListView, view: View, position: Int, item: Any): Boolean {
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

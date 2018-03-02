package yet.ui.page.select

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import net.yet.R
import yet.theme.Str
import yet.ui.ext.*
import yet.ui.page.TitledPage
import yet.ui.widget.Action
import yet.ui.widget.TitleBar
import yet.ui.widget.listview.*
import yet.ui.widget.listview.itemview.CheckItemView
import yet.util.JsonUtil

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class SelectPage<T, V : View> : TitledPage(), ListViewClickListener {
	protected var title: String = Str.SELECT
	private var multiSelect: Boolean = false
	private var limit: Int = -1

	protected lateinit var listView: ListView
	protected var selAllAction: Action? = null
	protected var doneAction: Action? = null


	var onSelect: ((position: Int, item: T) -> Unit)? = null
	var onSelectIndex: ((position: Int) -> Unit)? = null
	var onSelectValue: ((item: T) -> Unit)? = null

	var onMultiSelectIndices: ((indexs: Set<Int>) -> Unit)? = null
	var onMultiSelectValues: ((values: List<T>) -> Unit)? = null

	private val adapter = object : SelectFilterAdapter<T>() {
		override fun bindView(position: Int, itemView: View, item: T) {
			if (multiSelect) {
				val checkItemView = itemView as CheckItemView
				checkItemView.isChecked = isSelected(position)
				onBindItemView(checkItemView.itemView as V, item)

			} else {
				onBindItemView(itemView as V, item)
			}
		}

		override fun newView(context: Context, position: Int): View {
			val v = onNewItemView(context, this.getItem(position))
			if (multiSelect) {
				val checkItemView = CheckItemView(context, v, null)
				checkItemView.setCheckboxMarginRight(15)
				checkItemView.isCheckModel = true
				return checkItemView
			} else {
				return v
			}
		}

		override fun accept(item: T, searchText: String): Boolean {
			return this@SelectPage.accept(item, searchText)
		}
	}

	fun limit(maxSelCount: Int) {
		limit = maxSelCount
	}

	fun enableSearch(enable: Boolean) {
		if (enable) {
			searchEdit?.visiable()
		} else {
			searchEdit?.gone()
		}
	}

	open fun accept(item: T, text: String): Boolean {
		return JsonUtil.toJson(item)?.toString()?.contains(text) ?: false
	}


	fun multiMode() {
		multiSelect = true
	}


	override fun preCreatePage() {
		super.preCreatePage()
		withSearchEdit()
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar.showBack()

		if (limit > 0) {
			titleBar.title = title + "(最多${limit}个)"
		} else {
			titleBar.title = title
		}
		listView = contentView.addListView(lParam().widthFill().heightDp(0).weight(1)) {
		}
		listView.adapter = adapter

		ListViewUtil.addClick(listView, this)

		if (multiSelect) {
			selAllAction = titleBar.addAction(Str.SEL_ALL).icon(R.drawable.yet_sel_all)
			doneAction = titleBar.addAction(Str.DONE)
		}
		searchEdit?.gone()
	}

	override fun onSearchTextChanged(s: String) {
		adapter.filter(s)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkSellAllState()
	}

	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (action.isTag(Str.SEL_ALL)) {
			if (adapter.isAllSelected) {
				deselectAll()
			} else {
				selectAll()
			}
			notifyDataSetChanged()
			checkSellAllState()
			return
		}
		if (action.isTag(Str.DONE)) {
			finish()
			if (adapter.selectedCount > 0) {
				onMultiSelectIndices?.invoke(adapter.selectedIndexs)
				onMultiSelectValues?.invoke(adapter.selectedItems)
			}
			return
		}
	}

	fun notifyDataSetChanged() {
		adapter.notifyDataSetChanged()
	}

	private fun checkSellAllState() {
		if (multiSelect) {
			if (adapter.isAllSelected) {
				selAllAction?.icon(R.drawable.yet_sel_all2)
			} else {
				selAllAction?.icon(R.drawable.yet_sel_all)
			}
			val n = adapter.selectedCount
			if (n == 0) {
				doneAction?.label(Str.DONE)
			} else {
				doneAction?.label(Str.DONE + "(" + n + ")")
			}
			titleBar.commit()
		}
	}

	override fun onItemClickAdapter(listView: ListView, view: View, position: Int) {
		if (multiSelect) {
			val checkItemView = view as CheckItemView
			checkItemView.toggle()
			val c = checkItemView.isChecked
			if (c) {
				if (limit > 0 && adapter.selectedCount >= limit) {
					checkItemView.isChecked = false
					toast("不能选择更多了")
					return
				}
			}
			adapter.selectPosition(position, c)
			checkSellAllState()
		} else {
			val item = adapter.getItem(position)
			finish()
			onSelect?.invoke(position, item)
			onSelectIndex?.invoke(position)
			onSelectValue?.invoke(item)
		}
	}

	override fun onItemClickHeader(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClickFooter(listView: ListView, view: View, position: Int) {

	}

	override fun onItemClick(listView: ListView, view: View, position: Int) {

	}

	fun selectIndex(vararg indexArr: Int) {
		for (index in indexArr) {
			adapter.selectPosition(index, true)
		}
	}

	fun selectItem(vararg itemArr: T) {
		for (item in itemArr) {
			adapter.selectItem(item)

		}
	}

	fun selectItem(ls: Collection<T>) {
		for (item in ls) {
			adapter.selectItem(item)
		}
	}

	fun deselectAll() {
		adapter.clearSelect()
	}

	fun selectAll() {
		adapter.selectAll()
	}

	val selectItems: List<T>
		get() {
			return adapter.selectedItems
		}

	fun title(title: String) {
		this.title = title
		if (pageReady) {
			titleBar.title = title
		}
	}

	fun setItems(data: List<T>) {
		adapter.setItems(data)
		checkSellAllState()
	}


	fun getItem(index: Int): T {
		return adapter.getItem(index)
	}


	abstract fun onNewItemView(context: Context, item: T): V

	abstract fun onBindItemView(itemView: V, item: T)


}

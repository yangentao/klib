package net.yet.ui.page.select

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import net.yet.R
import net.yet.theme.Str
import net.yet.ui.ext.*
import net.yet.ui.page.TitledPage
import net.yet.ui.widget.Action
import net.yet.ui.widget.TitleBar
import net.yet.ui.widget.listview.ListViewClickListener
import net.yet.ui.widget.listview.ListViewUtil
import net.yet.ui.widget.listview.TypedAdapter
import net.yet.ui.widget.listview.itemview.CheckItemView
import java.util.*

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class SelectPage<T, V : View> : TitledPage(), ListViewClickListener {
	protected var title:String = "选择"
	protected var multiSelect:Boolean = false
	protected val items: ArrayList<T> = ArrayList(64)
	protected val indexSet: MutableSet<Int> = TreeSet()
	protected lateinit  var listView: ListView
	protected var selAllAction: Action? = null
	protected var doneAction: Action? = null

	var onSelect:((position: Int, item: T)->Unit)? = null

	var onMultiSelect:((indexs: Set<Int>)->Unit)? = null

	private val adapter = object : TypedAdapter<T>() {
		override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int) {
			if (multiSelect) {
				val checkItemView = itemView as CheckItemView
				val c = indexSet.contains(position)
				checkItemView.isChecked = c
				onBindItemView(checkItemView.itemView as V, item)

			} else {
				onBindItemView(itemView as V, item)
			}
		}

		override fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View {
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
	}

	fun multiMode() {
		multiSelect = true
	}

	 override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.showBack()
		titleBar.title = title

		listView = context.createListView()
		listView.isVerticalScrollBarEnabled = true
		listView.isScrollbarFadingEnabled = true
		adapter.setItems(items)
		listView.adapter = adapter
		contentView.addViewParam(listView){
			widthFill().heightDp(0).weight(1)
		}

		ListViewUtil.addClick(listView, this)

		if (multiSelect) {
			selAllAction = titleBar.addAction(Str.SEL_ALL).icon(R.drawable.sel_all)
			doneAction = titleBar.addAction(Str.DONE)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkSellAllState()
	}

	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (action.isTag(Str.SEL_ALL)) {
			if (indexSet.size == items.size) {
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
			if (!indexSet.isEmpty()) {
				onMultiSelect?.invoke(indexSet)
			}
			return
		}
	}

	fun notifyDataSetChanged() {
		adapter.notifyDataSetChanged()
	}

	private fun checkSellAllState() {
		if (multiSelect) {
			if (items.size == indexSet.size) {
				selAllAction?.icon(R.drawable.sel_all2)
			} else {
				selAllAction?.icon(R.drawable.sel_all)
			}
			val n = indexSet.size
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
				indexSet.add(position)
			} else {
				indexSet.remove(position)
			}
			checkSellAllState()
		} else {
			val item = adapter.getItem(position)
			finish()
			onSelect?.invoke(position, item)
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
			indexSet.add(index)
		}
	}

	fun selectItem(vararg itemArr: T) {
		for (item in itemArr) {
			val index = items.indexOf(item)
			if (index >= 0) {
				indexSet.add(index)
			}
		}
	}

	fun selectItem(ls: Collection<T>) {
		for (item in ls) {
			val index = items.indexOf(item)
			if (index >= 0) {
				indexSet.add(index)
			}
		}
	}

	fun deselectAll() {
		indexSet.clear()
	}

	fun selectAll() {
		for (i in items.indices) {
			indexSet.add(i)
		}
	}

	val selectItems: List<T>
		get() {
			val ls = ArrayList<T>()
			for (n in indexSet) {
				ls.add(items[n])
			}
			return ls
		}

	fun title(title: String) {
		this.title = title
		if (pageReady) {
			titleBar.title = title
		}
	}

	fun setItems(data: List<T>) {
		items.clear()
		items.addAll(data)
	}

	fun addItems(data: Collection<T>) {
		items.addAll(data)
	}

	fun addItems(vararg data: T) {
		for (item in data) {
			items.add(item)
		}
	}

	fun getItem(index: Int): T {
		return items[index]
	}


	abstract fun onNewItemView(context: Context, item: T): V

	abstract fun onBindItemView(itemView: V, item: T)


}

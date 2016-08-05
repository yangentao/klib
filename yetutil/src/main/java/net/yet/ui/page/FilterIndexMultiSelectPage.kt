package net.yet.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import net.yet.theme.InputSize
import net.yet.ui.ext.*
import net.yet.ui.util.XTextWatcher
import net.yet.ui.widget.Action
import net.yet.ui.widget.IIndexable
import net.yet.ui.widget.ListIndexBar
import net.yet.ui.widget.TitleBar
import net.yet.util.Util
import java.util.*

@SuppressLint("UseSparseArrays")
abstract class FilterIndexMultiSelectPage<T : IIndexable> : CheckListPage<T>() {
	private val searchList = LinkedList<String>()
	protected lateinit var indexBar: ListIndexBar<T>
	private var allItems: List<T>? = null

	protected abstract fun onSelected(selList: List<T>)

	protected abstract val items: List<T>

	protected abstract fun onFilter(items: List<T>, text: String): List<T>

	protected abstract fun makeTagItem(tag: Char): T

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		val searchEdit = context.createEditText()
		contentView.addView(searchEdit, 0, linearParam().widthFill().heightDp(InputSize.EditHeight - 5).margins(10, 2, 10, 2))
		searchEdit.addTextChangedListener(object : XTextWatcher() {
			override fun afterTextChanged(text: String) {
				search(text)
			}
		})
		indexBar = object : ListIndexBar<T>(context, listViewParent, listView) {

			override fun makeTagItem(tag: Char): T {
				return this@FilterIndexMultiSelectPage.makeTagItem(tag)
			}
		}
		listViewParent.addView(indexBar, relativeParam().width(NAVBAR_WIDTH).heightFill().parentRight())

		setAutoEnterCheckModelOnLongClick(false)
		setCheckItemPaddingRight(NAVBAR_WIDTH)

		allItems = items
		requestItems()
		setMultiChoiceMode(true)

		titleBar.addAction(CANCEL)
		titleBar.addAction(DONE)

	}

	private fun search(s: String) {
		synchronized (searchList) {
			searchList.add(s)
		}
		requestItems()
	}

	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (action.isTag(CANCEL)) {
			finish()
			return
		}

		if (action.isTag(DONE)) {
			done()
			return
		}
		super.onTitleBarAction(bar, action)
	}

	private fun done() {
		val ls = checkedItems
		finish()
		onSelected(ls)
	}

	override fun onBackPressed(): Boolean {
		done()
		return true
	}

	override fun onTitleBarActionNav(bar: TitleBar, action: Action) {
		done()
	}

	protected open fun onCustomFilter(items: List<T>): List<T> {
		return items
	}

	override fun onRequestItems(): List<T> {
		var st: String? = null
		synchronized (searchList) {
			st = searchList.pollLast()
			searchList.clear()
		}
		var ls = if (Util.empty(st)) allItems else onFilter(allItems ?: emptyList(), st ?: "")
		ls = onCustomFilter(ls ?: emptyList())
		return processIndexBarItems(ls)
	}

	open protected val indexBarLimit: Int
		get() = 0

	protected fun processIndexBarItems(ls: List<T>): List<T> {
		return indexBar.processItems(ls, indexBarLimit, itemComparator)
	}

	open protected val itemComparator: Comparator<T>?
		get() = null

	companion object {
		private val CANCEL = "取消"
		private val DONE = "完成"
		const val NAVBAR_WIDTH = 40
	}

}

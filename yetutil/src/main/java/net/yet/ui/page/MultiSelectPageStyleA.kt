package net.yet.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout

import net.yet.ui.widget.Action
import net.yet.ui.widget.TitleBar

@SuppressLint("UseSparseArrays")
abstract class MultiSelectPageStyleA<T> : CheckListPage<T>() {
	private var allItems: List<T>? = null

	protected abstract fun onSelected(selList: List<T>)

	protected abstract val items: List<T>

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		setAutoEnterCheckModelOnLongClick(false)
		allItems = items
		requestItems()
		setMultiChoiceMode(true)

		titleBar.addActions(CANCLE, DONE)

	}

	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (action.isTag(CANCLE)) {
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

	override fun onRequestItems(): List<T> {
		return allItems ?: emptyList()
	}

	companion object {
		private val CANCLE = "取消"
		private val DONE = "完成"
	}

}

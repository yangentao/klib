package yet.ui.list.select

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import net.yet.R
import yet.theme.Str
import yet.ui.list.ListPage
import yet.ui.list.check.CheckView
import yet.ui.page.Cmd

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class SelectPage : ListPage() {
	protected var title: String = Str.SELECT
	var multiSelect: Boolean = false
	private var limit: Int = -1

	private var selAllAction: Cmd? = null
	private var doneAction: Cmd? = null


	var onResult: ((position: Int, item: Any) -> Unit)? = null
	var onResultIndex: ((position: Int) -> Unit)? = null
	var onResultValue: ((item: Any) -> Unit)? = null

	var onResultMultiIndices: ((indexs: Set<Int>) -> Unit)? = null
	var onResultMultiValues: ((values: List<Any>) -> Unit)? = null

	init {
		withSearchEdit = true
	}


	override fun packNewView(context: Context, view: View, position: Int): View {
		return if (multiSelect) {
			CheckView(context).bind(view)
		} else {
			view
		}
	}

	override fun unpackBindView(itemView: View, position: Int): View {
		return if (itemView is CheckView) {
			itemView.isChecked = anyAdapter.isChecked(position)
			itemView.view
		} else {
			itemView
		}
	}


	fun limit(maxSelCount: Int) {
		limit = maxSelCount
	}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		enablePullRefresh(false)
		titleBar {
			if (limit > 0) {
				title(title + "(最多${limit}个)")
			} else {
				title(title)
			}

			if (multiSelect) {
				selAllAction = actionImage(R.drawable.yet_sel_all)
				selAllAction?.onClick = {
					if (anyAdapter.isAllChecked) {
						deselectAll()
					} else {
						selectAll()
					}
					notifyDataSetChanged()
					checkSellAllState()
				}
				doneAction = actionText(Str.DONE)
				doneAction?.onClick = {
					finish()
					if (anyAdapter.checkedCount > 0) {
						onResultMultiIndices?.invoke(anyAdapter.checkedIndexs)
						onResultMultiValues?.invoke(anyAdapter.checkedItems)
					}
				}
			}
		}


	}

	override fun onSearchTextChanged(s: String) {
		anyAdapter.filter {
			s in it.toString()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		checkSellAllState()
	}


	private fun checkSellAllState() {
		if (multiSelect) {
			if (anyAdapter.isAllChecked) {
				selAllAction?.setImage(R.drawable.yet_sel_all2)
			} else {
				selAllAction?.setImage(R.drawable.yet_sel_all)
			}
			val n = anyAdapter.checkedCount
			if (n == 0) {
				doneAction?.setText(Str.DONE)
			} else {
				doneAction?.setText(Str.DONE + "(" + n + ")")
			}
		}
	}

	override fun onItemClickAdapter(view: View, item: Any, position: Int) {
		if (multiSelect) {
			val checkItemView = view as CheckView
			checkItemView.toggle()
			val c = checkItemView.isChecked
			if (c) {
				if (limit > 0 && anyAdapter.checkedCount >= limit) {
					checkItemView.isChecked = false
					toast("不能选择更多了")
					return
				}
			}
			anyAdapter.checkPosition(position, c)
			checkSellAllState()
		} else {
			finish()
			onResult?.invoke(position, item)
			onResultIndex?.invoke(position)
			onResultValue?.invoke(item)
		}
	}

	fun selectIndex(vararg indexArr: Int) {
		for (index in indexArr) {
			anyAdapter.checkPosition(index, true)
		}
	}

	fun selectItem(vararg itemArr: Any) {
		for (item in itemArr) {
			anyAdapter.checkItem(item)

		}
	}

	fun selectItem(ls: Collection<Any>) {
		for (item in ls) {
			anyAdapter.checkItem(item)
		}
	}

	fun deselectAll() {
		anyAdapter.clearChecked()
	}

	fun selectAll() {
		anyAdapter.checkAll()
	}

	val selectItems: List<Any>
		get() {
			return anyAdapter.checkedItems
		}

	override fun onItemsRefreshed() {
		super.onItemsRefreshed()
		checkSellAllState()
	}

}

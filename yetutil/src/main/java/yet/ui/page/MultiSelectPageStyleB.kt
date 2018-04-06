package yet.ui.page

import android.content.Context
import android.widget.LinearLayout
import yet.ui.widget.Action
import yet.ui.widget.BottomBar
import yet.ui.widget.TitleBar

/**

 */
abstract class MultiSelectPageStyleB<T> : CheckListPage<T>() {
	private var allItems: List<T>? = null

	protected abstract fun onSelected(selList: List<T>)

	protected abstract val items: List<T>

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		setMultiChoiceMode(true)
		allItems = items
		requestItems()
	}

	override fun onBottomBarCheckModeEnter(bar: BottomBar) {
		bar.addActions(CANCLE, DONE)
	}

	override fun onBackPressed(): Boolean {
		val ls = checkedItems
		finish()
		onSelected(ls)
		return true
	}

	override fun onTitleBarActionNav(bar: TitleBar, action: Action) {
		onBackPressed()
	}

	override fun onBottomBarCheckModelActionEmpty(bar: BottomBar, action: Action) {
		if (action.isLabel(CANCLE)) {
			finish()
			return
		}
	}

	override fun onBottomBarCheckModelActionResult(bar: BottomBar, action: Action, items: List<T>) {
		if (action.isLabel(CANCLE)) {
			finish()
			return
		}
		if (action.isTag(DONE)) {
			finish()
			onSelected(items)
			return
		}
	}

	override fun onRequestItems(): List<T> {
		return allItems ?: emptyList()
	}

	companion object {
		private val CANCLE = "取消"
		private val DONE = "完成"
	}

}

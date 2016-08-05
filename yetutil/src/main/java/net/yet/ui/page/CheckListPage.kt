package net.yet.ui.page

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.LinearLayout
import net.yet.theme.Str
import net.yet.ui.widget.Action
import net.yet.ui.widget.BottomBar
import net.yet.ui.widget.TitleBar
import net.yet.ui.widget.listview.itemview.CheckItemView
import net.yet.util.TaskUtil
import net.yet.util.Util
import java.util.*

/**
 * 使用HashSet来存储选中的项目, 因此, T应该重写hashCode和equals方法
 *
 *
 * 在有搜索框的可多选ListView中,  adapter的items是会改变的, 因此, 不能使用ListView自身存储的选中集合

 * @param
 */
abstract class CheckListPage<T> : ListPage<T>() {
	private var autoEnterCheckModel = false
	protected val selSet = HashSet<T>(64)
	private var checkItemPaddingRight = 0

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
	}

	//TODO 调用父类的pack/unpack方法
	override fun packNewView(context: Context, view: View, position: Int, parent: ViewGroup): View {
		if (isItemCheckable(position)) {
			val checkItemView = CheckItemView(context, view)
			checkItemView.isCheckModel = isMultiChoiceModel
			return checkItemView
		} else {
			return view
		}
	}

	override fun unpackBindView(position: Int, itemView: View, parent: ViewGroup, item: T): View {
		if (itemView is CheckItemView) {
			itemView.isCheckModel = isMultiChoiceModel
			itemView.setCheckboxMarginRight(checkItemPaddingRight)
			return super.unpackBindView(position, itemView.itemView, parent, item)
		}
		return super.unpackBindView(position, itemView, parent, item)
	}

	override fun onInterceptItemClick(parent: AdapterView<*>, view: View, adapterPosition: Int, id: Long): Boolean {
		if (isMultiChoiceModel) {
			if (!isItemCheckable(adapterPosition)) {
				setItemChecked(adapterPosition, false, false)
			}
			onListViewCheckStateChanged(adapterPosition, isListViewItemChecked(adapterPosition), true)
			return true
		}
		return super.onInterceptItemClick(parent, view, adapterPosition, id)
	}

	override fun onInterceptItemLongClick(parent: AdapterView<*>, view: View, adapterPosition: Int, id: Long): Boolean {
		if (isMultiChoiceModel) {
			if (isItemCheckable(adapterPosition)) {
				setItemChecked(adapterPosition, true, true)
			}
			return true
		}
		if (autoEnterCheckModel) {
			setMultiChoiceMode(true)
			TaskUtil.fore(Runnable { setItemChecked(adapterPosition, true, true) })
			return true
		}
		return super.onInterceptItemLongClick(parent, view, adapterPosition, id)
	}

	private fun onListViewCheckStateChanged(adapterPosition: Int, checked: Boolean, fire: Boolean) {
		if (checked) {
			selSet.add(adapter.getItem(adapterPosition))
		} else {
			selSet.remove(adapter.getItem(adapterPosition))
		}
		if (fire) {
			onCheckChanged()
		}
	}

	fun setItemChecked(adapterPosition: Int, checked: Boolean, fire: Boolean) {
		if (isItemCheckable(adapterPosition)) {
			listView.setItemChecked(adapterPosition + headerCount(), checked)
			onListViewCheckStateChanged(adapterPosition, checked, fire)
		}
	}

	open fun isItemCheckable(position: Int): Boolean {
		return true
	}

	val checkedCount: Int
		get() = selSet.size

	/**
	 * @return 选中的项目
	 */
	val checkedItems: List<T>
		get() = Util.asList(selSet)

	fun clearCheckedItems() {
		selSet.clear()
	}

	val listViewCheckedCount: Int
		get() {
			if (isMultiChoiceModel) {
				return listView.checkedItemCount
			}
			return 0
		}

	fun isListViewItemChecked(adapterPosition: Int): Boolean {
		return listView.isItemChecked(adapterPosition + headerCount())
	}

	fun setCheckItemPaddingRight(dp: Int) {
		this.checkItemPaddingRight = dp
		if (listView != null) {
			listView.invalidateViews()
		}
	}

	fun setMultiChoiceMode(choice: Boolean) {
		if (choice == isMultiChoiceModel) {
			return
		}
		if (choice) {
			selSet.clear()
			listView.clearChoices()
			listView.invalidateViews()
			val bbar = bottomBar
			bbar.pushMode(MODE_SELECT)
			onBottomBarCheckModeEnter(bbar)
			if (bbar.actionCount > 0) {
				bbar.commit()
				val tabBar = tabBar
				tabBar?.hide()
				bbar.show()
			}

			titleBar.pushMode(MODE_SELECT)
			titleBar.cleanActions()
			titleBar.showBack()
			titleBar.title = "选择"
			titleBar.addAction(Str.SEL_ALL)
			titleBar.commit()
			beforeEnterChoiceMode()
		} else {
			beforeLeaveChoiceMode()
			bottomBar.popMode()
			val tabBar = tabBar
			tabBar?.show()
			titleBar.popMode()
			bottomBar.hide()
		}
		var items: List<T>? = null
		if (!choice) {
			items = checkedItems
		}
		listView.choiceMode = if (choice) AbsListView.CHOICE_MODE_MULTIPLE else AbsListView.CHOICE_MODE_NONE
		if (choice) {
			afterEnterChoiceMode()
		} else {
			selSet.clear()
			afterLeaveChoiceMode(items ?: emptyList())
		}
		listView.invalidateViews()
	}

	fun selectAll() {
		for (i in 0..adapter.count - 1) {
			setItemChecked(i, true, false)
		}
		onCheckChanged()
	}

	fun unselectAll() {
		listView.clearChoices()
		listView.invalidateViews()
		for (i in 0..adapter.count - 1) {
			onListViewCheckStateChanged(i, false, false)
		}
		onCheckChanged()
	}

	override fun onItemsRefreshed() {
		super.onItemsRefreshed()
		listView.clearChoices()
		for (i in 0..adapter.count - 1) {
			val item = adapter.getItem(i)
			if (selSet.contains(item)) {
				listView.setItemChecked(i + headerCount(), true)
			}
		}
		listView.invalidateViews()
	}

	override fun onBackPressed(): Boolean {
		if (isMultiChoiceModel) {
			setMultiChoiceMode(false)
			return true
		}
		return super.onBackPressed()
	}

	override fun onTitleBarActionNav(bar: TitleBar, action: Action) {
		if (isMultiChoiceModel) {
			setMultiChoiceMode(false)
			return
		}
		super.onTitleBarActionNav(bar, action)
	}

	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (isMultiChoiceModel) {
			if (action.isLabel(Str.SEL_ALL)) {
				selectAll()
				action.label(Str.UNSEL_ALL)
			} else if (action.isLabel(Str.UNSEL_ALL)) {
				unselectAll()
				action.label(Str.SEL_ALL)
			}
			bar.commit()
			return
		}
		super.onTitleBarAction(bar, action)
	}

	protected fun onCheckChanged() {
		if (isMultiChoiceModel) {
			val bar = titleBar
			if (bar.isMode(MODE_SELECT)) {
				val count = selSet.size
				if (count > 0) {
					bar.title = "选择了($count)个"
				} else {
					bar.title = "选择"
				}
				bar.commit()
			}
		}
	}

	override fun onBottomBarAction(bar: BottomBar, action: Action) {
		if (isMultiChoiceModel) {
			val checkedModels = checkedItems
			if (checkedModels.size > 0) {
				onBottomBarCheckModelActionResult(bar, action, checkedModels)
				setMultiChoiceMode(false)
			} else {
				onBottomBarCheckModelActionEmpty(bar, action)
			}
		}
	}

	/**
	 * 进入选择模式时, bottombar添加按钮

	 * @param bar
	 */
	open fun onBottomBarCheckModeEnter(bar: BottomBar) {

	}

	open fun onBottomBarCheckModelActionResult(bar: BottomBar, action: Action, items: List<T>) {

	}

	open fun onBottomBarCheckModelActionEmpty(bar: BottomBar, action: Action) {

	}

	open fun beforeEnterChoiceMode() {
	}

	/**
	 */
	open fun beforeLeaveChoiceMode() {
	}

	open fun afterEnterChoiceMode() {
	}

	/**
	 * 此时, listview的状态已经改成了CHOICE_MODE_NONE.
	 */
	open fun afterLeaveChoiceMode(items: List<T>) {
	}

	val isMultiChoiceModel: Boolean
		get() = listView.choiceMode != AbsListView.CHOICE_MODE_NONE

	/**
	 * 是否长按进入选择模式

	 * @param autoEnter
	 */
	fun setAutoEnterCheckModelOnLongClick(autoEnter: Boolean) {
		this.autoEnterCheckModel = autoEnter
	}

	companion object {
		protected const val MODE_SELECT = "select"
	}

}

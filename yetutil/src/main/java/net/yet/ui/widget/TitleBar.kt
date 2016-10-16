package net.yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import net.yet.R
import net.yet.ext.notEmpty
import net.yet.ext.size
import net.yet.theme.Colors
import net.yet.ui.ext.*
import net.yet.util.app.Res
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


class TitleBar(context: Context) : RelativeLayout(context), IActionModeSupport {
	override val actionPanelView: View
		get() = this
	override var allActions: ArrayList<Action> = ArrayList<Action>()
	override var modeName: String = ""

	private val actionsBack = ArrayList<Action>()
	private var titleBack: String? = null
	private var navBack: Action? = null
	private var showInputBack = false

	var titleAlignCenter = titleCenter
	var navLargeIcon = false

	private var switchItems: List<String> = emptyList()
	private var switchPanel: SwitchPanel? = null
	private var switchItem: String = ""
	private var switchListener: SwitchPanel.SwitchSelectChangeListener? = null

	var title: String? = null
	var navAction: Action? = null

	var onAction: (bar: TitleBar, action: Action) -> Unit = {
		b, a ->
	}
	var onActionNav: (bar: TitleBar, action: Action) -> Unit = {
		b, a ->
	}
	var onTitleClick: (title: String) -> Unit = {}

	var inputEdit: EditTextX? = null
	var onSearch: (String) -> Unit = {}


	private var showInputEdit: Boolean = false
	private var editHint: CharSequence = ""
	private var onTextChanged: (String) -> Unit = {}
	var titleStyleDropdown = false


	init {
		genId()
		padding(0, 0, 0, 0).backColor(BG_COLOR)
		layoutParams = layoutParam().widthFill().height(HEIGHT)
	}

	override fun onResotreDefault() {
		allActions.clear()
		allActions.addAll(actionsBack)
		title = titleBack
		navAction = navBack
		showInputEdit = showInputBack
	}

	override fun onBackupDefault() {
		actionsBack.clear()
		actionsBack.addAll(allActions)
		titleBack = title
		navBack = navAction
		showInputBack = showInputEdit
	}

	override fun onCleanData() {
		allActions.clear()
		navAction = null
		title = null
		showInputEdit = false
	}


	fun isEditShowing(): Boolean {
		return showInputEdit
	}

	fun showInputEdit(hint: CharSequence = "", onTextChanged: (String) -> Unit) {
		this.showInputEdit = true
		editHint = hint
		this.onTextChanged = onTextChanged
	}

	fun hideInputEdit() {
		onTextChanged("")
		this.showInputEdit = false
		onTextChanged = {}
	}

	fun title(title: String?): TitleBar {
		this.title = title
		return this
	}

	fun setSwitchItems(vararg texts: String) {
		val ls = ArrayList<String>()
		for (s in texts) {
			ls.add(s)
		}
		setSwitchItems(ls)
	}

	fun setSwitchItems(switchItems: List<String>) {
		this.switchItems = switchItems
	}

	fun selectSwitch(switchItem: String) {
		this.switchItem = switchItem
		if (switchPanel != null) {
			switchPanel!!.selectItem(switchItem)
		}
	}

	fun getSwitchText(): String {
		return switchItem
	}

	fun setSwitchListener(switchListener: SwitchPanel.SwitchSelectChangeListener) {
		this.switchListener = switchListener
	}

	private val onClickListener = View.OnClickListener { v ->
		val action = v.tag as Action
		if (action.children.size > 0) {
			val pa = PopActions(context)
			pa.onPopAction = {
				it.onAction(it)
				onAction.invoke(this@TitleBar, it)
			}
			pa.addItems(action.children.filter { !it.hidden })
			pa.showAsDropDown(v, 0, 0)
		} else {
			action.onAction(action)
			if (isNavAction(action)) {
				onActionNav.invoke(this@TitleBar, action)
			} else {
				onAction.invoke(this@TitleBar, action)
			}
		}
	}


	private fun buildSwitchView(): SwitchPanel {
		val panel = SwitchPanel(context)
		panel.setItems(this.switchItems)
		if (switchItem.notEmpty()) {
			panel.selectItem(switchItem)
		}
		panel.setSelectCallback(object : SwitchPanel.SwitchSelectChangeListener {
			override fun onSwitchItemSelectChanged(text: String) {
				switchItem = text
				if (switchListener != null) {
					switchListener!!.onSwitchItemSelectChanged(text)
				}
			}
		})
		return panel
	}

	private fun buildActionView(action: Action): View {
		val view: View = if (action.icon != null) {
			val icon = action.icon
			icon?.size(IMAGE_BOUNDS)
			val iv = context.createImageView()
			iv.padding(PAD_HOR, PAD_VER, PAD_HOR, PAD_VER)
			if (navLargeIcon && isNavAction(action)) {
				iv.padding(4)
			}
			iv.setImageDrawable(icon)
			iv.scaleCenterInside()
			iv
		} else {
			val tv = context.createTextViewB().textColor(TEXT_COLOR).miniWidthDp(HEIGHT).gravityCenter().text(action.label).padding(5, 0, 5, 0)
			tv
		}
		view.tag = action
		view.setOnClickListener(this.onClickListener)
		view.backColor(Color.TRANSPARENT, BG_PRESSED)
		return view
	}

	private fun buildTitleView(): View {
		inputEdit = null
		if (showInputEdit) {
			val editText = context.createEditTextX().singleLine()
			editText.hint = editHint
			editText.gravity = Gravity.CENTER_VERTICAL
			editText.padding(10, 2, 10, 2)
			editText.imeSearch {
				onSearch(it.text.toString())
			}
			editText.addTextChangedListener(object : TextWatcher {
				override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
				}

				override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				}

				override fun afterTextChanged(s: Editable?) {
					onTextChanged(s?.toString() ?: "")
				}

			})
			inputEdit = editText
			return editText
		} else {
			val titleView = context.createTextViewA().textSizeTitle()
			titleView.textColor(TEXT_COLOR).miniWidthDp(35).gravityLeftCenter().ellipsizeMarquee()
			titleView.text(title)
			titleView.maxLines = 2
			titleView.onClick {
				onTitleClick(title ?: "")
			}
			if (titleStyleDropdown) {
				val drop = Res.drawable(R.drawable.dropdown).size(15, 15)
				titleView.setCompoundDrawables(null, null, drop, null)
				titleView.compoundDrawablePadding = dp(5)
			}
			return titleView
		}
	}

	private fun setViews(midCenter: Boolean, leftView: View?, midView: View, rightViews: List<View>) {
		var rightView: View? = null
		if (rightViews.isNotEmpty()) {
			val ll = context.createLinearHorizontal()
			addViewParam(ll) {
				widthWrap().heightFill().parentRight().centerVertical()
			}
			for (v in rightViews) {
				ll.addViewParam(v) {
					widthWrap().heightFill().gravityCenterVertical()
				}
			}
			rightView = ll
		} else {
			rightView = null
		}
		if (leftView != null) {
			addViewParam(leftView) {
				widthWrap().heightFill().parentLeft().centerVertical()
			}
		}

		if (showInputEdit) {
			addViewParam(midView) {
				if (leftView != null) {
					toRightOf(leftView).margins(5, 2, 5, 2)
				} else {
					parentLeft().margins(20, 2, 5, 2)
				}
				if (rightView != null) {
					toLeftOf(rightView!!)
				} else {
					parentRight()
				}
				heightFill().centerVertical().margins(0, 5, 20, 5)
			}
		} else {
			if (midCenter) {
				if (switchItems.isNotEmpty()) {
					val size = switchItems.size
					var w = if (size <= 2) {
						size * 100
					} else {
						size * 90
					}
					addViewParam(midView) {
						width(w).heightFill().centerInParent().margins(20, 8, 20, 8)
					}
				} else {
					addViewParam(midView) {
						widthWrap().heightFill().centerInParent()
					}
				}
			} else if (titleStyleDropdown) {
				addViewParam(midView) {
					if (leftView != null) {
						toRightOf(leftView).margins(5, 2, 5, 2)
					} else {
						parentLeft().margins(20, 2, 5, 2)
					}

					widthWrap().heightFill().centerVertical()
				}
			} else {
				addViewParam(midView) {
					if (leftView != null) {
						toRightOf(leftView).margins(5, 2, 5, 2)
					} else {
						parentLeft().margins(20, 2, 5, 2)
					}
					if (rightView != null) {
						toLeftOf(rightView!!)
					} else {
						parentRight()
					}
					heightFill().centerVertical()
				}
			}
		}
		leftView?.bringToFront()
		rightView?.bringToFront()

	}

	override fun onRebuild() {
		padding(0, 0, 0, 0).backColor(BG_COLOR)
		removeAllViews()
		var leftView: View? = null
		if (navAction != null) {
			if (!navAction!!.hidden) {
				leftView = buildActionView(navAction!!)
			}
		}
		var rightViews = ArrayList<View>()
		for (a in visibleAcitons) {
			val v = buildActionView(a)
			rightViews.add(v)
		}
		switchPanel = null
		if (switchItems.isNotEmpty()) {
			switchPanel = buildSwitchView()
			setViews(true, leftView, switchPanel!!, rightViews)
		} else {
			setViews(titleAlignCenter, leftView, buildTitleView(), rightViews)
		}

	}

	fun hideNav(): TitleBar {
		navAction = null
		return this
	}

	fun showBack(): TitleBar {
		setNavAction(Action(BACK_TAG).icon(R.drawable.back))
		return this
	}

	fun isBack(action: Action): Boolean {
		return action.isTag(BACK_TAG)
	}

	fun setNavAction(action: Action): TitleBar {
		this.navAction = action
		return this
	}

	fun isNavAction(action: Action): Boolean {
		return this.navAction == action
	}


	companion object {
		var titleCenter = false
		var TEXT_COLOR = Colors.Title
		var BG_COLOR = Colors.Theme
		var BG_PRESSED = Colors.Fade

		val HEIGHT = 50// dp
		val IMAGE_BOUNDS = 24// dp
		val PAD_VER = (HEIGHT - IMAGE_BOUNDS) / 2// pad top and bottom
		val PAD_HOR = (HEIGHT - IMAGE_BOUNDS) / 2// pad left-right

		val BACK_TAG = "title.back"
	}

}

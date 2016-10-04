package net.yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import net.yet.ext.size
import net.yet.theme.Dim
import net.yet.ui.ext.*
import net.yet.ui.res.Img
import net.yet.ui.res.ResConst
import net.yet.ui.widget.listview.itemview.TextDetailView
import net.yet.util.app.App
import java.util.*

/**
 * 添加action之前设置参数能提高性能.
 * 一次性添加所有action能提高性能
 * Created by yet on 2015/10/13.
 */
class ActionSetView(context: Context) : LinearLayout(context), IActionPanel {
	override var allActions: ArrayList<Action> = ArrayList<Action>()
	override val actionPanelView: View
		get() = this

	var GROUP_MARGIN = 15
	private var iconSize = Dim.iconSize
	private var subIconSize = Dim.iconSizeMin
	private val subIconLeftSize = Dim.iconSizeMid
	private var textSize = Dim.textSizeB
	private var subTextSize = Dim.textSizeC
	private var linearPanel: LinearPanel
	var onItemAction: (Action) -> Unit = {}
	private val groupSet = HashSet<Int>()


	init {
		vertical()
		linearPanel = LinearPanel(context)
		linearPanel.setItemHeight(50)
		linearPanel.setItemClickListener(object : LinearPanel.LinearPanelItemListener {
			override fun onItemViewClick(view: View, position: Int) {
				val a = view.tag as Action
				a.onAction(a)
				onItemAction(a)
			}
		})
		addViewParam(linearPanel) { widthFill().heightWrap() }
//		setBackgroundColor(Colors.PageGray)
		this.divider(Divider())
	}

	private fun createOneItemView(context: Context, item: Action, hasIcon: Boolean, hasSubIcon: Boolean): View {
		if (item.isSwitch) {
			val ll = context.createLinearHorizontal()
			ll.padding(20, 10, 20, 10)
			val tv = context.createTextView()
			tv.textSizeSp(textSize)
			tv.text = item.label
			val sb = SwitchButton(context)
			sb.isChecked = item.checked

			ll.addViewParam(tv) {
				width(0).weight(1).heightWrap().gravityCenter()
			}
			ll.addViewParam(sb) {
				width(SwitchButton.WIDTH).height(SwitchButton.HEIGHT).gravityCenter()
			}
			sb.setOnCheckedChangeListener { b, c ->
				item.checked = c
				item.onCheckChanged(item)
				item.onAction(item)
			}
			return ll
		}

		val view = TextDetailView(context)
		val padLeft = if (hasIcon) 15 else 20
		val padRight = if (hasSubIcon) 15 else 20
		view.padding(padLeft, 5, padRight, 5)
		view.setTextSize(textSize, subTextSize)
		view.setValues(item.label, item.subLabel)
		view.detailView.setTextColor(item.subLabelColor)
		var icon = item.icon
		if (icon == null && hasIcon) {
			icon = Img.color(Color.TRANSPARENT)
		}
		if (icon != null) {
			view.textView.compoundDrawablePadding = App.dp2px(10)
			view.textView.setCompoundDrawables(icon.size(iconSize), null, null, null)
		}
		var subIcon = item.subIcon
		if (item.num != 0) {
			subIcon = ResConst.redPoint()
		}
		if (subIcon == null && hasSubIcon) {
			subIcon = Img.color(Color.TRANSPARENT)
		}
		if (subIcon != null) {
			subIcon.size(subIconSize)
		}
		val subIconLeft = item.subIconLeft
		subIconLeft?.size(subIconLeftSize)
		if (subIcon != null || subIconLeft != null) {
			view.detailView.compoundDrawablePadding = App.dp2px(10)
			view.detailView.setCompoundDrawables(subIconLeft, null, subIcon, null)
		}
		return view
	}

	override fun onRebuild() {
		linearPanel.clean()
		val items = visibleAcitons
		var hasIcon = false
		var hasSubIcon = false
		for (a in items) {
			if (a.icon != null) {
				hasIcon = true
			}
			if (a.subIcon != null || a.num != 0) {
				hasSubIcon = true
			}
		}
		for (i in items.indices) {
			val action = items[i]
			val marginTop = if (i == 0) {
				0
			} else if (groupSet.contains(i)) {
				GROUP_MARGIN
			} else {
				action.marginTop
			}
			val view = createOneItemView(context, action, hasIcon, hasSubIcon)
			view.tag = action
			linearPanel.addItemView(view, marginTop)
		}
	}

	fun setIconSize(iconSize: Int, subIconSize: Int): ActionSetView {
		this.iconSize = iconSize
		this.subIconSize = subIconSize
		rebuild()
		return this
	}

	fun setTextSize(textSize: Int, subTextSize: Int): ActionSetView {
		this.textSize = textSize
		this.subTextSize = subTextSize
		rebuild()
		return this
	}

	fun newGroup(): ActionSetView {
		groupSet.add(actionCount)
		return this
	}




}

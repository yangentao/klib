package net.yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import net.yet.ui.ext.*
import net.yet.ui.util.StateColor
import net.yet.ui.util.StateImage
import net.yet.util.app.App
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

/**
 * tabbar是有select状态的.
 */
class TabBar(context: Context) : LinearLayout(context), IActionPanel {
	override var allActions: ArrayList<Action> = ArrayList<Action>()


	override val actionPanelView: View
		get() = this


	private var verLine = false
	val style = TabBarStyle()
	var HEIGHT = 50
		get() {
			return field
		}
		set(value) {
			field = value
			layoutParam().widthFill().height(HEIGHT).set(this)
		}


	init {
		this.horizontal()
		backColor(style.lineColor).gravityCenter()
		layoutParam().widthFill().height(HEIGHT).set(this)
	}


	var onUnselect: (TabBar, Action) -> Unit = {
		b, a ->
	}
	var onReselect: (TabBar, Action) -> Unit = {
		b, a ->
	}
	var onSelect: (TabBar, Action) -> Unit = {
		b, a ->
	}

	val selectedAction: Action? get() {
		for (a in visibleAcitons) {
			if (a.selected) {
				return a
			}
		}
		return null
	}

	private val onClickListener = View.OnClickListener { v ->
		val action = v.tag as Action
		action.onAction(action)
		select(action.tag)
	}


	fun select(tag: String) {
		for (a in visibleAcitons) {
			if (a.selected && !a.isTag(tag)) {
				a.selected(false)
				onUnselect.invoke(this@TabBar, a)
			}
		}
		for (a in visibleAcitons) {
			if (a.isTag(tag)) {
				if (a.selected) {
					onReselect.invoke(this@TabBar, a)
				} else {
					a.selected(true)
					onSelect.invoke(this@TabBar, a)
				}
			}
		}
		rebuild()
	}

	override fun onRebuild() {
		this.removeAllViews()
		this.backColor(style.lineColor)
		val items = visibleAcitons
		var size = items.size
		for (action in items) {
			val tv = context.createTextViewD()
			val csl = StateColor(style.textColor).pressed(style.textPressedColor).selected(style.textSelectedColor).get()
			tv.textColor(csl).gravityCenter().padding(1, 4, 1, 1)
			val backDraw = StateImage(style.backColor).pressed(style.backPressedColor).selected(style.backSelectedColor).value
			tv.backDrawable(backDraw)//.miniHeightTV(TabBar.HEIGHT_MIN);
			tv.text(action.label)
			if (action.icon != null) {
				action.icon?.setBounds(0, 0, App.dp2px(style.imageSize), App.dp2px(style.imageSize))
				tv.compoundDrawablePadding = 0
				tv.setCompoundDrawables(null, action.icon, null, null)
			}

			val indicateView = context.createTextView()
			indicateView.textColor(Color.WHITE).textSizeSp(10).gravityCenter().backDrawable("red_point").gone()
			if (action.num > 0) {
				indicateView.visiable().text("" + action.num)
			} else if (action.num == 0) {
				indicateView.gone()
			} else {
				// n < 0
				indicateView.visiable().text("")
			}

			val marginRight = if (verLine) if (size == size - 1) 0 else 1 else 0
			val panel = context.createRelativeLayout()
			panel.addViewParam(tv) { fill() }
			panel.addViewParam(indicateView) { parentRight().parentTop().margins(0, 5, 5, 0).size(15) }

			this.addViewParam(panel) { width(0).weight(1f).heightFill().gravityCenter().margins(0, 1, marginRight, 0) }

			if (action.selected) {
				panel.isSelected = true
			}
			panel.setOnClickListener(onClickListener)
			panel.tag = action
		}
	}

	fun setLineVer(verLine: Boolean): TabBar {
		this.verLine = verLine
		return this
	}

}

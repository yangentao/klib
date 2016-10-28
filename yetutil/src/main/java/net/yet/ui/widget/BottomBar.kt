package net.yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import net.yet.ext.empty
import net.yet.ext.size
import net.yet.theme.Colors
import net.yet.theme.Dim
import net.yet.ui.ext.*
import net.yet.ui.util.StateImage
import net.yet.util.Util
import net.yet.util.log.xlog
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


class BottomBar(context: Context) : TablePanel(context), IActionModeSupport {
	override var modeName: String = ""
	override var allActions: ArrayList<Action> = ArrayList<Action>()
	private val actionBack = ArrayList<Action>()

	override fun onResotreDefault() {
		allActions.clear()
		allActions.addAll(actionBack)
	}

	override fun onBackupDefault() {
		actionBack.addAll(allActions)
	}

	override fun onCleanData() {
		allActions.clear()
	}

	override val actionPanelView: View
		get() = this

	//颜色相关
	var TEXT_COLOR = Colors.TextColorMinor
	var TEXT_PRESSED = Colors.Fade
	var TEXT_RISK = Color.WHITE
	var BG_COLOR = Color.WHITE
	var BG_PRESSED = Colors.Fade
	var BG_RISK = Colors.Risk
	var LINE_COLOR = Color.LTGRAY//可以改变

	val HEIGHT = 50
	val IMG_SIZE = Dim.iconSize


	var onAction: (BottomBar, Action) -> Unit = {
		bar, action ->
	}

	private val onClickListener = View.OnClickListener { v ->
		val a = v.tag as Action
		a.onAction(a)
		onAction.invoke(this@BottomBar, a)
	}

	init {
		linearParam().widthFill().heightWrap().set(this)
	}


	override fun onRebuild() {
		this.cleanActions()
		this.setRowHeight(HEIGHT)
		this.backColor(Colors.PageGray)
		this.padding(0, 1, 0, 0)

		var needVLine = true
		val items = visibleAcitons
		var visibleCount = items.size
		for (action in items) {
			if (action.icon != null && Util.notEmpty(action.label)) {
				needVLine = false
			}
		}
		this.setHorSpace(if (needVLine) 1 else 0)
		this.addRow()
		for (action in items) {
			val view: View = if (action.label.empty()) {
				if (action.icon == null) {
					xlog.e("neithor Label OR icon NOT SET")
					throw IllegalArgumentException("neithor Label OR icon NOT SET")
				} else {//only  icon
					val iv = context.createImageView()
					iv.setImageDrawable(action.icon?.size(IMG_SIZE))
					iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
					iv.setBackgroundDrawable(StateImage(BG_COLOR).pressed(BG_PRESSED).value)
					iv
				}
			} else {//label
				val tv = context.createTextViewC()
				tv.gravityCenter().padding(1, 2, 1, 1).text(action.label)
				if (action.risk) {
					tv.backDrawable(StateImage(BG_RISK).pressed(BG_PRESSED).value).textColor(TEXT_RISK)
				} else {
					tv.backDrawable(StateImage(BG_COLOR).pressed(BG_PRESSED).value).textColor(TEXT_COLOR, TEXT_PRESSED)
				}
				if (action.icon != null) {
					action.icon?.setBounds(0, 0, dp(IMG_SIZE), dp(IMG_SIZE))
					tv.compoundDrawablePadding = 0
					tv.setCompoundDrawables(null, action.icon, null, null)
					tv.textSizeSp(Dim.textSizeD)
				} else {
					tv.textSizeSp(Dim.textSizeB)
				}
				tv
			}
			view.tag = action
			view.setOnClickListener(onClickListener)
			this.addItemView(view)

			val addedCount = this.itemViewCount
			if (visibleCount <= 4) {
				//1 row
			} else if (visibleCount < 8) {
				val firstRowCols = visibleCount / 2
				if (addedCount == firstRowCols) {
					this.addRow()
				}
			} else {
				val mod = visibleCount % 4
				if (addedCount % 4 == mod && visibleCount != addedCount) {
					this.addRow()
				}
			}
		}
	}
}

package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import yet.ext.size
import yet.theme.Colors
import yet.theme.IconSize
import yet.theme.TextSize
import yet.ui.ext.*
import yet.ui.res.ColorStated
import yet.ui.viewcreator.createImageView
import yet.ui.viewcreator.createTextViewC
import yet.util.Util
import yet.util.log.xlog
import java.util.*

/**
 * ActionTable actionTable = new ActionTable(getContext());
 * actionTable.addRow().add(new Action("拨号").safe(true), new Action("短信")).commit();
 * actionTable.addRow().addActions("复制号码", "复制名称号码", "删除").commit();
 * actionTable.addRow().addActions("添加到联系人", "新建联系人", "查看联系人").commit();
 * actionTable.addRow().addAction("删除").risk(true).commit();
 * actionTable.addRow().addAction("关闭").commit();
 * actionTable.setCallback(this);
 *
 *
 * AlertDialog.Builder b = new AlertDialog.Builder(getContext());
 * b.setTitle("杨恩涛: 15098760059");
 * b.setView(actionTable);
 * b.setCancelable(true);
 * AlertDialog dlg = b.create();
 * dlg.setCancelable(true);
 * dlg.setCanceledOnTouchOutside(true);
 * dlg.show();
 */
class ActionTable(context: Context) : LinearLayout(context) {

	var LINE_COLOR = Color.LTGRAY//可以改变

	private var callback: ActionTableCallback? = null
	private val clickListener = View.OnClickListener { v ->
		if (callback != null) {
			callback!!.onActionTable(this@ActionTable, v.tag as Action)
		}
	}
	private var rowHeight = 50
	private var horSpace = 1
	private var verSpace = 1

	init {
		genId()
		this.orientationVertical()
		this.setBackgroundColor(LINE_COLOR)
		linearParam().widthFill().heightWrap().set(this)
	}

	fun setCallback(callback: ActionTableCallback) {
		this.callback = callback
	}

	val rowCount: Int
		get() = childCount

	fun getRow(index: Int): ActionRow {
		return getChildAt(index).tag as ActionRow
	}

	fun addRow(): ActionRow {
		val row = ActionRow(context)
		row.clickListener = this.clickListener
		row.rowHeight = this.rowHeight
		row.horSpace = this.horSpace
		row.tag = row
		val lp = linearParam().widthFill().height(rowHeight).margins(0, if (childCount == 0) 0 else verSpace, 0, 0)
		addView(row, lp)
		return row
	}

	fun setSpace(spaceDp: Int) {
		setVerSpace(spaceDp)
		setHorSpace(spaceDp)
	}

	fun setVerSpace(verSpaceDp: Int) {
		this.verSpace = verSpaceDp
	}

	fun setHorSpace(horSpaceDp: Int) {
		this.horSpace = horSpaceDp
	}

	fun setRowHeight(rowHeightDp: Int) {
		this.rowHeight = rowHeightDp
	}

	interface ActionTableCallback {
		fun onActionTable(actionTable: ActionTable, action: Action)
	}

	class ActionRow(context: Context) : LinearLayout(context), IActionPanel {
		override val actionPanelView: View get() = this
		override var allActions: ArrayList<Action> = ArrayList<Action>()

		var IMG_SIZE = IconSize.Normal
		var rowHeight = 45
		var horSpace = 1
		var clickListener: View.OnClickListener? = null

		init {
			genId()
			horizontal()
		}

		override fun onRebuild() {
			removeAllViews()
			val needVLine = true
			val items = visibleAcitons
			for (action in items) {
				var view: View? = null
				if (Util.empty(action.label)) {
					if (action.icon == null) {
						xlog.e("neithor Label OR icon NOT SET")
						continue
					} else {
						//only  icon
						val iv = context.createImageView()
						iv.setImageDrawable(action.icon?.size(IMG_SIZE))
						iv.scaleType = ImageView.ScaleType.CENTER_INSIDE
						val itemBackDraw = ColorStated(Color.WHITE).pressed(Colors.Fade).value
						iv.setBackgroundDrawable(itemBackDraw)
						view = iv
					}
				} else {
					//label
					val tv = context.createTextViewC()
					tv.textSizeSp(if (action.icon == null) TextSize.Normal else TextSize.Tiny)
					tv.textX(action.label).gravityCenter().padding(1, 2, 1, 1)
					if (action.risk) {
						val riskItemBackkDraw = ColorStated(Colors.Risk).pressed(Colors.Fade).value
						tv.backDrawable(riskItemBackkDraw).textColor(Color.WHITE)
					} else if (action.safe) {
						val riskItemBackkDraw = ColorStated(Colors.Safe).pressed(Colors.Fade).value
						tv.backDrawable(riskItemBackkDraw).textColor(Color.WHITE)
					} else {
						val itemBackDraw = ColorStated(Color.WHITE).pressed(Colors.Fade).value
						tv.backDrawable(itemBackDraw).textColor(Colors.TextColorMid)
					}
					if (action.icon != null) {
						action.icon!!.setBounds(0, 0, dp(IMG_SIZE), dp(IMG_SIZE))
						tv.compoundDrawablePadding = 0
						tv.setCompoundDrawables(null, action.icon, null, null)
					}
					view = tv
				}
				view!!.tag = action
				view.setOnClickListener(clickListener)
				var marginLeft = if (childCount == 0) 0 else horSpace
				if (!needVLine) {
					marginLeft = 0
				}

				addView(view, linearParam().width(0).weight(1f).height(rowHeight).gravityCenter().margins(marginLeft, 0, 0, 0))
			}
		}
	}

}

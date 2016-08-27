package net.yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.theme.Dim
import net.yet.ui.ext.*
import net.yet.ui.util.ShapeUtil
import net.yet.util.DateUtil

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

/**
 * -----------------------------------------------
 * |     | title                          detail |
 * |icon |                                       |
 * |     | msg                            status |
 * -----------------------------------------------
 */
class ImageTitleDateMsgStatusItemView(context: Context) : HorItemView(context) {
	var iconView: ImageView = createImageView()
	var titleView: TextView = createTextViewB().singleLine().ellipsizeEnd()
	var dateView: TextView = createTextViewC().singleLine()
	var msgView: TextView = createTextViewC().singleLine().ellipsizeEnd()
	var statusView: TextView = createTextViewC().singleLine()
	private var iconSize = Dim.iconSizeMid

	private var topView = createLinearHorizontal().gravityCenterVertical()
	private var bottomView = createLinearHorizontal().gravityCenterVertical()

	init {
		padding(10, 0, 10, 0)
		addViewParam(iconView) { size(iconSize).gravityCenter().margins(0, 0, 5, 0) }

		val ll = createLinearVertical().apply {
			val top = topView.apply {
				addViewParam(titleView) { widthDp(0).heightWrap().weight(1f) }
				addView(dateView, linearParam().wrap().margins(5, 0, 0, 0))
			}
			addViewParam(top) { widthFill().heightWrap() }
			val bottom = bottomView.apply {
				addViewParam(msgView) { widthDp(0).heightWrap().weight(1f) }
				addViewParam(statusView) { wrap().margins(5, 0, 0, 0) }
			}
			addViewParam(bottom) { widthFill().heightWrap() }
		}
		addViewParam(ll) { widthDp(0).weight(1f).heightWrap().margins(5, 10, 5, 5) }
	}

	fun setSepratorSize(dp: Int) {
		val p = topView.layoutParams as LinearLayout.LayoutParams
		p.margins(p.leftMargin, p.topMargin, p.rightMargin, dp)
		topView.layoutParams = p
	}

	fun setIconSize(dp: Int): ImageTitleDateMsgStatusItemView {
		this.iconSize = dp
		return this
	}


	fun setValues(icon: Drawable?, title: String, date: String, msg: String, status: String): ImageTitleDateMsgStatusItemView {
		iconView.setImageDrawable(icon)
		titleView.text = title
		dateView.text = date
		msgView.text = msg
		statusView.text = status
		return this
	}

	fun setValues(icon: Drawable?, title: String, date: Long, msg: String, status: String): ImageTitleDateMsgStatusItemView {
		val s = if (date == 0L) "" else DateUtil.shortString(date)
		return setValues(icon, title, s, msg, status)
	}

	fun showRedPoint(show: Boolean): ImageTitleDateMsgStatusItemView {
		statusView.setCompoundDrawables(null, null, if (show) redDrawable else null, null)
		return this
	}

	companion object {
		private val redDrawable = ShapeUtil.oval(10, Color.rgb(255, 128, 0))
	}

}

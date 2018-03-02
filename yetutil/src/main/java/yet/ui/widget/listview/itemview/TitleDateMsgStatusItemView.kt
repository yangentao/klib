package yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView
import yet.ui.ext.*
import yet.ui.util.ShapeUtil
import yet.util.DateUtil

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

// 有3个文本的view, name和date在一行, msg在他们的下面
// name----------------date
// msg-----------------status
class TitleDateMsgStatusItemView(context: Context) : HorItemView(context) {
	private val verLayout: LinearLayout
	var titleView: TextView
	var dateView: TextView
	var msgView: TextView
	var statusView: TextView

	init {
		verLayout = createLinearVertical()
		val top = createLinearHorizontal().gravityCenterVertical()
		titleView = createTextViewB().singleLine().ellipsizeEnd()
		dateView = createTextViewC().singleLine()

		top.addViewParam(titleView) { widthDp(0).heightWrap().weight(1f) }
		top.addViewParam(dateView) { wrap().margins(5, 0, 0, 0) }

		val bottom = createLinearHorizontal().gravityCenterVertical()
		msgView = createTextViewC().singleLine().ellipsizeEnd()
		statusView = createTextViewC().singleLine()
		bottom.addViewParam(msgView) { widthDp(0).heightWrap().weight(1f) }
		bottom.addViewParam(statusView) { wrap().margins(5, 0, 0, 0) }

		verLayout.addViewParam(top) { widthFill().heightWrap() }
		verLayout.addViewParam(bottom) { widthFill().heightWrap().margins(0, 5, 0, 0) }
		this.addViewParam(verLayout) { widthFill().heightWrap() }
	}

	fun title(title: String): TitleDateMsgStatusItemView {
		titleView.text = title
		return this
	}

	fun msg(msg: String): TitleDateMsgStatusItemView {
		msgView.text = msg
		return this
	}

	fun date(date: String): TitleDateMsgStatusItemView {
		dateView.text = date
		return this
	}

	fun date(date: Long): TitleDateMsgStatusItemView {
		dateView.text = DateUtil.shortString(date)
		return this
	}

	fun status(status: String): TitleDateMsgStatusItemView {
		statusView.text = status
		return this
	}

	fun setValues(name: String, date: String, msg: String, status: String?): TitleDateMsgStatusItemView {
		titleView.text = name
		dateView.text = date
		msgView.text = msg
		statusView.text = status
		return this
	}

	fun setValues(name: String, date: Long, msg: String, status: String?): TitleDateMsgStatusItemView {
		val s = if (date == 0L) "" else DateUtil.shortString(date)
		return setValues(name, s, msg, status)
	}

	fun showRedPoint(show: Boolean): TitleDateMsgStatusItemView {
		statusView.setCompoundDrawables(null, null, if (show) redDrawable else null, null)
		return this
	}

	companion object {

		fun create(context: Context): TitleDateMsgStatusItemView {
			return TitleDateMsgStatusItemView(context)
		}

		private val redDrawable = ShapeUtil.oval(10, Color.rgb(255, 128, 0))
	}

}

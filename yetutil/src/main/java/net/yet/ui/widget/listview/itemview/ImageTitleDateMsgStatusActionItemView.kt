package net.yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import net.yet.theme.Colors
import net.yet.theme.Dim
import net.yet.ui.ext.*
import net.yet.ui.res.ResConst
import net.yet.ui.util.ShapeUtil
import net.yet.util.DateUtil

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

/**
 * --------------------------------------------------------
 * |     | title                          detail |        |
 * |icon |                                       |iconTail|
 * |     | msg                            status |        |
 * --------------------------------------------------------
 */
class ImageTitleDateMsgStatusActionItemView(context: Context) : HorItemView(context) {
    interface ImageItemActionCallback {
        fun onImageItemAction(itemView: ImageTitleDateMsgStatusActionItemView, position: Int)
    }

    var iconView: ImageView = createImageView()
    var titleView: TextView = createTextViewB().singleLine().ellipsizeEnd()
    var dateView: TextView = createTextViewC().singleLine()
    var msgView: TextView = createTextViewC().singleLine().ellipsizeEnd()
    var statusView: TextView = createTextViewC().singleLine()
    var subIconView: ImageView
    private val lineView: View
    var position = -1

    private var iconSize = Dim.iconSizeMid

    private var callback: ImageItemActionCallback? = null


    init {
        padding(10, 0, 0, 0)
        addViewParam(iconView) { size(iconSize).gravityCenter().margins(0, 0, 5, 0) }

        val ll = createLinearVertical().apply {
            val top = createLinearHorizontal().gravityCenterVertical().apply {
                addViewParam(titleView) { widthDp(0).heightWrap().weight(1f) }
                addViewParam(dateView) { wrap().margins(5, 0, 0, 0) }
            }
            addViewParam(top) { widthFill().heightWrap() }
            val bottom = createLinearHorizontal().gravityCenterVertical().apply {
                addViewParam(msgView) { widthDp(0).heightWrap().weight(1f) }
                addViewParam(statusView) { wrap().margins(5, 0, 0, 0) }
            }
            addViewParam(bottom) { widthFill().heightWrap() }
        }

        this.addViewParam(ll) { widthDp(0).weight(1f).heightWrap().margins(5, 10, 5, 5) }

        lineView = View(context).genId().backColorPage()
        addViewParam(lineView) { widthDp(1).heightDp(40).margins(3, 3, 0, 3).gravityCenterVertical() }

        subIconView = createImageView().backColor(Color.TRANSPARENT, Colors.Fade).padding(16)
        subIconView.setImageDrawable(arrowRight)
        subIconView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        addViewParam(subIconView) { heightFill().widthDp(45).gravityCenter() }
    }


    fun setValues(icon: Drawable, title: String, date: String, msg: String, status: String, position: Int): ImageTitleDateMsgStatusActionItemView {
        return setValues(icon, title, date, msg, status, arrowRight, position)
    }

    fun setValues(icon: Drawable, title: String, date: String, msg: String, status: String, subIcon: Drawable, position: Int): ImageTitleDateMsgStatusActionItemView {
        iconView.setImageDrawable(icon)
        titleView.text = title
        dateView.text = date
        msgView.text = msg
        statusView.text = status
        subIconView.setImageDrawable(subIcon)
        this.position = position
        subIconView.tag = this
        subIconView.setOnClickListener(clickListener)
        return this
    }

    fun setValues(icon: Drawable, title: String, date: Long, msg: String, status: String, subIcon: Drawable, position: Int): ImageTitleDateMsgStatusActionItemView {
        val s = if (date == 0L) "" else DateUtil.shortString(date)
        return setValues(icon, title, s, msg, status, subIcon, position)
    }

    fun setValues(icon: Drawable, title: String, date: Long, msg: String, status: String, position: Int): ImageTitleDateMsgStatusActionItemView {
        return setValues(icon, title, date, msg, status, arrowRight, position)
    }

    fun showRedPoint(show: Boolean): ImageTitleDateMsgStatusActionItemView {
        statusView.setCompoundDrawables(null, null, if (show) redDrawable else null, null)
        return this
    }

    fun setCallback(callback: ImageItemActionCallback): ImageTitleDateMsgStatusActionItemView {
        this.callback = callback
        return this
    }

    fun setIconSize(dp: Int): ImageTitleDateMsgStatusActionItemView {
        this.iconSize = dp
        return this
    }

    fun hideAction(hide: Boolean): ImageTitleDateMsgStatusActionItemView {
        lineView.visibility = if (hide) View.GONE else View.VISIBLE
        subIconView.visibility = if (hide) View.GONE else View.VISIBLE
        return this
    }

    companion object {

        private val arrowRight = ResConst.arrowRight()

        private val redDrawable = ShapeUtil.oval(10, Color.rgb(255, 128, 0))

        private val clickListener = View.OnClickListener { v ->
            val iv = v.tag as ImageTitleDateMsgStatusActionItemView
            if (iv.callback != null) {
                iv.callback!!.onImageItemAction(iv, iv.position)
            }
        }
    }
}

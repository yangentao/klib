package net.yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.ui.ext.*
import net.yet.ui.widget.recycler.ActionItemView
import net.yet.ui.widget.recycler.ImageItemAction
import net.yet.ui.widget.recycler.TextItemAction
import net.yet.util.app.App
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

class XRowItemView(context: Context) : HorItemView(context), ActionItemView {
    var leftPanel: LinearLayout
    var rightPanel: LinearLayout
    private val actionViews = ArrayList<View>()

    init {
        padding(10, 0, 10, 0).gravityCenterVertical()
        leftPanel = createLinearHorizontal().gravityCenterVertical()
        rightPanel = createLinearHorizontal().gravityRightCenter()
        addViewParam(leftPanel) { widthDp(0).weight(1f).heightFill() }
        addViewParam(rightPanel) { widthWrap().heightFill() }
    }

    fun minHeight(dp: Int): XRowItemView {
        minimumHeight = App.dp2px(dp)
        return this
    }

    fun setRightWidth(dp: Int): XRowItemView {
        val lp = rightPanel.layoutParams as LinearLayout.LayoutParams
        lp.width = App.dp2px(dp)
        rightPanel.layoutParams = lp
        return this
    }

    fun setRightWidthWrap(): XRowItemView {
        val lp = rightPanel.layoutParams as LinearLayout.LayoutParams
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        rightPanel.layoutParams = lp
        return this
    }

    fun setRightWeight(weight: Float): XRowItemView {
        val lp = rightPanel.layoutParams as LinearLayout.LayoutParams
        lp.width = 0
        lp.weight = weight
        rightPanel.layoutParams = lp
        return this
    }

    fun getLeft(index: Int): View {
        return leftPanel.getChildAt(index)
    }

    fun getRight(index: Int): View {
        return rightPanel.getChildAt(index)
    }

    fun addLeft(view: View): XRowItemView {
        leftPanel.addView(view)
        return this
    }

    fun addRight(view: View): XRowItemView {
        rightPanel.addView(view)
        return this
    }

    fun setLeftText(index: Int, text: String): XRowItemView {
        val tv = leftPanel.getChildAt(index) as TextView
        tv.text = text
        return this
    }

    fun setRightText(index: Int, text: String): XRowItemView {
        val tv = rightPanel.getChildAt(index) as TextView
        tv.text = text
        return this
    }

    fun addLeftTextAction(text: String): TextItemAction {
        val t = TextItemAction(context).text(text)
        leftPanel.addView(t)
        return t
    }

    fun addRightTextAction(text: String): TextItemAction {
        val t = TextItemAction(context).text(text)
        rightPanel.addView(t)
        return t
    }

    fun addLeftText(text: String): TextView {
        val tv = context.createTextViewB()
        tv.gravityLeftCenter().text(text)
        leftPanel.addView(tv)
        return tv
    }

    fun addLeftIcon(icon: Drawable): ImageItemAction {
        val v = ImageItemAction(context)
        v.icon(icon)
        leftPanel.addView(v)
        return v
    }

    fun addRightIcon(icon: Drawable): ImageItemAction {
        val v = ImageItemAction(context)
        v.icon(icon)
        rightPanel.addView(v)
        return v
    }

    fun asAction(view: View) {
        actionViews.add(view)
    }

    override fun getActionCount(): Int {
        return actionViews.size
    }

    override fun getActionView(index: Int): View {
        return actionViews[index]
    }
}

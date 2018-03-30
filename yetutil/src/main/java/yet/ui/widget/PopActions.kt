package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.PopupWindow
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.res.D
import yet.ui.res.sized
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createTextViewA
import yet.util.fore
import java.util.*

class PopActions(private val context: Context) {


	private val items = ArrayList<Action>()
	private var p: PopupWindow? = null
	var onPopAction: (Action) -> Unit = {

	}

	private fun createItemView(context: Context, item: Action): View {
		val tv = context.createTextViewA()
		tv.singleLine()
		tv.gravityLeftCenter().padding(5, 5, 20, 5)
		tv.text = item.label
		var d = item.icon
		if (d == null) {
			d = D.color(Color.TRANSPARENT)
		}
		d = d.sized(TitleBar.IMAGE_BOUNDS)
		tv.compoundDrawablePadding = dp(10)
		tv.setCompoundDrawables(d, null, null, null)
		tv.textColorWhite()
		return tv
	}

	private fun create(context: Context): PopupWindow {
		val linearPanel = LinearPanel(context)
		linearPanel.backColor(Colors.TRANS)
		linearPanel.minimumWidth = dp(MIN_WIDTH)
		linearPanel.setItemHeight(45)
		for (action in this.items) {
			val v = createItemView(context, action)
			linearPanel.addItemView(v)
			v.backColorTransFade()
		}
		linearPanel.setItemClickListener(object : LinearPanel.LinearPanelItemListener {
			override fun onItemViewClick(view: View, position: Int) {
				p!!.dismiss()
				fore { onPopAction(items[position]) }
			}
		})

		val p = PopupWindow(context)
		p.width = LayoutParams.WRAP_CONTENT
		p.height = LayoutParams.WRAP_CONTENT
		p.isFocusable = true
		p.isOutsideTouchable = true
		p.setBackgroundDrawable(ColorDrawable(0))

		val view = context.createLinearVertical()
		val gd = GradientDrawable()
		gd.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, dp(4).toFloat(), dp(4).toFloat())
		gd.setColor(Colors.Theme)
		gd.setBounds(0, 0, 200, 300)
		view.backDrawable(gd).padding(5)

		view.addView(linearPanel, linearParam().wrap())

		p.contentView = view
		return p
	}

	fun showAt(parent: View, gravity: Int, xOffset: Int, yOffset: Int) {
		p = create(context)
		p!!.showAtLocation(parent, gravity, xOffset, yOffset)
	}

	fun showAsDropDown(anchor: View, xOffset: Int, yOffset: Int) {
		p = create(context)
		p!!.showAsDropDown(anchor, xOffset, yOffset)
	}

	fun addItems(items: List<Action>) {
		this.items.addAll(items)
	}

	fun addItems(vararg items: Action) {
		for (a in items) {
			this.items.add(a)
		}
	}

	companion object {
		var MIN_WIDTH = 150
	}

}

package net.yet.ui.widget

import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.theme.Colors
import net.yet.ui.ext.*
import net.yet.util.fore
import java.util.*

open class SwitchView {
	class SwitchItemView(context: Context) : LinearLayout(context) {
		var textView: TextView

		init {
			this.genId()
			this.orientationVertical().backColor(Colors.WHITE, Colors.Fade)
			textView = context.createTextViewB()
			textView.padding(20, 10, 20, 10).gravityCenter().backColor(Colors.WHITE)
			this.addViewParam(textView) { widthFill().heightWrap().margins(0, 0, 0, 3) }
		}

		var text: String
			get() {
				return textView.text.toString()
			}
			set(text) {
				textView.text(text)
			}

	}

	private var _view: LinearLayout? = null
	val view: LinearLayout get() = _view!!

	private val items = ArrayList<String>()
	private val viewMap = HashMap<String, SwitchItemView>()

	private var sepLine = true

	fun create(context: Context): View {
		_view = context.createLinearHorizontal().backColorWhite()
		view.setLinearParam { widthFill().heightWrap().margins(0, 1, 0, 1) }
		return view
	}

	fun getItemView(itemName: String): SwitchItemView {
		return viewMap.get(itemName)!!
	}

	fun setSepLine(hasLine: Boolean) {
		this.sepLine = hasLine
	}

	fun clean() {
		items.clear()
		viewMap.clear()
		view.removeAllViews()
	}

	fun addItems(vararg items: String) {
		for (s in items) {
			addItem(s)
		}
	}

	fun setItems(items: List<String>) {
		clean()
		for (item in items) {
			addItems(item)
		}
	}

	fun addItem(itemName: String) {
		items.add(itemName)
		if (sepLine && view.childCount > 0) {
			val v = view.createView().backColor(Colors.PageGray)
			view.addViewParam(v) { widthPx(2).heightFill().margins(0, 3, 0, 0) }
		}
		val itemView = SwitchItemView(view.context)
		itemView.text = itemName
		view.addViewParam(itemView) { width(0).weight(1f).heightWrap() }
		itemView.setOnClickListener(clickListener)
		viewMap.put(itemName, itemView)
	}

	private val clickListener = OnClickListener { v ->
		val s = (v as SwitchItemView).text
		fore {
			select(s, true)
		}
	}

	fun select(name: String, fire: Boolean = true) {
		for (s in items) {
			viewMap[s]?.isSelected = false
		}
		viewMap[name]?.isSelected = true
		if (fire) {
			onSelectChanged(items.indexOf(name), name)
		}
	}

	open fun onSelectChanged(index: Int, itemName: String) {

	}

}

package yet.ui.dialogs.list

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ListView
import android.widget.TextView
import yet.theme.Colors
import yet.theme.InputSize
import yet.ui.ext.*
import yet.ui.res.RectDraw
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createListView
import yet.ui.viewcreator.createTextViewA
import yet.ui.widget.TitleBar
import yet.ui.widget.listview.XBaseAdapter
import yet.ui.widget.listview.itemview.CheckItemView
import java.util.*

abstract class ListSelectDialog<T, V : View> {
	private val items = ArrayList<T>(64)
	private var alertDialog: AlertDialog? = null
	var title: String? = null

	var multiSelect = false
	var indexSet: MutableSet<Int> = TreeSet()
	private var midButtonTitle: String? = null
	private var midListener: View.OnClickListener? = null

	var onConfigDialog: ((AlertDialog) -> Unit)? = null
	var onConfigTitleView: ((TextView) -> Unit)? = null
	var onConfigListView: ((ListView) -> Unit)? = null
	var onDismiss: (() -> Unit)? = null

	var onMultiSelectIndices: ((Set<Int>) -> Unit)? = null
	var onMultiSelectValues: ((List<T>) -> Unit)? = null
	var onSelectIndex: ((Int) -> Unit)? = null
	var onSelectValue: ((T) -> Unit)? = null

	var argS: String? = null

	private val adapter = object : XBaseAdapter<T>() {
		override fun bindView(position: Int, itemView: View, item: T) {
			if (multiSelect) {
				val checkItemView = itemView as CheckItemView
				val c = indexSet.contains(position)
				checkItemView.isChecked = c
				onBindItemView(checkItemView.itemView as V, item)

			} else {
				onBindItemView(itemView as V, item)
			}
		}

		override fun newView(context: Context, position: Int): View {
			val v = onNewItemView(context, this.getItem(position))
			if (multiSelect) {
				val checkItemView = CheckItemView(context, v)
				checkItemView.setCheckboxMarginRight(15)
				checkItemView.isCheckModel = true
				return checkItemView
			} else {
				return v
			}
		}
	}

	fun setMidButton(title: String, listener: View.OnClickListener) {
		this.midButtonTitle = title
		this.midListener = listener

	}

	fun selectIndex(vararg indexArr: Int) {
		for (index in indexArr) {
			indexSet.add(index)
		}
	}

	fun selectItem(vararg itemArr: T) {
		for (item in itemArr) {
			val index = items.indexOf(item)
			if (index >= 0) {
				indexSet.add(index)
			}
		}
	}

	fun selectItem(ls: Collection<T>) {
		for (item in ls) {
			val index = items.indexOf(item)
			if (index >= 0) {
				indexSet.add(index)
			}
		}
	}

	fun selectAll() {
		for (i in items.indices) {
			indexSet.add(i)
		}
	}

	val selectItems: List<T>
		get() {
			val ls = ArrayList<T>()
			for (n in indexSet) {
				ls.add(items[n])
			}
			return ls
		}


	fun setItems(data: List<T>) {
		items.clear()
		items.addAll(data)
	}

	fun addItems(data: Collection<T>) {
		items.addAll(data)
	}

	fun addItems(vararg data: T) {
		items.addAll(data)
	}

	fun getItem(index: Int): T {
		return items[index]
	}

	private fun hasTitle(): Boolean {
		return title != null && title!!.isNotEmpty()
	}

	protected fun createView(context: Context): View {
		val corner = InputSize.DialogCorner
		val ll = context.createLinearVertical().divider().padding(22, 10, 22, 10)
		val bgList: Drawable
		var listPadTop = 0
		if (hasTitle()) {
			val textView = context.createTextViewA().text(title).textColorWhite().textSizeTitle().padding(15, 0, 0, 0)
			textView.backDrawable(RectDraw(Colors.Theme).corners(corner, corner, 0, 0).value)
			ll.addViewParam(textView) {
				widthFill().height(TitleBar.HEIGHT).gravityLeftCenter()
			}
			onConfigTitleView?.invoke(textView)
			listPadTop = 0
			if (multiSelect) {
				bgList = RectDraw(Colors.WHITE).value
			} else {
				bgList = RectDraw(Colors.WHITE).corners(0, 0, corner, corner).value
			}
		} else {
			listPadTop = corner
			if (multiSelect) {
				bgList = RectDraw(Colors.WHITE).corners(corner, corner, 0, 0).value
			} else {
				bgList = RectDraw(Colors.WHITE).corners(corner, corner, corner, corner).value
			}
		}


		val listView = context.createListView().backDrawable(bgList).padding(0, listPadTop, 0, if (multiSelect) 0 else corner)
		listView.isVerticalScrollBarEnabled = true
		listView.isScrollbarFadingEnabled = false

		adapter.setItems(items)
		listView.adapter = adapter
		onConfigListView?.invoke(listView)
		ll.addViewParam(listView) {
			widthFill().height(0).weight(1)
		}

		listView.setOnItemClickListener({ _, view, position, _ ->
			if (multiSelect) {
				val checkItemView = view as CheckItemView
				checkItemView.toggle()
				if (checkItemView.isChecked) {
					indexSet.add(position)
				} else {
					indexSet.remove(position)
				}
			} else {
				val item = adapter.getItem(position)
				dismiss()
				onSelect(position, item)
			}
		})
		if (multiSelect) {
			ll.addViewParam(createCancelOkView(context, corner)) {
				widthFill().height(InputSize.ButtonHeight)
			}
		}

		return ll
	}

	private fun createCancelOkView(context: Context, corner: Int): View {
		val ll2 = context.createLinearHorizontal().divider()
		val cancelView = context.createTextViewA().gravityCenter().text("取消").textColorMajor().textSizeA().padding(15, 10, 15, 10)
		cancelView.backDrawable(
				RectDraw(Colors.WHITE).corners(0, 0, 0, corner).value,
				RectDraw(Colors.Fade).corners(0, 0, 0, corner).value
		)

		ll2.addViewParam(cancelView) {
			width(0).weight(1).heightFill().gravityCenter()
		}
		if (midListener != null && midButtonTitle != null) {
			val midView = context.createTextViewA().text(midButtonTitle).textColorMajor().textSizeA().padding(15, 10, 15, 10).gravityCenter()
			midView.backDrawable(
					RectDraw(Colors.WHITE).value,
					RectDraw(Colors.Fade).value
			)

			ll2.addViewParam(midView) {
				width(0).weight(1).heightFill().gravityCenter()
			}
			midView.setOnClickListener(midListener)
		}


		val okView = context.createTextViewA().text("确定").textColor(Colors.GreenMajor).textSizeA().padding(15, 10, 15, 10).gravityCenter()
		okView.backDrawable(
				RectDraw(Colors.WHITE).corners(0, 0, corner, 0).value,
				RectDraw(Colors.Fade).corners(0, 0, corner, 0).value
		)

		ll2.addViewParam(okView) {
			width(0).weight(1).heightFill().gravityCenter()
		}

		cancelView.setOnClickListener({ dismiss() })
		okView.setOnClickListener({
			dismiss()
			if (!indexSet.isEmpty()) {
				onMultiSelect(indexSet)
			}
		})
		return ll2
	}


	fun show(context: Context, title: String): AlertDialog {
		this.title = title
		return show(context)
	}

	fun show(context: Context): AlertDialog {
		adapter.setItems(items)
		val builder = AlertDialog.Builder(context)
		builder.setView(createView(context))
		builder.setCancelable(true)
		val dlg = builder.create()
		this.alertDialog = dlg
		dlg.setCanceledOnTouchOutside(true)
		dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		onConfigDialog?.invoke(dlg)
		dlg.setOnDismissListener {
			onDismiss?.invoke()
		}
		dlg.show()
		return dlg
	}

	fun dismiss() {
		alertDialog?.dismiss()
	}

	fun gravityTop(dlg: AlertDialog, yMargin: Int) {
		val lp = dlg.window?.attributes ?: return
		lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
		lp.y = dp(yMargin)
		dlg.window!!.attributes = lp
	}

	open protected fun onSelect(position: Int, item: T) {
		onSelectIndex?.invoke(position)
		onSelectValue?.invoke(item)
	}

	open protected fun onMultiSelect(indexSet: Set<Int>) {
		onMultiSelectIndices?.invoke(indexSet)
		onMultiSelectValues?.invoke(selectItems)
	}

	abstract fun onNewItemView(context: Context, item: T): V

	abstract fun onBindItemView(itemView: V, item: T)

}

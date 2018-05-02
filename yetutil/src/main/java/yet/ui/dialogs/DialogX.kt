package yet.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import yet.theme.Colors
import yet.theme.ViewSize
import yet.ui.ext.*
import yet.ui.list.CheckListView
import yet.ui.list.SimpleListView
import yet.ui.res.D
import yet.ui.res.Shapes
import yet.ui.viewcreator.*
import yet.ui.widget.TitleBar

class DialogX(val context: Context) {
	var titleHeight = 45
	val dlg = Dialog(context)
	val dlgParam: WindowManager.LayoutParams = dlg.window?.attributes!!
	val rootLayout = context.createLinearVertical()
	var bodyView: View = context.createTextViewB().textColorMajor()
	var bodyViewParam: LinearLayout.LayoutParams = LParam.WidthFill.HeightWrap
	var onDismiss: (DialogX) -> Unit = {}
	var preDismiss: (DialogX) -> Unit = {}


	var title: String? = null
	var buttons = ArrayList<DialogButton>()
	val result = ArrayList<Any>()

	var argS: String = ""
	var argN = 0

	init {
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
		rootLayout.divider()
		rootLayout.padding(20, 0, 20, 0)
		dlg.setCancelable(true)
		dlg.setCanceledOnTouchOutside(true)
		dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dlgParam.widthFill()
		dlgParam.heightWrap()
	}


	fun title(title: String?) {
		this.title = title
	}

	fun button(text: String, color: Int, block: (String) -> Unit): DialogButton {
		val b = DialogButton(text, color, block)
		buttons.add(b)
		return b
	}

	fun risk(text: String, block: (String) -> Unit) {
		button(text, Colors.Risk, block)
	}

	fun safe(text: String, block: (String) -> Unit) {
		button(text, Colors.GreenMajor, block)
	}

	fun normal(text: String, block: (String) -> Unit) {
		button(text, Colors.TextColorMajor, block)
	}

	fun cancel(text: String = "取消") {
		normal(text) {}
	}

	fun ok(text: String = "确定", block: (String) -> Unit) {
		safe(text, block)
	}

	fun buttons(block: DialogX.() -> Unit) {
		this.block()
	}


	fun body(view: View) {
		this.bodyView = view
	}

	fun body(block: (DialogX) -> View) {
		this.bodyView = block(this)
	}

	fun bodyText(text: String, block: TextView.() -> Unit = {}) {
		val tv = context.createTextViewB()
		tv.padding(15, 10, 15, 10)
		tv.text = text
		tv.gravityCenter()
		tv.block()
		body(tv)
	}

	fun bodyInput(block: EditText.() -> Unit) {
		val rl = context.createRelative()
		val ed = rl.editX(RParam.CenterInParent.WidthFill.HeightWrap.margins(15)) {
			minimumWidth = dp(200)
			minimumHeight = dp(ViewSize.EditHeight)
			this.block()
		}
		preDismiss = {
			result.add(ed.textS)
		}
		body(rl)
	}

	fun bodyInputLines(block: EditText.() -> Unit) {
		val rl = context.createRelative()
		val ed = rl.edit(RParam.CenterInParent.WidthFill.HeightWrap.margins(15)) {
			minimumWidth = dp(200)
			minimumHeight = dp(ViewSize.EditHeight)
			this.multiLine()
			minLines = 5
			gravityTopLeft()
			padding(5)
			this.block()
		}
		preDismiss = {
			result.add(ed.textS)
		}
		body(rl)
	}

	fun bodyList(): SimpleListView {
		bodyViewParam = LParam.WidthFill.HeightFlex
		val lv = SimpleListView(context)
		body(lv)
		return lv
	}

	fun bodyListString(block: (Any) -> String = { it.toString() }): SimpleListView {
		val lv = bodyList()
		lv.anyAdapter.onBindView = { v, p ->
			(v as TextView).text = block(lv.anyAdapter.item(p))
		}
		return lv
	}

	fun bodyCheck(): CheckListView {
		bodyViewParam = LParam.WidthFill.HeightFlex
		val lv = CheckListView(context)
		body(lv)
		return lv
	}

	fun bodyCheckString(block: (Any) -> String = { it.toString() }): CheckListView {
		val lv = bodyCheck()
		lv.onBindView = { v, p ->
			(v as TextView).text = block(lv.anyAdapter.item(p))
		}
		return lv
	}


	private fun createView() {
		val hasTitle = title != null && title!!.isNotEmpty()
		val hasButton = buttons.isNotEmpty()
		if (hasTitle) {
			rootLayout.textView(LParam.WidthFill.height(titleHeight)) {
				textColorWhite().textSizeTitle()
				backDrawable(Shapes.rect {
					this.fillColor = Colors.Theme
					this.cornerListDp(ViewSize.DialogCorner, ViewSize.DialogCorner, 0, 0)
				})
				if (TitleBar.TitleCenter) {
					gravityCenter()
				} else {
					padding(15, 0, 0, 0)
					gravityLeftCenter()
				}
				textS = title!!
			}
		}
		var c1 = 0
		var c2 = 0
		if (!hasTitle) {
			c1 = ViewSize.DialogCorner
		}
		if (!hasButton) {
			c2 = ViewSize.DialogCorner
		}
		bodyView.minimumHeight = dp(60)
		bodyView.minimumWidth = dp(240)
		bodyView.backDrawable(Shapes.rect {
			fillColor = Colors.WHITE
			cornerListDp(c1, c1, c2, c2)
		})
		rootLayout.addView(bodyView, bodyViewParam)
		createButtonsView()
	}

	private fun createButtonsView() {
		if (buttons.isEmpty()) {
			return
		}
		rootLayout.linearHor(LParam.WidthFill.HeightButton) {
			divider()
			for (b in buttons) {
				textView(LParam.gravityCenter().HeightFill.WidthFlex) {
					textSizeA()
					textColor(b.color)
					backColor(b.backColor)
					padding(15, 10, 15, 10)
					gravityCenter()
					textS = b.text
					onClick {

						dismiss()
						b.callback(b.text)
					}
				}
			}
			if (buttons.size == 1) {
				val d1 = Shapes.rect {
					fillColor = buttons[0].backColor
					cornerListDp(0, 0, ViewSize.DialogCorner, ViewSize.DialogCorner)
				}
				val d2 = Shapes.rect {
					fillColor = Colors.Fade
					cornerListDp(0, 0, ViewSize.DialogCorner, ViewSize.DialogCorner)
				}
				getChildAt(0).backDrawable(D.light(d1, d2))
			} else { //>=2
				val d1 = Shapes.rect {
					fillColor = buttons[0].backColor
					cornerListDp(0, 0, 0, ViewSize.DialogCorner)
				}
				val d2 = Shapes.rect {
					fillColor = Colors.Fade
					cornerListDp(0, 0, 0, ViewSize.DialogCorner)
				}
				getChildAt(0).backDrawable(D.light(d1, d2))
				val d3 = Shapes.rect {
					fillColor = buttons[0].backColor
					cornerListDp(0, 0, ViewSize.DialogCorner, 0)
				}
				val d4 = Shapes.rect {
					fillColor = Colors.Fade
					cornerListDp(0, 0, ViewSize.DialogCorner, 0)
				}
				getChildAt(childCount - 1).backDrawable(D.light(d3, d4))
			}
		}
	}


	fun show() {
		createView()
		dlg.setContentView(rootLayout)
		dlg.setOnDismissListener {
			onDismiss(this)
		}
		dlg.show()
	}


	fun dismiss() {
		preDismiss(this@DialogX)
		dlg.dismiss()
	}

	fun gravityTop(yMargin: Int) {
		dlgParam.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
		dlgParam.y = dp(yMargin)
		dlg.window!!.attributes = dlgParam
	}

	class DialogButton(val text: String, val color: Int, val callback: (String) -> Unit) {
		var backColor: Int = Colors.WHITE
	}


	companion object {
		fun show(context: Context, block: DialogX.() -> Unit) {
			val d = DialogX(context)
			d.block()
			d.show()
		}

		fun alert(context: Context, msg: String) {
			alert(context, msg, null)
		}

		fun alert(context: Context, msg: String, title: String?, dismissCallback: () -> Unit = {}) {
			val d = DialogX(context)
			d.title(title)
			d.bodyText(msg) {
				if (msg.length < 16) {
					gravityCenter()
				} else {
					gravityLeftCenter()
				}
			}
			d.ok {}
			d.onDismiss = {
				dismissCallback()
			}
			d.show()
		}

		fun confirm(context: Context, msg: String, title: String?, onOK: () -> Unit) {
			val d = DialogX(context)
			d.title(title)
			d.bodyText(msg)
			d.cancel()
			d.ok {
				onOK()
			}
			d.show()
		}

		fun input(context: Context, title: String?, value: String = "", onOK: (String) -> Unit) {
			val d = DialogX(context)
			d.title(title)
			d.bodyInput {
				this.textS = value
			}
			d.cancel()
			d.ok {
				val s = d.result.first() as String
				onOK(s)
			}
			d.show()
		}

		fun inputLines(context: Context, title: String?, value: String = "", onOK: (String) -> Unit) {
			val d = DialogX(context)
			d.title(title)
			d.bodyInputLines {
				this.textS = value
			}
			d.cancel()
			d.ok {
				val s = d.result.first() as String
				onOK(s)
			}
			d.show()
		}

		fun listText(context: Context, items: List<Any>, title: String? = "选择", textBlock: (Any) -> String, onResult: (Any) -> Unit) {
			val d = DialogX(context)
			d.title(title)
			val lv = d.bodyListString(textBlock)
			lv.setItems(items)
			lv.onItemClick = {
				d.dismiss()
				onResult(it)
			}
			d.show()
		}

		fun listString(context: Context, items: List<String>, title: String? = "选择", onResult: (String) -> Unit) {
			listText(context, items, title, { it.toString() }) {
				onResult(it as String)
			}
		}

		fun listTextN(context: Context, items: List<Any>, title: String? = "选择", textBlock: (Any) -> String, onResult: (Int) -> Unit) {
			val d = DialogX(context)
			d.title(title)
			val lv = d.bodyListString(textBlock)
			lv.setItems(items)
			lv.onItemClick2 = { v, item, p ->
				d.dismiss()
				onResult(p)
			}
			d.show()
		}

		fun listStringN(context: Context, items: List<String>, title: String? = "选择", onResult: (Int) -> Unit) {
			listTextN(context, items, title, { it.toString() }, onResult)
		}

		fun checkText(context: Context, items: List<Any>, title: String = "选择", textBlock: (Any) -> String, onResult: (List<Any>) -> Unit) {
			val d = DialogX(context)
			d.title(title)
			val lv = d.bodyCheckString(textBlock)
			lv.setItems(items)
			d.cancel()
			d.ok {
				onResult(lv.anyAdapter.checkedItems)
			}
			d.show()
		}

		fun checkString(context: Context, items: List<String>, title: String = "选择", onResult: (List<String>) -> Unit) {
			checkText(context, items, title, { it.toString() }) {
				onResult(it.map { it as String })
			}
		}

		fun checkTextN(context: Context, items: List<Any>, title: String = "选择", textBlock: (Any) -> String, onResult: (Set<Int>) -> Unit) {
			val d = DialogX(context)
			d.title(title)
			val lv = d.bodyCheckString(textBlock)
			lv.setItems(items)
			d.cancel()
			d.ok {
				onResult(lv.anyAdapter.checkedIndexs)
			}
			d.show()
		}

		fun checkStringN(context: Context, items: List<String>, title: String = "选择", onResult: (Set<Int>) -> Unit) {
			checkTextN(context, items, title, { it.toString() }, onResult)
		}
	}

}

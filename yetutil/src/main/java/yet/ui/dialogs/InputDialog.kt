package yet.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.EditText
import yet.theme.Colors
import yet.theme.InputSize
import yet.ui.ext.*
import yet.ui.res.RectDraw
import yet.ui.viewcreator.*

class InputDialog {
	var CORNER = InputSize.DialogCorner
	var TITLE_HEIGHT = 45
	var OK_COLOR = Colors.GreenMajor
	var MARGIN_HOR = 25

	private var alertDialog: Dialog? = null
	var title: String? = null
	var text: String? = null
	var hint: String? = null
	var midButtonText: String? = null
	var okButtonText: String? = "确定"
	var cancelButtonText: String? = "取消"

	var argS: String? = null
	var argObj: Any? = null
	var argN = 0

	var multiline: Boolean = false

	private var editText: EditText? = null


	var inputType = InputType.TYPE_CLASS_TEXT

	fun multiline() {
		this.multiline = true
	}

	fun inputTypeText() {
		this.inputType = InputType.TYPE_CLASS_TEXT
	}

	fun inputTypePhone() {
		this.inputType = InputType.TYPE_CLASS_PHONE
	}

	fun inputTypeNumber() {
		this.inputType = InputType.TYPE_CLASS_NUMBER
	}

	fun inputTypeNumberFloat() {
		this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
	}

	fun inputTypePassword() {
		this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
	}

	fun inputTypePasswordNumber() {
		this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
	}

	fun inputTypeEmail() {
		this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
	}


	val inputText: String
		get() {
			if (editText != null) {
				return editText!!.text.toString()
			}
			return ""
		}

	val trimText: String
		get() {
			if (editText != null) {
				return editText!!.text.toString().trim { it <= ' ' }
			}
			return ""
		}

	fun safe(): InputDialog {
		this.OK_COLOR = Colors.GreenMajor
		return this
	}

	fun risk(): InputDialog {
		this.OK_COLOR = Colors.Risk
		return this
	}


	fun title(title: String?): InputDialog {
		this.title = title
		return this
	}

	fun text(text: String?): InputDialog {
		this.text = text
		return this
	}

	fun hint(hint: String?): InputDialog {
		this.hint = hint
		return this
	}


	fun ok(text: String?): InputDialog {
		this.okButtonText = text
		return this
	}

	fun cancel(text: String?): InputDialog {
		this.cancelButtonText = text
		return this
	}

	fun mid(text: String?): InputDialog {
		this.midButtonText = text
		return this
	}


	private fun createView(context: Context): View {
		val ll = context.createLinearVertical()
		ll.padding(0)
		ll.divider()
		if (title != null) {
			val textView = context.createTextViewA()
			val bgText = RectDraw(Colors.Theme).corners(CORNER, CORNER, 0, 0).value
			textView.setText(title)
			textView.textColorWhite().textSizeTitle().backDrawable(bgText).padding(15, 0, 0, 0)
			ll.addView(textView) { widthFill().height(TITLE_HEIGHT).gravityLeftCenter() }
		}

		editText = context.createEdit()
		editText!!.setText(text)
		editText!!.hint = hint
		editText!!.inputType = inputType
		if (multiline) {
			editText!!.gravityTopLeft()
			editText!!.multiLine()
		}
		val ll2 = context.createLinearHorizontal()
		ll2.addView(editText!!) {
			gravityCenter().widthFill()
			if (multiline) {
				height(InputSize.EditHeight * 3)
			} else {
				height(InputSize.EditHeight)
			}
		}
		ll.addView(ll2) { widthFill().heightWrap() }

		val vw = ll2.padding(15, 25, 15, 25).gravityCenter()
		if (title == null) {
			vw.backDrawable(RectDraw(Colors.WHITE).corners(CORNER, CORNER, 0, 0).value)
		} else {
			vw.backDrawable(RectDraw(Colors.WHITE).value)
		}

		val buttonsView = createButtonsView(context)
		ll.addView(buttonsView) { widthFill().height(InputSize.ButtonHeight) }
		return ll
	}

	private fun createButtonsView(context: Context): View {
		val ll2 = context.createLinearHorizontal()
		ll2.divider()
		if (cancelButtonText != null) {
			val cancelView = context.createTextViewA()
			cancelView.setText(cancelButtonText)
			val cornerRight = if (midButtonText == null && okButtonText == null) CORNER else 0
			cancelView.textColorMajor()
					.textSizeA()
					.backDrawable(
							RectDraw(Colors.WHITE).corners(0, 0, cornerRight, CORNER).value,
							RectDraw(Colors.Fade).corners(0, 0, cornerRight, CORNER).value
					)
					.padding(15, 10, 15, 10)
					.gravityCenter()
			ll2.addView(cancelView) { width(0).weight(1).heightFill().gravityCenter() }
			cancelView.setOnClickListener(View.OnClickListener {
				dismiss()
				onCancel()
			})
		}
		if (midButtonText != null) {
			val midView = context.createTextViewA()
			midView.setText(midButtonText)
			val leftCorner = if (cancelButtonText == null) CORNER else 0
			val rightCorner = if (okButtonText == null) CORNER else 0
			midView.textColorMajor()
					.textSizeA()
					.backDrawable(
							RectDraw(Colors.WHITE).corners(0, 0, rightCorner, leftCorner).value,
							RectDraw(Colors.Fade).corners(0, 0, rightCorner, leftCorner).value
					)
					.padding(15, 10, 15, 10)
					.gravityCenter()
			ll2.addView(midView) { width(0).weight(1).heightFill().gravityCenter() }
			midView.setOnClickListener(View.OnClickListener {
				dismiss()
				onMid()
			})
		}

		if (okButtonText != null) {
			val okView = context.createTextViewA()
			okView.setText(okButtonText)
			val leftCorner = if (cancelButtonText == null && midButtonText == null) CORNER else 0
			okView.textColor(OK_COLOR)
					.textSizeA()
					.backDrawable(
							RectDraw(Colors.WHITE).corners(0, 0, CORNER, leftCorner).value,
							RectDraw(Colors.Fade).corners(0, 0, CORNER, leftCorner).value
					)
					.padding(15, 10, 15, 10)
					.gravityCenter()
			ll2.addView(okView) { width(0).weight(1).heightFill().gravityCenter() }
			okView.setOnClickListener(View.OnClickListener {
				dismiss()
				onOK(inputText)
			})
		}
		return ll2
	}

	fun show(context: Context, title: String) {
		title(title)
		show(context)
	}

	fun show(context: Context, title: String, value: String) {
		title(title).text(value)
		show(context)
	}

	fun show(context: Context): Dialog {
		val dlg = Dialog(context)
		alertDialog = dlg
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val view = createView(context)
		val rootLayout = context.createRelative()
		rootLayout.addViewParam(view) {
			widthFill().heightWrap().margins(MARGIN_HOR, 0, MARGIN_HOR, 0).centerInParent()
		}
		dlg.setContentView(rootLayout)
		dlg.setCancelable(true)
		dlg.setCanceledOnTouchOutside(true)
		dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		val p = dlg.window?.attributes!!
		p.widthFill()
		p.heightWrap()
		dlg.setOnDismissListener { this@InputDialog.onDismiss() }
		onConfigDialog(dlg)
		dlg.show()

		return dlg
	}

	fun dismiss() {
		alertDialog?.dismiss()
	}

	fun gravityTop(dlg: Dialog, yMargin: Int) {
		val lp = dlg.window!!.attributes
		if (lp != null) {
			lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
			lp.y = dp(yMargin)
			dlg.window!!.attributes = lp
		}
	}

	var onConfigDialog: (Dialog) -> Unit = {}
	var onDismiss: () -> Unit = {}
	var onCancel: () -> Unit = {}
	var onMid: () -> Unit = {}
	var onOK: (String) -> Unit = {}
}

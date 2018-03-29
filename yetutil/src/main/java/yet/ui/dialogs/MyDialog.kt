package yet.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import yet.theme.*
import yet.ui.ext.*
import yet.ui.util.RectDrawable
import yet.ui.viewcreator.*

open class MyDialog {
	var CORNER = InputSize.DialogCorner
	var TITLE_HEIGHT = 45
	var OK_COLOR = Colors.GreenMajor
	var MARGIN_HOR = 25

	private var alertDialog: Dialog? = null
	var title: String? = null
	var msg: String? = null
	var midButtonText: String? = null
	var okButtonText: String? = Str.OK
	var cancelButtonText: String? = Str.CANCEL


	var onConfigDialog: (Dialog) -> Unit = {}


	var onDismiss: () -> Unit = {}
	var onCancel: () -> Unit = {}
	var onMid: () -> Unit = {}
	var onOK: () -> Unit = {}

	var argS: String? = null
	var argObj: Any? = null
	var argN = 0


	fun safe(): MyDialog {
		this.OK_COLOR = Colors.GreenMajor
		return this
	}

	fun risk(): MyDialog {
		this.OK_COLOR = Colors.Risk
		return this
	}


	fun title(title: String?): MyDialog {
		this.title = title
		return this
	}

	fun msg(msg: String?): MyDialog {
		this.msg = msg
		return this
	}

	fun ok(text: String?): MyDialog {
		this.okButtonText = text
		return this
	}

	fun cancel(text: String?): MyDialog {
		this.cancelButtonText = text
		return this
	}

	fun mid(text: String?): MyDialog {
		this.midButtonText = text
		return this
	}


	protected fun createView(context: Context): View {
		val ll = context.createLinearVertical()
		ll.padding(0)
		ll.divider()
		if (title != null) {
			val textView = context.createTextViewA()
			val bgText = RectDrawable(Colors.Theme).corners(CORNER, CORNER, 0, 0).value
			textView.setText(title)
			textView.textColorWhite().textSizeTitle().backDrawable(bgText).padding(15, 0, 0, 0)
			ll.addView(textView) { widthFill().height(TITLE_HEIGHT).gravityLeftCenter() }
		}
		if (msg != null) {
			val msgView = context.createTextViewA()
			msgView.setText(msg)
			val vw = msgView.textColorMajor().padding(15, 10, 15, 10).gravityCenter()
			vw.miniHeightDp(80)
			if (title == null) {
				vw.backDrawable(RectDrawable(Colors.WHITE).corners(CORNER, CORNER, 0, 0).value)
			} else {
				vw.backDrawable(RectDrawable(Colors.WHITE).value)
			}
			ll.addView(msgView) { widthFill().heightWrap() }
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
							RectDrawable(Colors.WHITE).corners(0, 0, cornerRight, CORNER).value,
							RectDrawable(Colors.Fade).corners(0, 0, cornerRight, CORNER).value
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
							RectDrawable(Colors.WHITE).corners(0, 0, rightCorner, leftCorner).value,
							RectDrawable(Colors.Fade).corners(0, 0, rightCorner, leftCorner).value
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
							RectDrawable(Colors.WHITE).corners(0, 0, CORNER, leftCorner).value,
							RectDrawable(Colors.Fade).corners(0, 0, CORNER, leftCorner).value
					)
					.padding(15, 10, 15, 10)
					.gravityCenter()
			ll2.addView(okView) { width(0).weight(1).heightFill().gravityCenter() }
			okView.setOnClickListener(View.OnClickListener {
				dismiss()
				onOK()
			})
		}
		return ll2
	}

	fun show(context: Context, title: String?, msg: String?) {
		title(title).msg(msg)
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
		p.horizontalMargin = dp(30).toFloat()
		dlg.setOnDismissListener { this@MyDialog.onDismiss() }
		onConfigDialog(dlg)
		dlg.show()
		return dlg
	}

	fun dismiss() {
		val d = alertDialog
		d?.dismiss()
	}

	fun gravityTop(dlg: Dialog, yMargin: Int) {
		val lp = dlg.window?.attributes
		if (lp != null) {
			lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
			lp.y = dp(yMargin)
			dlg.window!!.attributes = lp
		}
	}


}

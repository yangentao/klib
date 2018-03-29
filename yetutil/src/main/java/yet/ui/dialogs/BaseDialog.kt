package yet.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import yet.theme.*
import yet.ui.ext.*
import yet.ui.util.RectDrawable
import yet.ui.viewcreator.*

open class BaseDialog {
	var cornerOfDialog = InputSize.DialogCorner
	var titleHeight = 45

	var okColor = Colors.GreenMajor
	var midColor = TextColor.Primary
	var cancelColor = TextColor.Primary
	var okBackColor = Colors.WHITE
	var midBackColor = Colors.WHITE
	var cancelBackColor = Colors.WHITE

	var cancelButtonText: String? = null
	var midButtonText: String? = null
	var okButtonText: String? = null

	var marginLeft = 25
	var magtinRight = 25
	var marginTop = 0
	var marginBottom = 0

	var bodyBackColor: Int = Colors.WHITE

	var dialog: Dialog? = null
	var title: String? = null


	var bodyView: View? = null


	var onConfigDialog: (Dialog) -> Unit = {}
	var onLayoutDialog: (WindowManager.LayoutParams) -> Unit = {}


	var onDismiss: () -> Unit = {}
	var onCancel: () -> Unit = {}
	var onMid: () -> Unit = {}
	var onOK: () -> Unit = {}

	var argS: String? = null
	var argObj: Any? = null
	var argN = 0


	fun okSafe(): BaseDialog {
		this.okColor = Colors.GreenMajor
		return this
	}

	fun okRisk(): BaseDialog {
		this.okColor = Colors.Risk
		return this
	}


	fun title(title: String?): BaseDialog {
		this.title = title
		return this
	}


	fun ok(text: String?): BaseDialog {
		this.okButtonText = text
		return this
	}

	fun cancel(text: String?): BaseDialog {
		this.cancelButtonText = text
		return this
	}

	fun mid(text: String?): BaseDialog {
		this.midButtonText = text
		return this
	}

	protected fun createMessageView(context: Context, msg:String): TextView {
		val tv = context.createTextViewA()
		tv.padding(15, 10, 15, 10)
		tv.text = msg
		tv.gravityCenter()
		return tv
	}


	protected fun createView(context: Context, rootLayout: LinearLayout, bodyView: View) {
		if (title != null) {
			val textView = context.createTextViewA().text(title)
			val bgText = RectDrawable(Colors.Theme).corners(cornerOfDialog, cornerOfDialog, 0, 0).value
			textView.textColorWhite().textSizeTitle().backDrawable(bgText).padding(15, 0, 0, 0)
			rootLayout.addView(textView) { widthFill().height(titleHeight).gravityLeftCenter() }
		}
		val buttonsView = createButtonsView(context)

		val bg = if (title == null) {
			if (buttonsView == null) {
				RectDrawable(bodyBackColor).corners(cornerOfDialog, cornerOfDialog, cornerOfDialog, cornerOfDialog).value
			} else {
				RectDrawable(bodyBackColor).corners(cornerOfDialog, cornerOfDialog, 0, 0).value
			}
		} else {
			if (buttonsView == null) {
				RectDrawable(bodyBackColor).corners(0, 0, cornerOfDialog, cornerOfDialog).value
			} else {
				RectDrawable(bodyBackColor).value
			}
		}
		bodyView.minimumHeight = dp(80)
		bodyView.backDrawable(bg)
		rootLayout.addView(bodyView) { widthFill().heightWrap() }
		if (buttonsView != null) {
			rootLayout.addView(buttonsView) { widthFill().height(InputSize.ButtonHeight) }
		}
	}

	private fun createButtonsView(context: Context): View? {
		if (cancelButtonText == null && midButtonText == null && okButtonText == null) {
			return null
		}
		val ll2 = context.createLinearHorizontal()
		ll2.divider()
		if (cancelButtonText != null) {
			val cancelView = context.createTextViewA().textColor(cancelColor).text(cancelButtonText).padding(15, 10, 15, 10).gravityCenter()
			val cornerRight = if (midButtonText == null && okButtonText == null) cornerOfDialog else 0
			cancelView.backDrawable(
					RectDrawable(cancelBackColor).corners(0, 0, cornerRight, cornerOfDialog).value,
					RectDrawable(Colors.Fade).corners(0, 0, cornerRight, cornerOfDialog).value
			)

			ll2.addView(cancelView) { width(0).weight(1).heightFill().gravityCenter() }
			cancelView.setOnClickListener({
				dismiss()
				onCancel()
			})
		}
		if (midButtonText != null) {
			val midView = context.createTextViewA().textColor(midColor).text(midButtonText).padding(15, 10, 15, 10).gravityCenter()
			val leftCorner = if (cancelButtonText == null) cornerOfDialog else 0
			val rightCorner = if (okButtonText == null) cornerOfDialog else 0
			midView.backDrawable(
					RectDrawable(midBackColor).corners(0, 0, rightCorner, leftCorner).value,
					RectDrawable(Colors.Fade).corners(0, 0, rightCorner, leftCorner).value
			)

			ll2.addView(midView) { width(0).weight(1).heightFill().gravityCenter() }
			midView.setOnClickListener({
				dismiss()
				onMid()
			})
		}

		if (okButtonText != null) {
			val okView = context.createTextViewA().textColor(okColor).text(okButtonText).padding(15, 10, 15, 10).gravityCenter()
			val leftCorner = if (cancelButtonText == null && midButtonText == null) cornerOfDialog else 0
			okView.backDrawable(
					RectDrawable(okBackColor).corners(0, 0, cornerOfDialog, leftCorner).value,
					RectDrawable(Colors.Fade).corners(0, 0, cornerOfDialog, leftCorner).value
			)

			ll2.addView(okView) { width(0).weight(1).heightFill().gravityCenter() }
			okView.setOnClickListener({
				dismiss()
				onOK()
			})
		}
		return ll2
	}


	fun show(context: Context, bodyView: View): Dialog {
		this.bodyView = bodyView
		val dlg = Dialog(context)
		dialog = dlg
		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
		val rootLayout = context.createLinearVertical()
		rootLayout.divider()
		createView(context, rootLayout, bodyView)
		rootLayout.padding(marginLeft, marginTop, magtinRight, marginBottom)
		dlg.setContentView(rootLayout)
		dlg.setCancelable(true)
		dlg.setCanceledOnTouchOutside(true)
		dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dlg.setOnDismissListener { onDismiss() }
		val lp = dlg.window?.attributes!!
		lp.widthFill()
		lp.heightWrap()
		onLayoutDialog(lp)
		onConfigDialog(dlg)
		dlg.show()
		return dlg
	}

	fun dismiss() {
		dialog?.dismiss()
	}
}

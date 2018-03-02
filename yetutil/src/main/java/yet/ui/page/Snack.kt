package yet.ui.page

import android.content.Context
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import yet.theme.Colors
import yet.ui.ext.*
import yet.util.IdGen
import yet.util.RunTask
import yet.util.TaskUtil
import yet.util.Util

/**
 * Created by yet on 2015/10/20.
 */
class Snack(context: Context) : LinearLayout(context) {
	interface SnackCallback {
		fun onAction(action: String)
	}

	var textView: TextView = createTextViewB()
	var cancelButton: TextView = TextView(context)
	var okButton: TextView = TextView(context)
	private var callback: SnackCallback? = null
	private val gd: GestureDetector

	private val clickListener = View.OnClickListener { v ->
		val action = okButton.text.toString()
		val c = callback
		hide()
		if (v === okButton && c != null) {
			TaskUtil.fore(object : RunTask() {
				@Throws(Exception::class)
				override fun onRun() {
					c.onAction(action)
				}
			})
		}
	}

	init {
		horizontal()
		padding(10, 5, 10, 5)
		backColor(Color.DKGRAY)
		gravityCenterVertical()

		textView.textColorWhite().padding(0, 10, 0, 10)

		cancelButton.padding(15, 0, 15, 0).text("取消").gone().textSizeA().textColorWhite().backColor(Color.TRANSPARENT, Colors.Fade).gravityCenter()
		cancelButton.setOnClickListener(clickListener)

		okButton.padding(15, 0, 15, 0).gone().textSizeA().textColorWhite().backColor(Color.TRANSPARENT, Colors.Fade).gravityCenter()
		okButton.setOnClickListener(clickListener)

		addView(textView, linearParam().heightWrap().width(0).weight(1f).gravityCenterVertical())
		addView(cancelButton, linearParam().height(45).widthWrap().gravityCenterVertical())
		addView(okButton, linearParam().height(45).widthWrap().gravityCenterVertical())
		linearParam().widthFill().heightWrap().gravityCenterVertical().set(this)

		gd = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
			override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
				TaskUtil.fore(Runnable { hide() })
				return true
			}
		})
		this.isLongClickable = true
		this.setOnTouchListener({ v, event -> gd.onTouchEvent(event) })

	}


	fun msg(msg: String): Snack {
		textView.text = msg
		return this
	}

	fun cancelable(showCancel: Boolean): Snack {
		cancelButton.visibility = if (showCancel) View.VISIBLE else View.GONE
		return this
	}

	fun action(action: String?, callback: SnackCallback?): Snack {
		okButton.text = action
		this.callback = callback
		if (Util.empty(action) || callback == null) {
			okButton.visibility = View.GONE
		} else {
			okButton.visibility = View.VISIBLE
		}
		return this
	}

	fun show(seconds: Int = 0) {
		var seconds = seconds
		this.visibility = View.VISIBLE
		if (seconds >= 0) {
			if (seconds == 0) {
				seconds = defSeconds(textView.text.toString().length)
			}
			RunTask.cancel(GROUP)
			TaskUtil.foreDelay((seconds * 1000).toLong(), object : RunTask() {
				@Throws(Exception::class)
				override fun onRun() {
					hide()
				}
			}).addGroup(GROUP)
		} else {
			cancelable(true)
		}
	}

	private fun defSeconds(msgLength: Int): Int {
		return SECONDS * (msgLength / 10 + 1)
	}

	fun show(timeoutSeconds: Int, msg: String, showCancel: Boolean, action: String, callback: SnackCallback) {
		msg(msg).cancelable(showCancel).action(action, callback).show(timeoutSeconds)
	}

	fun show(msg: String) {
		msg(msg).show()
	}

	fun hide() {
		cancelable(false).action(null, null)
		visibility = View.GONE
	}

	companion object {

		private val SECONDS = 5
		private val GROUP = "snack" + IdGen.gen()
	}

}
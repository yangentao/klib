package yet.ui.widget.recycler

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.res.D


/**
 * Created by yet on 2015/10/28.
 */
class TextItemAction(context: Context) : TextView(context) {

	init {
		miniWidth(60).miniHeight(40).textSizeNormal().textColor(Colors.TextColorMinor).gravityCenter()
		linearParam().widthWrap().heightFill().set(this)
		padding(10, 0, 10, 0)
	}

	fun text(text: String): TextItemAction {
		setText(text)
		return this
	}

	fun text(): String {
		return this.text.toString()
	}

	fun textSize(sp: Int): TextItemAction {
		setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.toFloat())
		return this
	}

	fun textColor(color: Int): TextItemAction {
		setTextColor(color)
		return this
	}

	fun miniWidth(dp: Int): TextItemAction {
		minWidth = dp(dp)
		return this
	}

	fun miniHeight(dp: Int): TextItemAction {
		minHeight = dp(dp)
		return this
	}

	fun backColor(normal: Int, pressed: Int): TextItemAction {
		setBackgroundDrawable(D.lightColor(normal, pressed))
		return this
	}

	fun gravityCenter(): TextItemAction {
		gravity = Gravity.CENTER
		return this
	}

	fun gravityCenterVer(): TextItemAction {
		gravity = Gravity.CENTER_VERTICAL
		return this
	}

	fun gravityCenterHor(): TextItemAction {
		gravity = Gravity.CENTER_HORIZONTAL
		return this
	}

	fun gravityLeftCenter(): TextItemAction {
		gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
		return this
	}

	fun gravityRightCenter(): TextItemAction {
		gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
		return this
	}

	fun padding(leftDp: Int, topDp: Int, righDp: Int, bottomDp: Int): TextItemAction {
		setPadding(dp(leftDp), dp(topDp), dp(righDp), dp(bottomDp))
		return this
	}
}

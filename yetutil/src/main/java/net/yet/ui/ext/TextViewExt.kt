package net.yet.ui.ext

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import net.yet.ext.ColorList
import net.yet.theme.Colors
import net.yet.theme.Dim
import net.yet.theme.Space
import net.yet.ui.res.Img
import net.yet.util.Util
import net.yet.util.app.App

/**
 * Created by entaoyang@163.com on 16/3/12.
 */
fun <T : TextView> T.hideInputMethod(): T {
	Util.hideInputMethod(this)
	return this
}

fun <T : TextView> T.imeAction(action: Int, block: (TextView) -> Unit): T {
	this.setOnEditorActionListener(object : TextView.OnEditorActionListener {
		override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
			if (actionId == action) {
				if (v != null) {
					block(v)
				}
				return true
			}
			return false
		}

	})
	return this
}

fun <T : TextView> T.imeDone(block: (TextView) -> Unit): T {
	this.imeOptions = EditorInfo.IME_ACTION_DONE
	this.imeAction(EditorInfo.IME_ACTION_DONE, block)
	return this
}

fun <T : TextView> T.imeGo(block: (TextView) -> Unit): T {
	this.imeOptions = EditorInfo.IME_ACTION_GO
	this.imeAction(EditorInfo.IME_ACTION_GO, block)
	return this
}

fun <T : TextView> T.imeNext(block: (TextView) -> Unit): T {
	this.imeOptions = EditorInfo.IME_ACTION_NEXT
	this.imeAction(EditorInfo.IME_ACTION_NEXT, block)
	return this
}

fun <T : TextView> T.imeSearch(block: (TextView) -> Unit): T {
	this.imeOptions = EditorInfo.IME_ACTION_SEARCH
	this.imeAction(EditorInfo.IME_ACTION_SEARCH, block)
	return this
}

fun <T : TextView> T.imeSend(block: (TextView) -> Unit): T {
	this.imeOptions = EditorInfo.IME_ACTION_SEND
	this.imeAction(EditorInfo.IME_ACTION_SEND, block)
	return this
}

fun <T : TextView> T.clickable(b: Boolean = true): T {
	this.isClickable = b
	return this
}

fun <T : TextView> T.gravity(n: Int): T {
	this.gravity = n
	return this
}

fun <T : TextView> T.gravityCenterVertical(): T {
	this.gravity = Gravity.CENTER_VERTICAL
	return this
}

fun <T : TextView> T.gravityCenterHorizontal(): T {
	this.gravity = Gravity.CENTER_HORIZONTAL
	return this
}

fun <T : TextView> T.gravityLeftCenter(): T {
	this.gravity = Gravity.LEFT or Gravity.CENTER
	return this
}

fun <T : TextView> T.gravityRightCenter(): T {
	this.gravity = Gravity.RIGHT or Gravity.CENTER
	return this
}

fun <T : TextView> T.gravityCenter(): T {
	this.gravity = Gravity.CENTER
	return this
}

fun <T : TextView> T.gravityTopLeft(): T {
	this.gravity = Gravity.TOP or Gravity.LEFT
	return this
}

fun <T : TextView> T.miniWidthDp(widthDp: Int): T {
	this.minWidth = App.dp2px(widthDp)
	return this
}

fun <T : TextView> T.miniHeightDp(heightDp: Int): T {
	this.minHeight = App.dp2px(heightDp)
	return this
}

fun <T : TextView> T.inputTypePassword(): T {
	this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
	return this
}

fun <T : TextView> T.inputTypePasswordNumber(): T {
	this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
	return this
}

fun <T : TextView> T.inputTypePhone(): T {
	this.inputType = InputType.TYPE_CLASS_PHONE
	return this
}

fun <T : TextView> T.inputTypeEmail(): T {
	this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
	return this
}

fun <T : TextView> T.inputTypeNumber(): T {
	this.inputType = InputType.TYPE_CLASS_NUMBER
	return this
}


fun <T : TextView> T.textSizeSp(sp: Int): T {
	this.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.toFloat())
	return this
}

fun <T : TextView> T.textSize_(n: Int): T {
	return this.textSizeSp(n)
}

fun <T : TextView> T.textSizeA(): T {
	return this.textSizeSp(Dim.textSizeA)
}

fun <T : TextView> T.textSizeB(): T {
	return this.textSizeSp(Dim.textSizeB)
}

fun <T : TextView> T.textSizeC(): T {
	return this.textSizeSp(Dim.textSizeC)
}

fun <T : TextView> T.textSizeD(): T {
	return this.textSizeSp(Dim.textSizeD)
}


fun <T : TextView> T.textSizeTitle(): T {
	return textSizeSp(Dim.textSizeTitle)
}

fun <T : TextView> T.textSizeNormal(): T {
	return textSizeSp(Dim.textSize)
}

fun <T : TextView> T.lines(lines: Int): T {
	setLines(lines)
	return this
}

fun <T : TextView> T.maxLines(maxLines: Int): T {
	setMaxLines(maxLines)
	return this
}

fun <T : TextView> T.textColor(color: Int): T {
	this.setTextColor(color)
	return this
}

fun <T : TextView> T.textColor_(color: Int): T {
	this.setTextColor(color)
	return this
}


fun <T : TextView> T.textColor(color: Int, pressed: Int): T {
	this.setTextColor(ColorList(color, pressed))
	return this
}

fun <T : TextView> T.textColor(ls: ColorStateList): T {
	setTextColor(ls)
	return this
}

fun <T : TextView> T.textColorWhite(): T {
	this.setTextColor(Colors.WHITE)
	return this
}

fun <T : TextView> T.textColorMajor(): T {
	this.setTextColor(Colors.TextColorMajor)
	return this
}

fun <T : TextView> T.textColorMinor(): T {
	this.setTextColor(Colors.TextColorMinor)
	return this
}

fun <T : TextView> T.textColorMid(): T {
	this.setTextColor(Colors.TextColorMid)
	return this
}

fun <T : TextView> T.textColorSafe(): T {
	this.setTextColor(Colors.Safe)
	return this
}


fun <T : TextView> T.textColorMajorFade(): T {
	setTextColor(Img.colorList(Colors.TextColor, Colors.Fade))
	return this
}

fun <T : TextView> T.singleLine(): T {
	this.setSingleLine(true)
	return this
}

fun <T : TextView> T.ellipsizeStart(): T {
	ellipsize = TextUtils.TruncateAt.START
	return this
}

fun <T : TextView> T.ellipsizeMid(): T {
	ellipsize = TextUtils.TruncateAt.MIDDLE
	return this
}

fun <T : TextView> T.ellipsizeEnd(): T {
	ellipsize = TextUtils.TruncateAt.END
	return this
}

fun <T : TextView> T.ellipsizeMarquee(): T {
	ellipsize = TextUtils.TruncateAt.MARQUEE
	return this
}

fun <T : TextView> T.text(text: String?): T {
	setText(text)
	return this
}

fun <T : TextView> T.text_(text: String?): T {
	setText(text)
	return this
}

fun <T : TextView> T.textX(text: String?): T {
	setText(text)
	return this
}

fun <T : TextView> T.lineSpace(add: Float, multi: Float): T {
	setLineSpacing(add, multi)
	return this
}

fun <T : TextView> T.hint(text: String): T {
	this.hint = text
	return this
}

fun <T : TextView> T.linkifyAll(): T {
	this.autoLinkMask = Linkify.ALL
	this.movementMethod = LinkMovementMethod.getInstance()
	return this
}

fun <T : TextView> T.leftImage(d: Drawable, margin: Int = Space.Small): T {
	this.setCompoundDrawables(d, null, null, null)
	this.compoundDrawablePadding = dp(margin)
	return this
}

fun <T : TextView> T.rightImage(d: Drawable, margin: Int = Space.Small): T {
	this.setCompoundDrawables(null, null, d, null)
	this.compoundDrawablePadding = dp(margin)
	return this
}


fun <T : TextView> T.topImage(d: Drawable, margin: Int = Space.Small): T {
	this.setCompoundDrawables(null, d, null, null)
	this.compoundDrawablePadding = dp(margin)
	return this
}

fun <T : TextView> T.bottomImage(d: Drawable, margin: Int = Space.Small): T {
	this.setCompoundDrawables(null, null, null, d)
	this.compoundDrawablePadding = dp(margin)
	return this
}



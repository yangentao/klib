package yet.ui.res

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import android.support.annotation.DrawableRes
import net.yet.R
import yet.ext.RGB
import yet.theme.*
import yet.ui.widget.EditTextX
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-07-23.
 */

object D {
	val CheckBox: Drawable get() = checked(R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
	val EditClear: Drawable get() = D.sized(R.drawable.yet_edit_clear, EditTextX.IMAGE_WIDTH)
	val Back: Drawable get() = D.res(R.drawable.yet_back).sized(IconSize.Normal)
	val ArrowRight: Drawable get() = D.res(R.drawable.yet_arrow_right).sized(IconSize.Tiny)
	val RedPoint: Drawable
		get() = Shapes.oval {
			size(10)
			fillColor = RGB(255, 128, 0)
		}
	val Input: Drawable
		get() {
			val corner: Int = InputSize.EditCorner
			val normal = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.GRAY).value
			val focused = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.EditFocus).value
			return focused(normal, focused)
		}
	val InputSearch: Drawable
		get() {
			val corner: Int = InputSize.EditHeightSearch / 2
			val normal = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.GRAY).value
			val focused = RectDraw(Colors.WHITE).corner(corner).stroke(1, Colors.EditFocus).value
			return focused(normal, focused)
		}

	fun buttonGreen(corner: Int = InputSize.ButtonCorner): Drawable {
		return buttonColor(Colors.Safe, corner)
	}

	fun buttonRed(corner: Int = InputSize.ButtonCorner): Drawable {
		return buttonColor(Colors.RedMajor, corner)
	}

	fun buttonWhite(corner: Int = InputSize.ButtonCorner): Drawable {
		return buttonColor(Color.rgb(245, 245, 245), corner)
	}

	fun buttonColor(color: Int, corner: Int = InputSize.ButtonCorner): Drawable {
		val normal = RectDraw(color).corner(corner).value
		val pressed = RectDraw(Colors.Fade).corner(corner).value
		val enableFalse = RectDraw(Colors.Disabled).corner(corner).value
		return ImageStated(normal).pressed(pressed).enabled(enableFalse, false).value
	}

	fun panelBorder(color: Int = Colors.LightGray, corner: Int = InputSize.ButtonCorner): Drawable {
		return RectDraw(Color.WHITE).corner(corner).stroke(1, color).value
	}

	fun res(@DrawableRes resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.app.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

	fun sized(@DrawableRes resId: Int, size: Int): Drawable {
		return res(resId).sized(size, size)
	}

	fun limited(@DrawableRes resId: Int, edge: Int): Drawable {
		return res(resId).limited(edge)
	}

	fun color(color: Int): ColorDrawable {
		return ColorDrawable(color)
	}

	fun listColor(normal: Int, pressed: Int): ColorStateList {
		return ColorList(normal).pressed(pressed).selected(pressed).focused(pressed).get()
	}

	fun light(normal: Drawable, pressed: Drawable): StateListDrawable {
		return ImageStated(normal).pressed(pressed).selected(pressed).focused(pressed).value
	}

	fun light(@DrawableRes normal: Int, @DrawableRes light: Int): StateListDrawable {
		return ImageStated(normal).pressed(light).selected(light).focused(light).value
	}

	fun lightColor(normalColor: Int, pressedColor: Int): StateListDrawable {
		return ColorStated(normalColor).pressed(pressedColor).selected(pressedColor).focused(pressedColor).value
	}

	fun checked(@DrawableRes normalId: Int, @DrawableRes checkedId: Int): StateListDrawable {
		return ImageStated(D.res(normalId)).checked(D.res(checkedId)).value
	}

	fun focused(@DrawableRes normalId: Int, @DrawableRes checkedId: Int): StateListDrawable {
		return ImageStated(D.res(normalId)).focused(D.res(checkedId)).value
	}

	fun focused(normal: Drawable, focusedImage: Drawable): StateListDrawable {
		return ImageStated(normal).focused(focusedImage).value
	}
}
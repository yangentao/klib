package yet.ui.res

import android.graphics.drawable.*
import android.support.annotation.DrawableRes
import yet.theme.Colors
import yet.util.BmpUtil
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-10-31.
 */

object Images {
	fun res(@DrawableRes id: Int): Drawable {
		return Res.drawable(id)
	}

	fun tint(@DrawableRes res: Int, color: Int): Drawable {
		val bmp = BmpUtil.fromRes(res)!!
		val b2 = BmpUtil.tint(bmp, color)
		return BitmapDrawable(App.resource, b2)
	}

	fun tintStatedTheme(@DrawableRes res: Int): StateListDrawable {
		return tintStated(res, Colors.Unselected, Colors.Theme)
	}

	fun tintStated(@DrawableRes res: Int, normalColor: Int, lightColor: Int): StateListDrawable {
		val bmp = BmpUtil.fromRes(res)!!
		val b = BmpUtil.tint(bmp, normalColor)
		val d = BitmapDrawable(App.resource, b)
		val b2 = BmpUtil.tint(bmp, lightColor)
		val d2 = BitmapDrawable(App.resource, b2)
		return ImageStated(d).pressed(d2).selected(d2).value
	}

	fun stated(@DrawableRes normal: Int, @DrawableRes lighted: Int): StateListDrawable {
		return ImageStated(res(normal)).pressed(res(lighted)).selected(res(lighted)).value
	}

	fun pressed(@DrawableRes normal: Int, @DrawableRes pressed: Int): StateListDrawable {
		return ImageStated(res(normal)).pressed(res(pressed)).value
	}

	fun selected(@DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable {
		return ImageStated(res(normal)).selected(res(selected)).value
	}

	fun pressedSelected(@DrawableRes normal: Int, @DrawableRes pressed: Int, @DrawableRes selected: Int): StateListDrawable {
		return ImageStated(res(normal)).pressed(res(pressed)).selected(res(selected)).value
	}

	fun color(c: Int): ColorDrawable {
		return ColorDrawable(c)
	}

	fun colorPressed(normal: Int, pressed: Int): StateListDrawable {
		return ColorStated(normal).pressed(pressed).value
	}

	fun colorSelected(normal: Int, selected: Int): StateListDrawable {
		return ColorStated(normal).selected(selected).value
	}

	fun colorPressedSelected(normal: Int, pressed: Int, selected: Int): StateListDrawable {
		return ColorStated(normal).pressed(pressed).selected(selected).value
	}
}
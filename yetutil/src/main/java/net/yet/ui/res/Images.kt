package net.yet.ui.res

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.DrawableRes
import net.yet.ui.util.StateImage

/**
 * Created by entaoyang@163.com on 2016-10-31.
 */

object Images {
	fun res(@DrawableRes id: Int): Drawable {
		return Res.drawable(id)
	}

	fun pressed(@DrawableRes normal: Int, @DrawableRes pressed: Int): StateListDrawable {
		return StateImage(res(normal)).pressed(res(pressed)).value
	}

	fun selected(@DrawableRes normal: Int, @DrawableRes selected: Int): StateListDrawable {
		return StateImage(res(normal)).selected(res(selected)).value
	}

	fun pressedSelected(@DrawableRes normal: Int, @DrawableRes pressed: Int, @DrawableRes selected: Int): StateListDrawable {
		return StateImage(res(normal)).pressed(res(pressed)).selected(res(selected)).value
	}

	fun color(c: Int): ColorDrawable {
		return ColorDrawable(c)
	}

	fun colorPressed(normal: Int, pressed: Int): StateListDrawable {
		return StateImage(normal).pressed(pressed).value
	}

	fun colorSelected(normal: Int, selected: Int): StateListDrawable {
		return StateImage(normal).selected(selected).value
	}

	fun colorPressedSelected(normal: Int, pressed: Int, selected: Int): StateListDrawable {
		return StateImage(normal).pressed(pressed).selected(selected).value
	}
}
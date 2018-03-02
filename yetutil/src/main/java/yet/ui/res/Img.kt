package yet.ui.res

import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.os.Build
import android.support.annotation.DrawableRes
import yet.ext.size
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-07-23.
 */

object Img {


	fun colorLight(color: Int, lighted: Int): Drawable {
		return light(ColorDrawable(color), ColorDrawable(lighted))
	}

	fun color(color: Int): Drawable {
		return ColorDrawable(color)
	}

	fun colorStates(normal: Int, pressed: Int): StateListDrawable {
		return ColorStated(normal).pressed(pressed, true).selected(pressed, true).focused(pressed, true).value
	}

	fun colorList(normal: Int, pressed: Int): ColorStateList {
		return ColorList(normal).pressed(pressed, true).selected(pressed, true).focused(pressed, true).get()
	}


	fun light(normal: Drawable, pressed: Drawable): Drawable {
		return ImageStated(normal).pressed(pressed, true).selected(pressed, true).focused(pressed, true).value
	}
	fun resLight(@DrawableRes normal: Int, @DrawableRes light: Int): Drawable {
		return ImageStated(normal).pressed(light, true).selected(light, true).focused(light, true).value
	}

	fun res(@DrawableRes resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.app.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

	fun resSized(@DrawableRes resId: Int, size: Int): Drawable {
		return res(resId).size(size, size)
	}

}
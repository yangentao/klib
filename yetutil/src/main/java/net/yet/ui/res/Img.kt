package net.yet.ui.res

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import net.yet.ext.size
import net.yet.ui.util.StateColor
import net.yet.ui.util.StateImage
import net.yet.util.app.App
import net.yet.util.loge

/**
 * Created by entaoyang@163.com on 2016-07-23.
 */

object Img {

	fun bitmap(file: String): Bitmap {
		val b = AssetImage.bitmap(file)
		if (b == null) {
			loge("没有找到图片:", file)
		}
		return b!!
	}

	fun bitmap(file: String, state: State): Bitmap {
		val b = AssetImage.bitmap(file, state)
		if (b == null) {
			loge("没有找到图片:", file)
		}
		return b!!
	}

	fun named(file: String): Drawable {
		val b = AssetImage.drawable(file)
		if (b == null) {
			loge("没有找到图片:", file)
		}
		return b!!
	}

	fun namedSize(name: String, size: Int): Drawable {
		return named(name).size(size, size)
	}

//	fun namedState(file: String, state: State): Drawable {
//		return AssetImage.drawable(file, state)!!
//	}

	fun namedStates(name: String, withStates: Boolean): Drawable {
		val b = AssetImage.drawable(name, withStates)
		if (b == null) {
			loge("没有找到图片:", name)
		}
		return b!!
	}

	fun namedStatesSize(name: String, withStates: Boolean, size: Int): Drawable {
		return namedStates(name, withStates).size(size, size)
	}

	fun color(color: Int): Drawable {
		return ColorDrawable(color)
	}

	fun colorStates(normal: Int, pressed: Int): Drawable {
		return StateImage(normal).pressed(pressed, true).selected(pressed, true).focused(pressed, true).value
	}

	fun colorList(normal: Int, pressed: Int): ColorStateList {
		return StateColor(normal).pressed(pressed, true).selected(pressed, true).focused(pressed, true).get()
	}


	fun normalPressed(normal: Drawable, pressed: Drawable): Drawable {
		return StateImage(normal).pressed(pressed, true).selected(pressed, true).focused(pressed, true).value
	}

	fun res(@DrawableRes resId: Int): Drawable {
		return App.getResources().getDrawable(resId)
	}

	fun resSize(@DrawableRes resId: Int, size: Int): Drawable {
		return res(resId).size(size, size)
	}

}
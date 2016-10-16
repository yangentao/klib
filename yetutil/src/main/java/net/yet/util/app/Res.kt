package net.yet.util.app

import android.graphics.drawable.Drawable
import android.os.Build

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */
object Res {
	@JvmStatic fun str(resId: Int): String {
		return App.getResources().getString(resId)
	}

	@JvmStatic fun strArgs(resId: Int, vararg args: Any): String {
		return App.getResources().getString(resId, *args)
	}

	@JvmStatic fun image(resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.getResources().getDrawable(resId, App.get().theme)
		} else {
			App.getResources().getDrawable(resId)
		}
	}
	@JvmStatic fun drawable(resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.getResources().getDrawable(resId, App.get().theme)
		} else {
			App.getResources().getDrawable(resId)
		}
	}

	@JvmStatic fun color(resId: Int): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.getResources().getColor(resId, App.get().theme)
		} else {
			App.getResources().getColor(resId)
		}
	}
}
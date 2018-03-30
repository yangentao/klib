package yet.ui.res

import android.graphics.drawable.Drawable
import android.os.Build
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */
object Res {
	fun str(resId: Int): String {
		return App.resource.getString(resId)
	}

	fun strArgs(resId: Int, vararg args: Any): String {
		return App.resource.getString(resId, *args)
	}

	fun color(resId: Int): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			App.resource.getColor(resId, App.app.theme)
		} else {
			App.resource.getColor(resId)
		}
	}



	fun drawable(resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.app.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

}
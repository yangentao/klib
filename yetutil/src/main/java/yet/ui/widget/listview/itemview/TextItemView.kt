package yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.TextView
import yet.ext.size
import yet.theme.IconSize
import yet.theme.Space
import yet.ui.ext.*
import yet.ui.res.Img

/**
 * Created by entaoyang@163.com on 16/3/13.
 */
class TextItemView(context: Context) : TextView(context) {
	init {
		padding(Space.Normal, Space.Small, Space.Normal, Space.Small).gravityLeftCenter().textSizeA().textColorMajor()
	}

	fun icon(d: Drawable?) {
		d?.size(IconSize.Normal)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(d: Drawable?, size: Int) {
		d?.size(size)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(@DrawableRes resId: Int, size: Int) {
		val d = Img.resSized(resId, size)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(@DrawableRes resId: Int) {
		val d = Img.res(resId)
		setCompoundDrawables(d, null, null, null)
	}
}

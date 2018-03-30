package yet.ui.widget.recycler

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import yet.theme.IconSize
import yet.ui.ext.*
import yet.ui.res.sized

/**
 * Created by yet on 2015/10/28.
 */
class ImageItemAction(context: Context) : ImageView(context) {
	init {
		adjustViewBounds = true
		scaleType = ImageView.ScaleType.CENTER_INSIDE
		this.minimumHeight = dp(40)
		this.minimumWidth = dp(40)
		padding(8, 8, 8, 8)
		linearParam().width_(50).height_(40).gravityCenter().set(this)
	}

	fun icon(d: Drawable): ImageItemAction {
		setImageDrawable(d.sized(IconSize.Small))
		return this
	}
}

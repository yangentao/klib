package yet.ui.res

import android.graphics.drawable.Drawable
import yet.ui.ext.dp

/**
 * Created by entaoyang@163.com on 2018-03-07.
 */

fun Drawable.limit(maxEdge: Int): Drawable {
	val h = this.intrinsicHeight
	val w = this.intrinsicWidth
	if (w > maxEdge || h > maxEdge) {
		if (w > h) {
			this.setBounds(0, 0, dp(maxEdge), dp(h * maxEdge / w))
		} else {
			this.setBounds(0, 0, dp(w * maxEdge / h), dp(maxEdge))
		}
	}
	return this
}
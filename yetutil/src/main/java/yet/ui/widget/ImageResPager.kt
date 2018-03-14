package yet.ui.widget

import android.content.Context
import android.view.View
import android.widget.ImageView.ScaleType
import yet.ui.viewcreator.createImageView

class ImageResPager(context: Context) : BaseViewPager<Int>(context) {
	override fun newView(context: Context, position: Int, item: Int): View {
		val iv = context.createImageView()
		iv.scaleType = ScaleType.FIT_XY
		iv.setImageResource(item!!)
		return iv
	}
}

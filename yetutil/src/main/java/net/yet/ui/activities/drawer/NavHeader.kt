package net.yet.ui.activities.drawer

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import net.yet.theme.Space
import net.yet.ui.ext.*
import net.yet.util.BmpUtil

/**
 * Created by entaoyang@163.com on 16/6/27.
 */
class NavHeader(context: Context) : RelativeLayout(context) {

	var imageView: ImageView
	var nameText: TextView
	var statusText: TextView

	init {
		genId()
		imageView = context.createImageView().scaleFitXY()
		addViewParam(imageView) {
			size(80).centerVertical().parentLeft().margins(0, 0, Space.Small, 0)
		}

		nameText = context.createTextViewA().textColorWhite().padding(0, 0, 0, 12)
		addViewParam(nameText) {
			toRightOf(imageView).centerVertical().widthWrap().heightWrap()
		}

		statusText = context.createTextViewC().textColorWhite().padding(0, 12, 0, 0)
		addViewParam(statusText) {
			toRightOf(imageView).below(nameText).widthWrap().heightWrap()
		}
	}

	fun setBitmap(bmp: Bitmap) {
		val b = BmpUtil.round(bmp)
		imageView.setImageBitmap(b)
	}

	fun setName(name: String) {
		nameText.text = name
	}

	fun setStatus(status: String) {
		statusText.text = status
	}

	fun setStatusColor(color: Int) {
		statusText.setTextColor(color)
	}

	fun setNameColor(color: Int) {
		nameText.setTextColor(color)
	}


}

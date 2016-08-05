package net.yet.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.TextView

import net.yet.ui.res.Img
import net.yet.ui.util.XView

/**
 * 左右对齐listview itemview
 * addLeftXXX总是从0插入子View
 * addRightXXX总是从最后插入子View

 * @author yangentao@gmail.com
 */
class LeftRightItemView(context: Context, marginBottom: Int) : LinearLayout(context) {

	init {
		XView.view(this).orientationHorizontal().gravityCenterVertical().backColorWhiteFade().padding(10, 5, 10, 5)
		XView.linearParam().widthFill().height(ITEM_HEIGHT).margins(0, 0, 0, marginBottom).set(this)

		val v = XView.id(View(getContext()))
		addView(v, XView.linearParam().weight(1f).heightFill().get())
	}

	fun findCheckBox(): CheckBox? {
		for (i in 0..childCount - 1) {
			val v = getChildAt(i)
			if (v is CheckBox) {
				return v
			}
		}
		return null
	}

	private fun addText(text: String, width: Int, right: Boolean, marginLeft: Int): TextView {
		if (right) {
			val tv = XView.createTextViewB(context)
			XView.view(tv).textColorMinor().gravityRightCenter()
			tv.text = text
			XView.linearParam().heightWrap().width(width).gravityRightCenter().margins(marginLeft, 0, 0, 0).set(tv)
			this.addView(tv)
			return tv
		} else {
			val tv = XView.createTextViewA(context)
			tv.text = text
			XView.linearParam().heightWrap().width(width).gravityLeftCenter().margins(marginLeft, 0, 0, 0).set(tv)
			this.addView(tv, 0)
			return tv
		}
	}

	fun addTextLeft(text: String, width: Int, marginLeft: Int): TextView {
		return addText(text, width, false, marginLeft)
	}

	fun addTextRight(text: String, width: Int, marginLeft: Int): TextView {
		return addText(text, width, true, marginLeft)
	}

	private fun addImage(d: Drawable, sizeDp: Int, right: Boolean, marginLeft: Int): ImageView {
		val iv = XView.createImageView(context)
		iv.scaleType = ScaleType.CENTER_INSIDE
		iv.setImageDrawable(d)
		XView.view(iv).backColorTransFade()
		if (right) {
			XView.linearParam().size(sizeDp).gravityRightCenter().margins(marginLeft, 0, 0, 0).set(iv)
			this.addView(iv)
		} else {
			XView.linearParam().size(sizeDp).gravityLeftCenter().margins(marginLeft, 0, 0, 0).set(iv)
			this.addView(iv, 0)
		}
		return iv
	}

	fun addImageLeft(d: Drawable, sizeDp: Int, marginLeft: Int): ImageView {
		return addImage(d, sizeDp, false, marginLeft)
	}

	fun addImageRight(d: Drawable, sizeDp: Int, marginLeft: Int): ImageView {
		return addImage(d, sizeDp, true, marginLeft)
	}

	fun addCheckBoxRight(marginLeft: Int): CheckBox {
		val cb = XView.createCheckbox(context)
		val d = Img.namedStatesSize("checkbox", true, 15)
		cb.buttonDrawable = d
		XView.linearParam().size(20).gravityRightCenter().margins(marginLeft, 0, 0, 0).set(cb)
		this.addView(cb)
		return cb
	}

	companion object {
		val ITEM_HEIGHT = 45
	}

}

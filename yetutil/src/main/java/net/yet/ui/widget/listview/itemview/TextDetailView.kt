package net.yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import net.yet.R
import net.yet.ext.size
import net.yet.ui.ext.*
import net.yet.ui.res.Img
import net.yet.ui.res.Res

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


open class TextDetailView(context: Context) : HorItemView(context) {
	var textView: TextView
	var detailView: TextView

	init {
		padding(20, 10, 20, 10).gravityCenterVertical()

		textView = createTextView().textSizeB().textColorMajor().singleLine()
		addViewParam(textView) { widthDp(0).weight(1f).heightWrap().gravityLeftCenter() }

		detailView = createTextView().textSizeC().textColorMid().singleLine()
		addViewParam(detailView) { wrap().gravityRightCenter() }
	}

	fun setText(text: String?) {
		textView.text = text
	}

	fun setDetail(detail: String?) {
		detailView.text = detail
	}

	fun setValues(text: String?, detail: String?): TextDetailView {
		textView.text = text
		detailView.text = detail
		return this
	}

	fun setTextSize(sp1: Int, sp2: Int): TextDetailView {
		textView.textSizeSp(sp1)
		detailView.textSizeSp(sp2)
		return this
	}

	fun setLeftImage(d: Drawable) {
		textView.setCompoundDrawables(d, null, null, null)
		textView.compoundDrawablePadding = dp(10)
	}

	fun setLeftImage(name: String, size: Int = 25) {
		setLeftImage(Img.namedStatesSize(name, false, size))
	}

	fun setRightImage(name: String, size: Int = 25) {
		setRightImage(Img.namedStatesSize(name, false, size))
	}

	fun setRightImage(d: Drawable) {
		detailView.setCompoundDrawables(null, null, d, null)
		detailView.compoundDrawablePadding = dp(10)
	}

	fun rightImageMore() {
		val d = Res.drawable(R.drawable.yet_back).size(25)
		setRightImage(d)
	}
}
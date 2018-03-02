package yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import net.yet.R
import yet.ext.size
import yet.ui.ext.*
import yet.ui.res.Img

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


open class TextDetailView(context: Context) : HorItemView(context) {
	var textView: TextView
	var detailView: TextView

	var argS: String = ""
	var argN: Int = 0

	init {
		padding(20, 10, 20, 10).gravityCenterVertical()

		textView = addTextView(lParam().widthWrap().heightWrap().gravityLeftCenter()) {
			textSizeB().textColorMajor().singleLine()
		}
		addFlex()
		detailView = addTextView(lParam().wrap().gravityRightCenter()) {
			textSizeC().textColorMid().gravityRightCenter().multiLine()
			maxLines(2)
		}
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


	fun setRightImage(d: Drawable) {
		detailView.setCompoundDrawables(null, null, d, null)
		detailView.compoundDrawablePadding = dp(10)
	}

	fun rightImageMore() {
		val d = Img.res(R.drawable.yet_back).size(25)
		setRightImage(d)
	}
}

inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTextDetailView(param: P, block: TextDetailView.() -> Unit): TextDetailView {
	val v = TextDetailView(this.context)
	this.addView(v, param)
	v.block()
	return v
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTextDetailViewTrans(param: P, block: TextDetailView.() -> Unit): TextDetailView {
	val v = TextDetailView(this.context)
	this.addView(v, param)
	v.backColorTrans()
	v.textView.textColorWhite()
	v.detailView.textColorWhite()
	v.block()
	return v
}
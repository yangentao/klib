package net.yet.ui.page

import android.content.Context
import android.widget.LinearLayout
import android.widget.ScrollView
import net.yet.ui.ext.*

open class ScrollTitledFragment : TitledPage() {
	private var _scrollView: ScrollView? = null
	private var _scrollContentView: LinearLayout? = null

	val scrollContentView: LinearLayout get() = _scrollContentView!!
	val scrollView: ScrollView get() = _scrollView!!


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		_scrollView = ScrollView(activity).genId()
		contentView.addView(_scrollView, linearParam().widthFill().height(0).weight(1f))
		_scrollContentView = context.createLinearVertical()
		scrollView.addView(scrollContentView, layoutParam().widthFill().heightWrap())
		onCreateScrollContent(activity, scrollContentView)
	}

	open fun onCreateScrollContent(context: Context, scrollContentView: LinearLayout) {

	}

}

package net.yet.ui.page

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import net.yet.ui.ext.*

open class ScrollTitledFragment : TitledPage() {
	lateinit  var scrollView: ScrollView
	lateinit  var scrollContentView: LinearLayout


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		scrollView = ScrollView(activity).genId()
		contentView.addView(scrollView, linearParam().widthFill().height(0).weight(1f))
		scrollContentView = context.createLinearVertical()
		scrollView.addView(scrollContentView, layoutParam().widthFill().heightWrap())
		onCreateScrollContent(activity, scrollContentView)
	}

	open fun onCreateScrollContent(context: Context, scrollContentView: LinearLayout) {

	}
	fun addScrollItemView(view: View, params: LinearLayout.LayoutParams) {
		scrollContentView.addView(view, params)
	}

	fun addScrollItemView(view: View, index: Int, params: LinearLayout.LayoutParams) {
		scrollContentView.addView(view, index, params)
	}

}

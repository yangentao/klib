package net.yet.ui.page

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import net.yet.ui.ext.*

/**
 * Created by entaoyang@163.com on 2016-09-04.
 */

open class ScrollPage : TitledPage() {
	lateinit var scrollView: ScrollView
	lateinit var scrollContentView: LinearLayout

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		scrollView = ScrollView(context)
		contentView.addViewParam(scrollView) {
			widthFill().height(0).weight(1)
		}
		scrollContentView = createLinearVertical()
		scrollView.addView(scrollContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

	}
}
package yet.ui.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import yet.theme.Colors
import yet.ui.MyColor
import yet.ui.activities.TabBarActivity
import yet.ui.ext.*
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createScroll
import yet.ui.widget.TitleBarX
import yet.util.app.OS

open class TitlePageX : BaseFragment() {
	lateinit var rootView: LinearLayout
		private set
	lateinit var contentView: LinearLayout
		private set
	lateinit var titleBar: TitleBarX
		private set

	var enableContentScroll = false

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		rootView = createLinearVertical()
		titleBar = TitleBarX(activity)
		rootView.addView(titleBar, LParam.WidthFill.height(TitleBarX.HEIGHT))

		contentView = createLinearVertical()
		if (enableContentScroll) {
			val scrollView = createScroll()
			rootView.addView(scrollView, LParam.WidthFill.height(0).weight(1))
			scrollView.addView(contentView, LParam.WidthFill.HeightWrap)
		} else {
			rootView.addView(contentView, LParam.WidthFill.height(0).weight(1))
		}


		if (this.activity !is TabBarActivity) {
			titleBar.showBack().onClick = {
				finish()
			}
		}

		onCreateContent(this.activity, contentView)
		titleBar.commit()
		if (OS.GE50) {
			val c = MyColor(Colors.Theme)
			statusBarColor(c.multiRGB(0.7))
		}
		onContentCreated()
		return rootView
	}


	fun titleBar(block: TitleBarX.() -> Unit) {
		titleBar.block()
	}


	open fun onContentCreated() {

	}

	open fun onCreateContent(context: Context, contentView: LinearLayout) {

	}


}

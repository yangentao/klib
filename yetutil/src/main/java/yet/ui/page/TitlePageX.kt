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
import yet.ui.ext.HeightWrap
import yet.ui.ext.LParam
import yet.ui.ext.WidthFill
import yet.ui.ext.height
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createScroll
import yet.ui.widget.TitleBarX
import yet.util.app.OS

open class TitlePageX : BaseFragment() {
	lateinit var contentView: LinearLayout
		private set
	lateinit var titleBar: TitleBarX
		private set

	var autoStatusBarColor: Boolean = true
	var enableContentScroll = false

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		contentView = createLinearVertical()
		titleBar = TitleBarX(activity)
		contentView.addView(titleBar, LParam.WidthFill.height(TitleBarX.HEIGHT))

		val rootView: View = if (enableContentScroll) {
			val scrollView = createScroll()
			scrollView.addView(contentView, LParam.WidthFill.HeightWrap)
			scrollView
		} else {
			contentView
		}

		if (this.activity !is TabBarActivity) {
			titleBar.showBack().onClick = {
				finish()
			}
		}

		onCreateContent(this.activity, contentView)
		titleBar.commit()
		if (autoStatusBarColor) {
			if (OS.GE50) {
				val c = MyColor(Colors.Theme)
				statusBarColor(c.multiRGB(0.7))
			}
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

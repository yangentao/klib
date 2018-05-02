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
import yet.ui.widget.BottomBar
import yet.ui.widget.TitleBar
import yet.ui.widget.TopProgressBar
import yet.util.Progress
import yet.util.app.OS

open class TitlePage : BaseFragment(), Progress {
	lateinit var rootView: LinearLayout
		private set

	lateinit var titleBar: TitleBar
		private set

	lateinit var contentView: LinearLayout
		private set

	var hasBottomBar = false
	var bottomBar: BottomBar? = null
		private set

	var hasTopProgress = false
	var topProgress: TopProgressBar? = null

	var hasSnak = false
	var snack: Snack? = null

	var contentCreated = false
		private set

	var enableContentScroll = false


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		rootView = createLinearVertical()
		if (hasTopProgress) {
			val b = TopProgressBar(activity).gone()
			rootView.addView(b, LParam.WidthFill.height(6))
			topProgress = b
		}
		titleBar = TitleBar(activity)
		rootView.addView(titleBar, LParam.WidthFill.height(TitleBar.HEIGHT))


		if (hasSnak) {
			val v = Snack(activity).gone()
			rootView.addView(v, LParam.WidthFill.HeightWrap.GravityCenterVertical)
			snack = v
		}

		contentView = createLinearVertical()
		if (enableContentScroll) {
			val scrollView = createScroll()
			rootView.addView(scrollView, LParam.WidthFill.height(0).weight(1))
			scrollView.addView(contentView, LParam.WidthFill.HeightWrap)
		} else {
			rootView.addView(contentView, LParam.WidthFill.height(0).weight(1))
		}
		if (hasBottomBar) {
			val bar = BottomBar(activity)
			rootView.addView(bar, LParam.WidthFill.height(BottomBar.HEIGHT))
			bottomBar = bar
		}

		if (this.activity !is TabBarActivity) {
			titleBar.showBack().onClick = {
				finish()
			}
		}

		onCreateContent(this.activity, contentView)
		titleBar.commit()
		bottomBar?.commit()
		if (OS.GE50) {
			val c = MyColor(Colors.Theme)
			statusBarColor(c.multiRGB(0.7))
		}
		onContentCreated()
		contentCreated = true
		return rootView
	}


	fun titleBar(block: TitleBar.() -> Unit) {
		titleBar.block()
	}


	open fun onContentCreated() {

	}

	open fun onCreateContent(context: Context, contentView: LinearLayout) {

	}


	override fun onProgressStart(total: Int) {
		topProgress?.show(100)
	}

	override fun onProgress(current: Int, total: Int, percent: Int) {
		topProgress?.setProgress(percent)
	}

	override fun onProgressFinish() {
		topProgress?.hide()
	}

}

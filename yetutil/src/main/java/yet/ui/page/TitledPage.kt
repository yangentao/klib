package yet.ui.page

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import yet.theme.Colors
import yet.ui.MyColor
import yet.ui.ext.*
import yet.ui.widget.*
import yet.util.Progress
import yet.util.app.OS
import yet.util.fore

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


abstract class TitledPage : BaseFragment() {
	protected lateinit var relativeLayout: RelativeLayout
	lateinit var contentView: LinearLayout
		private set
	lateinit var titleBar: TitleBar
		private set
	lateinit var bottomBar: BottomBar
		protected set
	lateinit var topProgressBar: MyProgressBar
		protected set
	lateinit var rootView: LinearLayout
		private set

	var searchEdit:EditText? = null

	var autoStatusBarColor: Boolean = true

	var pageReady = false

	var enableContentScroll = false
	var withSearchEdit = false

	protected lateinit var snack: Snack


	fun withSearchEdit() {
		withSearchEdit = true
	}

	fun enableContentScroll() {
		enableContentScroll = true
	}

	open fun preCreatePage() {

	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		preCreatePage()
		rootView = activity.createLinearVertical().backColorWhite()
		linearParam().fill().set(rootView)

		titleBar = TitleBar(activity)
		titleBar.onAction = {
			b, a ->
			this@TitledPage.onTitleBarAction(b, a)
		}
		titleBar.onActionNav = {
			b, a ->
			this@TitledPage.onTitleBarActionNav(b, a)
		}
		titleBar.onTitleClick = {
			onTitleBarTitleClick(titleBar.title ?: "")
		}

		rootView.addView(titleBar, lParam().widthFill().height(TitleBar.HEIGHT))


		snack = Snack(activity).gone()
		rootView.addView(snack, linearParam().widthFill().heightWrap().gravityCenterVertical())

		topProgressBar = MyProgressBar(activity).gone()
		topProgressBar.setBackDrawable(MyProgressBar.makeDrawable(0, Color.WHITE))
		topProgressBar.setForeDrawable(MyProgressBar.makeDrawable(0, Colors.Progress))
		rootView.addViewParam(topProgressBar) { widthFill().heightDp(6) }

		contentView = linearVer()
		relativeLayout = relativeLayout()
		if (enableContentScroll) {
			val scrollView = ScrollView(activity).genId()
			relativeLayout.addView(scrollView, rParam().widthFill().heightFill())
			scrollView.addView(contentView, layoutParam().widthFill().heightWrap())
		} else {
			relativeLayout.addView(contentView, rParam().widthFill().heightFill())
		}
		rootView.addViewParam(relativeLayout) { widthFill().heightDp(0).weight(1f) }

		bottomBar = BottomBar(this.activity)
		rootView.addView(bottomBar, lParam().widthFill().height(50))
		bottomBar.hide()
		bottomBar.onAction = {
			bar, a ->
			onBottomBarAction(bar, a)
		}

		if (withSearchEdit) {
			searchEdit = contentView.addEditTextX(lParam().widthFill().heightEditSearch().margins(15, 5, 15, 5)) {
				styleSearch()
				singleLine()
				imeDone {
					it.hideInputMethod()
				}
				onTextChanged {
					onSearchTextChanged(it.trim())
				}
			}
		}

		pageReady = true
		onCreateContent(this.activity, contentView)
		if (autoStatusBarColor) {
			if (OS.GE50) {
				val c = MyColor(titleBar.themeBackColor)
				statusBarColor(c.multiRGB(0.7))
			}
		}
		titleBar.commit()
		onContentCreated()
		return rootView
	}

	override fun onResume() {
		super.onResume()
		searchEdit?.clearFocus()
	}

	open fun onSearchTextChanged(s: String) {

	}

	open fun onContentCreated() {

	}

	open fun onCreateContent(context: Context, contentView: LinearLayout) {

	}

	open fun onBottomBarAction(bar: BottomBar, action: Action) {

	}

	open fun onTitleBarAction(bar: TitleBar, action: Action) {

	}

	open fun onTitleBarActionNav(bar: TitleBar, action: Action) {
		if (bar.isBack(action)) {
			activity.onBackPressed()
		}
	}

	open fun onTitleBarTitleClick(title: String) {

	}

	override fun onBackPressed(): Boolean {
		return super.onBackPressed()
	}

	fun showProgressBar(max: Int) {
		fore {
			topProgressBar.show(max)
		}
	}

	fun setProgress(n: Int) {
		fore {
			topProgressBar.setProgress(n)
		}
	}

	fun hideProgressBar() {
		fore {
			topProgressBar.hide()
		}
	}

	val defaultProgress = object : Progress {
		override fun onStart(total: Int) {
			showProgressBar(total)
		}

		override fun onProgress(current: Int, total: Int, percent: Int) {
			setProgress(current)
		}

		override fun onFinish() {
			hideProgressBar()
		}

	}
}

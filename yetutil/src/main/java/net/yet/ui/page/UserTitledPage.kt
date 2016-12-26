package net.yet.ui.page

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import net.yet.theme.Colors
import net.yet.ui.ext.*
import net.yet.ui.widget.Action
import net.yet.ui.widget.BottomBar
import net.yet.ui.widget.MyProgressBar
import net.yet.ui.widget.TitleBar
import net.yet.util.fore

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


abstract class UserTitledPage : BaseFragment() {
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


	protected lateinit var snack: Snack

	lateinit  var titleRelativeView: RelativeLayout
	lateinit  var titleLinearView: LinearLayout

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		rootView = linearVer().apply {
			backColorWhite()
		}
		linearParam().fill().set(rootView)

		titleRelativeView = relativeLayout()
		rootView.addViewParam(titleRelativeView) {
			widthFill().heightWrap()
		}

		titleLinearView = linearLayout()
		titleRelativeView.addViewParam(titleLinearView) {
			widthFill().heightWrap()
		}


		titleBar = TitleBar(activity)
		titleBar.onAction = {
			b, a ->
			this@UserTitledPage.onTitleBarAction(b, a)
		}
		titleBar.onActionNav = {
			b, a ->
			this@UserTitledPage.onTitleBarActionNav(b, a)
		}
		titleBar.onTitleClick = {
			onTitleBarTitleClick(titleBar.title ?: "")
		}

		titleLinearView.addView(titleBar)

		snack = Snack(activity).gone()
		titleLinearView.addView(snack)

		topProgressBar = MyProgressBar(activity).gone()
		topProgressBar.setBackDrawable(MyProgressBar.makeDrawable(0, Color.WHITE))
		topProgressBar.setForeDrawable(MyProgressBar.makeDrawable(0, Colors.Progress))
		titleLinearView.addViewParam(topProgressBar) { widthFill().heightDp(6) }

		contentView = linearVer()
		relativeLayout = relativeLayout()
		relativeLayout.addViewParam(contentView) { this.fill() }
		rootView.addViewParam(relativeLayout) { widthFill().heightDp(0).weight(1f) }

		bottomBar = BottomBar(this.activity)
		rootView.addView(bottomBar)
		bottomBar.hide()
		bottomBar.onAction = {
			bar, a ->
			onBottomBarAction(bar, a)
		}
		onCreateContent(this.activity, contentView)

		titleBar.commit()
		onContentCreated()
		return rootView
	}

	open fun onContentCreated() {

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

	fun showProgressBar(): MyProgressBar {
		topProgressBar.show()
		return topProgressBar
	}

	fun hideProgressBar() {
		fore {
			topProgressBar.hide()
		}
	}

	abstract fun onCreateContent(context: Context, contentView: LinearLayout)

	open fun onBottomBarAction(bar: BottomBar, action: Action) {

	}

	open fun onTitleBarAction(bar: TitleBar, action: Action) {

	}

	open fun onTitleBarActionNav(bar: TitleBar, action: Action) {
		if (bar.isEditShowing()) {
			bar.hideInputEdit()
			return
		}
		if (bar.isBack(action)) {
			activity.onBackPressed()
		}
	}

	open fun onTitleBarTitleClick(title: String) {

	}

	override fun onBackPressed(): Boolean {
		return super.onBackPressed()
	}
}

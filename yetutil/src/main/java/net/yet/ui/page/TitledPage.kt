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


open class TitledPage : BaseFragment() {
	protected var _rootView: LinearLayout? = null
		private set
	protected var relativeLayout: RelativeLayout? = null
	var _contentView: LinearLayout? = null
		private set
	var _titleBar: TitleBar? = null
		protected set
	var _bottomBar: BottomBar? = null
		protected set
	var _topProgressBar: MyProgressBar? = null
		protected set

	val contentView: LinearLayout get() = _contentView!!
	val titleBar: TitleBar get() = _titleBar!!
	val rootView: LinearLayout get() = _rootView!!
	val bottomBar: BottomBar get() = _bottomBar!!
	val topProgressBar: MyProgressBar get() = _topProgressBar!!

	protected var snack: Snack? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		_rootView = linearVer().apply {
			backColorWhite()
		}
		linearParam().fill().set(rootView)

		_titleBar = TitleBar(activity)
		titleBar.onAction = {
			b, a ->
			this@TitledPage.onTitleBarAction(b, a)
		}
		titleBar.onActionNav = {
			b, a ->
			this@TitledPage.onTitleBarActionNav(b, a)
		}

		rootView.addView(titleBar)

		snack = Snack(activity).gone()
		rootView.addView(snack)

		_topProgressBar = MyProgressBar(activity).gone()
		topProgressBar.setBackDrawable(MyProgressBar.makeDrawable(0, Color.WHITE))
		topProgressBar.setForeDrawable(MyProgressBar.makeDrawable(0, Colors.Progress))
		rootView.addViewParam(topProgressBar) { widthFill().heightDp(6) };

		_contentView = linearVer()
		relativeLayout = relativeLayout()
		relativeLayout?.addViewParam(contentView) { this.fill() }
		rootView.addViewParam(relativeLayout!!) { widthFill().heightDp(0).weight(1f) }

		_bottomBar = BottomBar(this.activity)
		rootView.addView(bottomBar)
		bottomBar.hide()
		bottomBar.onAction = {
			bar, a ->
			onBottomBarAction(bar, a)
		}
		onCreateContent(this.activity, contentView)
		return rootView
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		titleBar.commit()
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

	protected open fun onCreateContent(context: Context, contentView: LinearLayout) {

	}

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

	override fun onBackPressed(): Boolean {
		if (titleBar.isEditShowing()) {
			titleBar.hideInputEdit()
			return true
		}
		return super.onBackPressed()
	}
}

package yet.ui.activities

import android.app.FragmentTransaction
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import yet.ui.ext.*
import yet.ui.page.BaseFragment
import yet.ui.util.FragmentHelper
import yet.ui.viewcreator.createFrame
import yet.ui.viewcreator.createLinearVertical
import yet.ui.widget.*
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

open class TabBarActivity : BaseActivity() {
	lateinit var rootView: LinearLayout
		private set
	lateinit var containerView: FrameLayout
		private set
	private var fragLayoutId = 0
	lateinit var tabBar: TabBar
		private set
	private val pages = HashMap<String, BaseFragment>()

	lateinit var fragmentHelper: FragmentHelper

	fun selectTab(tag: String) {
		tabBar.select(tag)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()
		rootView = this.createLinearVertical()
		this.setContentView(rootView)

		containerView = this.createFrame()
		rootView.addViewParam(containerView) {
			widthFill().heightDp(0).weight(1f)
		}
		fragLayoutId = containerView.id

		fragmentHelper = FragmentHelper(fragmentManager, fragLayoutId)

		tabBar = TabBar(this)
		tabBar.onUnselect = {
			b, a ->
			this@TabBarActivity.onXTabBarUnselect(b, a)
		}
		tabBar.onSelect = {
			b, a ->
			this@TabBarActivity.onXTabBarSelect(b, a)
		}
		tabBar.onReselect = {
			b, a ->
			this@TabBarActivity.onXTabBarReselect(b, a)
		}
		rootView.addView(tabBar)
	}

	fun addTab(action: Action, page: BaseFragment): Action {
		tabBar.add(action)
		pages.put(action.tag, page)
		return action
	}

	fun onXTabBarUnselect(bar: TabBar, action: Action) {
	}

	fun onXTabBarReselect(bar: TabBar, action: Action) {
	}

	fun onXTabBarSelect(bar: TabBar, action: Action) {
		val page = pages[action.tag]
		fragmentHelper.showFragment(page!!, action.tag)
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null && currentFragment.onKeyDown(keyCode, event)) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}

	override fun finish() {
		val currentFragment = fragmentHelper.currentBaseFragment
		super.finish()
		if (currentFragment != null) {
			val ac = currentFragment.activityAnim
			if (ac != null) {
				this.overridePendingTransition(ac.finishEnter, ac.finishExit)
			}
		}
	}

	override fun onBackPressed() {
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null) {
			if (currentFragment.onBackPressed()) {
				return
			}
		}
		super.onBackPressed()
	}
}

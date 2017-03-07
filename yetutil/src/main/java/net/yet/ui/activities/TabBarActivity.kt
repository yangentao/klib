package net.yet.ui.activities

import android.app.FragmentTransaction
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import net.yet.ui.ext.*
import net.yet.ui.page.BaseFragment
import net.yet.ui.util.FragmentHelper
import net.yet.ui.widget.Action
import net.yet.ui.widget.TabBar
import net.yet.ui.widget.add
import net.yet.util.log.xlog
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

open class TabBarActivity : BaseActivity() {
	var rootView: LinearLayout? = null
		private set
	var containerView: FrameLayout? = null
		private set
	private var fragLayoutId = 0
	private var _tabBar: TabBar? = null
		private set
	val tabBar: TabBar get() = _tabBar!!
	private val pages = HashMap<String, BaseFragment>()

	private var fragmentHelper: FragmentHelper? = null

	fun selectTab(tag: String) {
		tabBar.select(tag)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()
		rootView = this.createLinearVertical()
		this.setContentView(rootView)

		containerView = this.createFrameLayout()
		rootView!!.addViewParam(containerView!!) {
			widthFill().heightDp(0).weight(1f)
		}
		fragLayoutId = containerView!!.id

		fragmentHelper = FragmentHelper(getFragmentManager(), fragLayoutId)

		_tabBar = TabBar(this)
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
		rootView!!.addView(tabBar)
	}

	fun addTab(action: Action, page: BaseFragment): Action {
		tabBar.add(action)
		pages.put(action.tag, page)
		return action
	}

	fun onXTabBarUnselect(bar: TabBar, action: Action) {
		xlog.d("unselect ", action.tag)
	}

	fun onXTabBarReselect(bar: TabBar, action: Action) {
		xlog.d("reselect ", action.tag)
	}

	fun onXTabBarSelect(bar: TabBar, action: Action) {
		xlog.d("select ", action.tag)
		val page = pages[action.tag]
		fragmentHelper!!.showFragment(page!!, action.tag)
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		val currentFragment = fragmentHelper!!.currentBaseFragment
		if (currentFragment != null && currentFragment.onKeyDown(keyCode, event)) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}

	override fun finish() {
		val currentFragment = fragmentHelper!!.currentBaseFragment
		super.finish()
		if (currentFragment != null) {
			val ac = currentFragment.activityAnim
			if (ac != null) {
				this.overridePendingTransition(ac.finishEnter, ac.finishExit)
			}
		}
	}

	override fun onBackPressed() {
		val currentFragment = fragmentHelper!!.currentBaseFragment
		if (currentFragment != null) {
			if (currentFragment.onBackPressed()) {
				return
			}
		}
		super.onBackPressed()
	}
}

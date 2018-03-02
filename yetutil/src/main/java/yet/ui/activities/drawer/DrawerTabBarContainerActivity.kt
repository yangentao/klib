package yet.ui.activities.drawer

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import yet.ui.activities.BaseActivity
import yet.ui.ext.*
import yet.ui.page.BaseFragment
import yet.ui.util.FragmentHelper
import yet.ui.widget.Action
import yet.ui.widget.TabBar
import yet.ui.widget.add
import yet.util.log.xlog
import java.util.*

class DrawerTabBarContainerActivity : BaseActivity() {
	lateinit var drawerLayout: DrawerLayout
	lateinit var rootView: LinearLayout
		private set
	lateinit var containerView: FrameLayout
		private set
	private var fragLayoutId = 0
	lateinit var tabBar: TabBar
		private set
	private val pages = HashMap<String, BaseFragment>()

	lateinit var fragmentHelper: FragmentHelper
	lateinit var navView: DrawerNavView

	fun selectTab(tag: String) {
		tabBar.select(tag)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()

		drawerLayout = DrawerLayout(this).genId()

		rootView = createLinearVertical()
		val lp = DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
		drawerLayout.addView(rootView, lp)

		navView = DrawerNavView(this)
		navView.onActionCallback = {
			closeDrawer()
		}
		val lp2 = DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
		lp2.gravity = Gravity.START
		drawerLayout.addView(navView, lp2)

		this.setContentView(drawerLayout)

		val toggle = ActionBarDrawerToggle(
				this, drawerLayout, net.yet.R.string.yet_navigation_drawer_open, net.yet.R.string.yet_navigation_drawer_close)
		drawerLayout.setDrawerListener(toggle)
		toggle.syncState()
		containerView = createFrameLayout()
		rootView.addViewParam(containerView) {
			widthFill().height(0).weight(1f)
		}
		fragLayoutId = containerView.id

		fragmentHelper = FragmentHelper(fragmentManager, fragLayoutId)

		tabBar = TabBar(this)
		tabBar.onSelect = { bar, action ->
			xlog.d("select ", action.tag)
			val page = pages[action.tag]
			fragmentHelper.showFragment(page!!, action.tag)
		}
		rootView.addView(tabBar)
	}

	var navHeaderView: View
		get() = navView.header
		set(view) {
			navView.header = view
		}

	private val actions = ArrayList<Action>()

	fun addDrawerAction(a: Action) {
		actions.add(a)
	}

	fun addDrawerAction(titleAndTag: String): Action {
		val a = Action(titleAndTag)
		addDrawerAction(a)
		return a
	}


	fun commitDrawerActions() {
		navView.setActions(actions)
		actions.clear()
	}

	val currentPage: BaseFragment?
		get() {
			val a = tabBar.selectedAction
			if (a != null) {
				return pages[a.tag]
			}
			return null
		}

	fun addTab(action: Action, page: BaseFragment): Action {
		tabBar.add(action)
		pages.put(action.tag, page)
		return action
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

	fun openDrawer() {
		drawerLayout.openDrawer(GravityCompat.START)
	}

	fun closeDrawer() {
		drawerLayout.closeDrawer(GravityCompat.START)
	}

	val isDrawerOpen: Boolean
		get() = drawerLayout.isDrawerOpen(GravityCompat.START)

	override fun onBackPressed() {
		if (isDrawerOpen) {
			closeDrawer()
			return
		}
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null) {
			if (currentFragment.onBackPressed()) {
				return
			}
		}
		super.onBackPressed()
	}


}

package net.yet.ui.activities

import android.app.FragmentTransaction
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import net.yet.ui.ext.*
import net.yet.ui.page.BaseFragment

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


open class PageActivity : BaseActivity() {
	lateinit  var rootView: LinearLayout
	lateinit var fragmentContainerView: FrameLayout
	var currentFragment: BaseFragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		currentFragment = OpenActivity.pop(intent)
		if (currentFragment == null) {
			currentFragment = getInitPage()
		}
		fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()
		rootView = this.createLinearVertical()
		this.setContentView(rootView)

		fragmentContainerView = this.createFrameLayout()
		rootView.addViewParam(fragmentContainerView) {
			widthFill().heightDp(0).weight(1f)
		}

		if (currentFragment != null) {
			replaceFragment(currentFragment!!)
		}
	}

	open fun getInitPage(): BaseFragment? {
		return null
	}

	override fun finish() {
		super.finish()
		val ac = currentFragment?.activityAnim

		if (ac != null) {
			this.overridePendingTransition(ac.finishEnter, ac.finishExit)
		}

	}

	override fun onBackPressed() {
		if (currentFragment?.onBackPressed() ?: false) {
			return
		}
		super.onBackPressed()
	}

	val fragmentContainerId: Int
		get() = fragmentContainerView!!.id

	fun replaceFragment(fragment: BaseFragment) {
		this.currentFragment = fragment
		fragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commitAllowingStateLoss()
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (currentFragment?.onKeyDown(keyCode, event) ?: false) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}


}

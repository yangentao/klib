package net.yet.yetlibdemo

import yet.ui.activities.PageActivity
import yet.ui.page.BaseFragment

class MainActivity : PageActivity() {


	override fun getInitPage(): BaseFragment? {
		return MainPage()
	}


}

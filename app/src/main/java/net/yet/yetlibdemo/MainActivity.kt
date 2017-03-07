package net.yet.yetlibdemo

import net.yet.ui.activities.PageActivity
import net.yet.ui.page.BaseFragment

class MainActivity : PageActivity() {


	override fun getInitPage(): BaseFragment? {
		return MainPage()
	}


}

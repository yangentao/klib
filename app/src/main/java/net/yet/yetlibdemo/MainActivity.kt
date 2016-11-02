package net.yet.yetlibdemo

import net.yet.ui.activities.ContainerActivity
import net.yet.ui.page.BaseFragment

class MainActivity : ContainerActivity() {


	override fun getInitPage(): BaseFragment? {
		return MainPage()
	}


}

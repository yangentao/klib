package net.yet.yetlibdemo

import android.widget.LinearLayout
import net.yet.sys.contact.ContactSelectPage
import net.yet.ui.activities.PageUtil
import net.yet.ui.activities.TitledActivity
import net.yet.ui.widget.Action

class MainActivity : TitledActivity() {
	val TEST = "Text"
	override fun onCreateContent(contentView: LinearLayout) {
		super.onCreateContent(contentView)
		titleBar.title = "主页"
		titleBar.addAction(TEST)
		titleBar.showBack()
	}

	override fun onTitleBarAction(action: Action) {
		if (action.isTag(TEST)) {
			PageUtil.open(this, ContactSelectPage())
		}
	}

}

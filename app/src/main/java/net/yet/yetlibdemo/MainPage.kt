package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import net.yet.ui.page.TitledPage
import net.yet.ui.page.select.StringSelectPage
import net.yet.util.log.log

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */

class MainPage : TitledPage() {


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title = "Test"

		titleBar.addAction("test").onAction {
			test()
		}


	}

	fun test() {
		val p = StringSelectPage()
		p.multiMode()
		p.addItems("Yang", "En", "Tao", "DouDou")
		p.onMultiSelect = {
			log(it, p.selectItems)
		}
		openPage(p)
	}


}
package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import net.yet.ui.dialogs.list.StringSelectDialog
import net.yet.ui.page.TitledPage
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
		val dlg = StringSelectDialog()
		dlg.onSelectValue = {
			log(it)
		}
		dlg.onMultiSelectValues = {
			log(it)
		}
		dlg.multiSelect = true
		dlg.addItems("A", "B", "C")
		dlg.show(activity, "Hello")
	}


}
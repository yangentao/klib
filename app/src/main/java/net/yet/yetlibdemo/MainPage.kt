package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import net.yet.ui.dialogs.InputDialog
import net.yet.ui.page.TitledPage

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
		val dlg = InputDialog()
		dlg.onOK = {
			toast(it)
		}
		dlg.inputTypeNumber()
		dlg.show(activity, "Title", "EntaoYang")


//		val dlg = MyDialog()
//		dlg.title("Title").msg("Message")
//		dlg.ok("OK")
//		dlg.show(activity)

//		confirm("Title", "Message ? ", "OK", "Cancel") {
//			toast("OK")
//		}

	}


}
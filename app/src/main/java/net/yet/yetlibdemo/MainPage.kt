package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
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
		alert("Title", "Hello")

	}


}
package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import yet.ui.ext.backColorWhite
import yet.ui.page.TitlePage

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */

class MainPage : TitlePage() {


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		contentView.backColorWhite()
		titleBar {
			title("Main")
			actionText("Test").onClick = {
				test()
			}
		}

	}

	fun test() {

	}


}
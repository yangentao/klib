package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import yet.ui.page.TitledPage
import yet.ui.widget.grid.GridSelectPage
import yet.util.log.logd

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */
typealias OnResult = (String) -> Unit

class MainPage : TitledPage() {

	fun test1(r: OnResult) {
		r.invoke("Hello")
	}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title = "Test"

		titleBar.addAction("test").onAction {
			test()
		}

	}

	fun test() {
		val p = GridSelectPage<String>()
		p.title = "选择人员"
		p.items = listOf("Yang", "En", "Tao", "Dou", "Ba", "YeYe")


		p.bindRes {
			it to R.mipmap.ppp
		}
		p.onItemClick = { p, s ->
			logd(s)
			p.finish()
		}



		openPage(p)

	}


}
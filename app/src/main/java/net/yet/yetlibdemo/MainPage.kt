package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import yet.ui.ext.padding
import yet.ui.page.TitledPage
import yet.ui.widget.grid.GridSelectPage

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
		p.onPageCreated = {
			it.gridView.enableLine = true
			it.bindRes {
				it to R.mipmap.ppp
			}
			it.gridView.onItemClick = {
				p.finish()
			}
			it.onConfigCellView = {
				it.padding(15)
			}

			it.gridView.setItems(listOf("Yang", "En", "Tao", "Dou", "Ba", "YeYe"))
		}

		openPage(p)

	}


}
package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import yet.ui.page.TitledPage
import yet.util.log.logd
import yet.yson.YsonObject

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
//			openPage(HelloPage())
		}

	}

	fun test() {
		val s = """
			 {
  "code": 0,
  "msg": "操作成功",
  "data": {
    "token": "317c616e64726f69647c30",
    "user": {
      "admin": 0,
      "birthdate": "1000-01-01",
      "hide": 0,
      "id": 1,
      "info": "",
      "name": "",
      "phone": "15098760059",
      "portrait": 0,
      "sex": 0
    }
  }
}
			 """
		val yo = YsonObject(s)
		logd(yo.toString())
	}


}
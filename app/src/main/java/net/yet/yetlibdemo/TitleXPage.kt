package net.yet.yetlibdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import yet.ui.ext.LParam
import yet.ui.ext.WidthFill
import yet.ui.ext.height
import yet.ui.page.BaseFragment
import yet.ui.res.Res
import yet.ui.viewcreator.createLinearVertical
import yet.ui.widget.TitleBarX

class TitleXPage : BaseFragment() {

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
		super.onCreateView(inflater, container, savedInstanceState)
		val ll = createLinearVertical()
		val titleBar = TitleBarX(activity)
		ll.addView(titleBar, LParam.WidthFill.height(TitleBarX.HEIGHT))

		titleBar.apply {
			titleCenter = false
			title("豆豆最棒")
//			showBack().onClick = {
//				finish()
//			}
			imageCmd("login", Res.portrait).onClick = {
				toast("Hello")
			}
			textCmd("user", "发布").onClick = {
				toast("user")
			}
			menu {
				menuItem("a", "发布", 0)
				menuItem("b", "订单", Res.portrait)
				menuItem("c", "状态", Res.me)
			}
		}
		titleBar.build()

		return ll

	}
}
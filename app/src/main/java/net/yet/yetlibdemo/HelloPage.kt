package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import yet.ui.page.TitlePageX
import yet.ui.res.Res

class HelloPage : TitlePageX() {

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			titleCenter = false
			title("豆豆最棒")
//			showBack().onClick = {
//				finish()
//			}
			cmdImage("login", Res.portrait).onClick = {
				toast("Hello")
			}
			cmdText("user", "发布").onClick = {
				toast("user")
			}
			menu {
				menuItem("a", "发布", 0)
				menuItem("b", "订单", Res.portrait)
				menuItem("c", "状态", Res.me).onClick = {
					toast("Hello ")
				}
			}
		}
	}


}
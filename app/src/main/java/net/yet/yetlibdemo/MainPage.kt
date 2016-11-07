package net.yet.yetlibdemo

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import net.yet.theme.Colors
import net.yet.ui.ext.*
import net.yet.ui.page.TitledPage
import net.yet.util.log.log

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */

class MainPage : TitledPage() {
	val url = "http://app800.cn/dav/temp/a.jpg"

	lateinit var imageView: ImageView

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title = "Test"

		titleBar.addAction("test").onAction {
			test()
		}

		val group = contentView.addRadioGroup {
			widthFill().heightWrap()
		}

		group.addRadioButton("微信") {
			widthFill().heightButton()
		}.styleImageTextCheckRes(R.drawable.image_miss)

		group.addRadioButton("支付宝") {
			widthFill().heightButton()
		}.styleImageTextCheck(null)

	}

	fun test() {
		val c = Color.rgb(255, 128, 0)
		val s = Colors.toStringColor(c)
		log(s)
	}

}
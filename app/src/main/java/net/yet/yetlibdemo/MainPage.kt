package net.yet.yetlibdemo

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import net.yet.ui.ext.*
import net.yet.ui.page.UserTitledPage

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */

class MainPage : UserTitledPage() {


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title = "Test"

		titleBar.addAction("test").onAction {
			toast("Test")
		}

		titleBar.themeBackColor = Color.TRANSPARENT

		val imageView = createImageView()
		imageView.scaleCenterCrop()
		imageView.setImageResource(R.mipmap.user_bg)
		titleRelativeView.addViewParam(imageView) {
			widthFill().height(220)
		}
		titleLinearView.bringToFront()

		statusBarColor(Color.rgb(200, 100, 0))


		val tv = createTextViewA()
		tv.text = "Hello"
		contentView.addViewParam(tv) {
			widthFill().heightWrap()
		}

	}


}
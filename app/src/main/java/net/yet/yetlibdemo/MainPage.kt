package net.yet.yetlibdemo

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import net.yet.ui.ext.*
import net.yet.ui.page.TitledPage
import net.yet.util.imgloader.ImgLoader

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */

class MainPage : TitledPage() {
	val url = "http://app800.cn/dav/temp/a.jpg"

	lateinit var imageView: ImageView

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar.title = "Test"

		titleBar.addAction("show").onAction {
			ImgLoader.display(imageView, url)
		}
		titleBar.addAction("del").onAction {
			ImgLoader.Local.remove(url)
			imageView.setImageDrawable(null)
		}


		imageView = createImageView()
		contentView.addViewParam(imageView) {
			size(320).gravityCenter().margins(30)
		}
	}

}
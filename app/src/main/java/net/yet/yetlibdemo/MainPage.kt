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

		val cl = context.createConstraintLayout()

		contentView.addViewParam(cl) {
			fill()
		}

		val b = cl.createButton("A")
		cl.addViewParam(b) {
			width(200).height(0)
			centerInParent()
			ratioW(2.0, 1.0)
//			centerHorInParent()
//			topToTopOfParent()
		}

	}

}
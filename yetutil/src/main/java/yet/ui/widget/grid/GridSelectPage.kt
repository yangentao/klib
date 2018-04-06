package yet.ui.widget.grid

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import yet.ui.ext.LParam
import yet.ui.ext.WidthFill
import yet.ui.ext.height
import yet.ui.ext.weight
import yet.ui.page.TitledPage
import yet.ui.res.D
import yet.ui.res.limited
import yet.ui.viewcreator.simpleGridView

/**
 * Created by entaoyang@163.com on 2016-08-24.
 * val p = GridSelectPage<String>()
 * p.title = "选择人员"
 * p.items = listOf("Yang", "En", "Tao", "Dou", "Ba", "YeYe")
 * p.bindRes {
 *     it to R.mipmap.ppp
 * }
 * p.onItemClick = { p, s ->
 *     logd(s)
 *     p.finish()
 * }
 * openPage(p)
 */

open class GridSelectPage<T> : TitledPage() {
	lateinit var gridView: SimpleGridView<T>
	var title: String = "选择"
	var imageMaxEdge: Int = 48

	var onPageCreated: (GridSelectPage<T>) -> Unit = {}

	var onConfigCellView: (GridItemView) -> Unit = {}

	fun bindRes(block: (T) -> Pair<String, Int>) {
		gridView.onBindView = { v, item, p ->
			val p = block.invoke(item)
			val d = D.res(p.second).limited(imageMaxEdge)
			(v as GridItemView).setValues(p.first, d)
		}
	}

	fun bindImage(block: (T) -> Pair<String, Drawable>) {
		gridView.onBindView = { v, item, p ->
			val p = block.invoke(item)
			val d = p.second.limited(imageMaxEdge)
			(v as GridItemView).setValues(p.first, d)
		}
	}

	override fun onContentCreated() {
		super.onContentCreated()
		onPageCreated(this)
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title = title
		titleBar.showBack()
		gridView = contentView.simpleGridView<T>(LParam.WidthFill.height(0).weight(1)) {
			onNewView = { _, _ ->
				val v = GridItemView(context)
				v.imageSize(imageMaxEdge)
				onConfigCellView(v)
				v
			}
		}
	}


}
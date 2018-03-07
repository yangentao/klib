package yet.ui.widget.grid

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.page.BaseFragment
import yet.ui.page.TitledPage
import yet.ui.res.Img
import yet.ui.res.limit
import yet.ui.widget.listview.XBaseAdapter

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
	lateinit var gridView: GridView
	lateinit var adapter: XBaseAdapter<T>

	var hasGridLine = true

	var title: String = "选择"

	var columnCount: Int = 3

	var cellBackColor: Int = Colors.WHITE

	var imageMaxEdge: Int = 48
	var horSpace: Int = 20
	var verSpace: Int = 20

	var items: List<T> = emptyList()
	var onItemClick: (BaseFragment, T) -> Unit = { p, item -> }
	var onBindItemView: (GridItemView, T) -> Unit = { v, item -> }

	var onNewItemView: (GridItemView) -> Unit = {}

	var onConfigGrid: (GridView) -> Unit = {}

	fun bindRes(block: (T) -> Pair<String, Int>) {
		onBindItemView = { v, item ->
			val p = block.invoke(item)
			val d = Img.res(p.second).limit(imageMaxEdge)
			v.setValues(p.first, d)
		}
	}

	fun bindImage(block: (T) -> Pair<String, Drawable>) {
		onBindItemView = { v, item ->
			val p = block.invoke(item)
			val d = p.second.limit(imageMaxEdge)
			v.setValues(p.first, d)
		}
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		contentView.backColor(cellBackColor)
		titleBar.title = title
		titleBar.showBack()
		gridView = GridView(context)
		gridView.numColumns = columnCount
		if (hasGridLine) {
			gridView.backColor(Colors.LineGray)
			gridView.padding(1)
			gridView.horizontalSpacing = 1
			gridView.verticalSpacing = 1
		} else {
			gridView.backColor(cellBackColor)
			gridView.padding(horSpace / 2)
			gridView.horizontalSpacing = dp(horSpace)
			gridView.verticalSpacing = dp(verSpace)
		}
		contentView.addViewParam(gridView) {
			widthFill().height(0).weight(1)
		}

		adapter = object : XBaseAdapter<T>() {
			override fun bindView(position: Int, itemView: View, item: T) {
				var view = itemView as GridItemView
				onBindItemView(view, item)

			}

			override fun newView(context: Context, position: Int): View {
				val v = GridItemView(context)
				v.imageSize(imageMaxEdge)
				if (hasGridLine) {
					v.backColor(cellBackColor, Colors.Fade)
					v.padding(horSpace / 2, verSpace / 2, horSpace / 2, verSpace / 2)
				} else {
					v.backColorWhiteFade()
				}
				onNewItemView(v)
				return v
			}

			override fun onRequestItems(): List<T> {
				return this@GridSelectPage.onRequestItems()
			}

		}
		gridView.adapter = adapter
		gridView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
			onItemClick(this@GridSelectPage, adapter.getItem(pos))
		}
		onConfigGrid(gridView)
		adapter.requestItems()
	}


	open fun onRequestItems(): List<T> {
		return items
	}
}
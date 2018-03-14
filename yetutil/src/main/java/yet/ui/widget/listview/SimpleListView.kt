package yet.ui.widget.listview

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ListView
import android.widget.TextView
import yet.theme.Colors
import yet.ui.res.Img

/**
 */
class SimpleListView<T>(context: Context) : ListView(context) {


	var newView: (context: Context, position: Int) -> View = { c, p -> TextView(c) }
	var bindView: (itemView: View, item: T, position: Int) -> Unit = { v, item, p -> }

	var myAdapter: XBaseAdapter<T> = object : XBaseAdapter<T>() {

		override fun bindView(position: Int, itemView: View, item: T) {
			this@SimpleListView.bindView(itemView, item, position)
		}

		override fun newView(context: Context, position: Int): View {
			return this@SimpleListView.newView(context, position)
		}
	}

	init {
		cacheColorHint = 0
		selector = Img.colorStates(Color.TRANSPARENT, Colors.Fade)
		setAdapter(myAdapter)
	}

	fun setItems(items: List<T>) {
		myAdapter.setItems(items)
	}

	fun getItem(position: Int): T {
		return myAdapter.getItem(position)
	}

}

package yet.ui.widget.listview

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ListView
import yet.theme.Colors
import yet.ui.ext.genId
import yet.ui.res.D

/**
 */
class SimpleListView<T>(context: Context) : ListView(context) {


	val myAdapter = SimpleBaseAdapter<T>()
	var onNewView: (context: Context, position: Int) -> View
		get() {
			return myAdapter.onNewView
		}
		set(value) {
			myAdapter.onNewView = value
		}
	var onBindView: (itemView: View, item: T, position: Int) -> Unit
		get() {
			return myAdapter.onBindView
		}
		set(value) {
			myAdapter.onBindView = value
		}

	var onItemClick: (item: T) -> Unit = {}


	init {
		genId()
		cacheColorHint = 0
		selector = D.lightColor(Color.TRANSPARENT, Colors.Fade)
		setAdapter(myAdapter)
		this.setOnItemClickListener { parent, view, position, id ->
			onItemClick(getItem(position))
		}
	}

	fun setItems(items: List<T>) {
		myAdapter.setItems(items)
	}

	fun getItem(position: Int): T {
		return myAdapter.getItem(position)
	}

}

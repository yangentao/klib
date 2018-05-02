package yet.ui.list

import android.content.Context
import android.view.View
import yet.ui.ext.textS
import yet.ui.list.check.CheckView
import yet.ui.list.views.TextItemView

/**
 */
class CheckListView(context: Context) : AnyListView(context) {
	val anyAdapter = AnyAdapter()
	var onNewView: (Context, Int) -> View = { c, p ->
		TextItemView(c)
	}
	var onBindView: (View, Int) -> Unit = { v, p ->
		v as TextItemView
		v.textS = getItem(p).toString()
	}

	init {
		anyAdapter.onNewView = { c, p ->
			val v = this@CheckListView.onNewView(c, p)
			CheckView(c).bind(v)
		}
		anyAdapter.onBindView = { v, p ->
			v as CheckView
			v.isChecked = anyAdapter.isChecked(p)
			this@CheckListView.onBindView(v.view, p)
		}

		onItemClick2 = { v, item, p ->
			v as CheckView
			v.toggle()
			val c = v.isChecked
			anyAdapter.checkPosition(p, c)
		}
		adapter = anyAdapter
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}
}

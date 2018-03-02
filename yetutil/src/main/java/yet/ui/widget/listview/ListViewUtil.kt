package yet.ui.widget.listview

import android.view.View
import android.widget.AdapterView
import android.widget.ListView

/**
 * Created by yet on 2015/10/11.
 */
object ListViewUtil {
	@JvmStatic fun addClick(listView: ListView, listener: ListViewClickListener) {
		listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
			click(listView, view, position, listener)
		}
	}

	@JvmStatic fun addLongClick(listView: ListView, listener: ListViewLongClickListener) {
		listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, position, id ->
			longClick(listView, view, position, listener)
		}
	}

	@JvmStatic fun click(listView: ListView, view: View, position: Int, event: ListViewClickListener?) {
		if (event == null) {
			return
		}
		var countAdapter = 0
		val la = listView.adapter
		if (la != null) {
			countAdapter = la.count
		}
		var pos = position
		if (pos >= 0 && pos < listView.headerViewsCount) {
			event.onItemClickHeader(listView, view, pos)
		}
		pos -= listView.headerViewsCount
		if (pos >= 0 && pos < countAdapter) {
			event.onItemClickAdapter(listView, view, pos)

		}
		pos -= countAdapter
		if (pos >= 0 && pos < listView.footerViewsCount) {
			event.onItemClickFooter(listView, view, pos)
		}
		event.onItemClick(listView, view, position)
	}

	@JvmStatic fun longClick(listView: ListView, view: View, position: Int, event: ListViewLongClickListener?): Boolean {
		if (event == null) {
			return false
		}
		var ret = false
		var countAdapter = 0
		val la = listView.adapter
		if (la != null) {
			countAdapter = la.count
		}
		var pos = position
		if (pos >= 0 && pos < listView.headerViewsCount) {
			ret = ret || event.onItemLongClickHeader(listView, view, pos)
		}
		pos -= listView.headerViewsCount
		if (pos >= 0 && pos < countAdapter) {
			ret = ret || event.onItemLongClickAdapter(listView, view, pos)
		}
		pos -= countAdapter
		if (pos >= 0 && pos < listView.footerViewsCount) {
			ret = ret || event.onItemLongClickFooter(listView, view, pos)
		}
		ret = ret || event.onItemLongClick(listView, view, position)
		return ret
	}


}

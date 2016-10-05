package net.yet.yetlibdemo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import net.yet.sys.contact.ContactSelectPage
import net.yet.ui.activities.PageUtil
import net.yet.ui.activities.TitledActivity
import net.yet.ui.dialogs.ListViewDialog
import net.yet.ui.ext.addViewParam
import net.yet.ui.ext.heightWrap
import net.yet.ui.ext.widthWrap
import net.yet.ui.widget.Action
import net.yet.ui.widget.listview.AdapterListView
import net.yet.ui.widget.listview.itemview.TextItemView
import java.util.*

class MainActivity : TitledActivity() {
	val TEST = "Text"
	override fun onCreateContent(contentView: LinearLayout) {
		super.onCreateContent(contentView)
		titleBar.title = "主页"
		titleBar.addAction(TEST)
		titleBar.showBack()
		titleBar.titleAlignCenter = true
		titleBar.titleStyleDropdown = true
		titleBar.onTitleClick = {
			this@MainActivity.onTitleClick(it)
		}

		val lv = object : AdapterListView<String>(this) {

			override fun newView(context: Context, position: Int, parent: ViewGroup): View {
				return TextItemView(context)
			}

			override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: String) {
				val v = itemView as TextItemView
				v.text = item
//				v.icon(ResConst.back())
			}

			override fun onItemClickAdapter(listView: ListView, view: View, position: Int) {
				toast(this.getItem(position))
			}
		}
		lv.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
			toast("Hello")
		}
		contentView.addViewParam(lv) {
			widthWrap().heightWrap()
		}
		val ls = ArrayList<String>()
		for (i in 0..30) {
			ls.add("Item   $i")
		}
		lv.setItems(ls)
	}

	override fun onTitleBarAction(action: Action) {
		if (action.isTag(TEST)) {
			PageUtil.open(this, ContactSelectPage())
		}
	}

	fun onTitleClick(title: String) {
		val dlg = object : ListViewDialog() {
			override fun onSelect(index: Int, s: String) {
				toast(s)
			}
		}
		dlg.gravityTop = true
		dlg.addItems("A", "B", "C")
		dlg.show(this, "Title")
	}

}

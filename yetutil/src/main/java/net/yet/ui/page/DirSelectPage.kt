package net.yet.ui.page

import android.content.Context
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import net.yet.ui.activities.PageUtil
import net.yet.ui.widget.Action
import net.yet.ui.widget.TitleBar
import net.yet.ui.widget.listview.itemview.TextDetailView
import net.yet.util.OnValue
import net.yet.util.Util
import net.yet.util.xlog
import java.io.File
import java.util.*


class DirSelectPage : ListPage<File>() {

	private var file: File? = null
	private var onValue: OnValue<File>? = null

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar.showBack()
		titleBar.title = file!!.absolutePath + "/"
		titleBar.addAction(SELECT).icon(SELECT)
		requestItems()

	}

	override fun newView(context: Context, position: Int, parent: ViewGroup, item: File): View {
		return TextDetailView(context)
		//        return TextDetailView(context);
	}

	override fun onDestroy() {
		super.onDestroy()
		onValue = null
		file = null
	}

	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (action.isTag(SELECT)) {
			if (onValue != null) {
				onValue!!.onValue(file)
				finish()
			}
			return
		}
		super.onTitleBarAction(bar, action)
	}

	override fun onRequestItems(): List<File> {
		val files = file!!.listFiles()
		var ls: List<File> = Util.asList(*files).filter { !it.name.startsWith(".") }
		Collections.sort(ls) { lhs, rhs -> lhs.name.compareTo(rhs.name) }
		return ls
	}

	override fun onCreateListViewHeaderFooter(context: Context, listView: ListView) {
		val v = TextDetailView(activity)
		v.setValues("上级目录..", null)
		listView.addHeaderView(v)
	}

	override fun onItemClickHeader(listView: ListView, view: View, position: Int) {
		goUp()
	}

	private fun goUp() {
		val f = file!!.parentFile
		if (f != null) {
			DirSelectPage.open(activity, f, onValue!!)
			finish()
		}
	}

	override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: File) {
		val v = itemView as TextDetailView
		var s = item.name
		if (item.isDirectory) {
			s += "/"
		}
		v.setValues(s, null)

	}

	override fun onItemClickAdapter(listView: ListView, view: View, position: Int) {
		val item = getItem(position)
		if (item.isDirectory) {
			DirSelectPage.open(activity, item, onValue!!)
			finish()
		}
	}

	companion object {
		private val SELECT = "选择"

		fun open(context: Context, onValue: OnValue<File>) {
			open(context, Environment.getExternalStorageDirectory(), onValue)
		}

		fun open(context: Context, dir: File, onValue: OnValue<File>) {
			if (Util.debug() && !dir.isDirectory) {
				xlog.fatal("应该给一个存在的目录做参数")
			}
			val page = DirSelectPage()
			page.file = dir
			page.onValue = onValue
			PageUtil.open(context, page)
		}
	}

}

package yet.ui.page

import android.Manifest
import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import yet.ui.activities.Pages
import yet.ui.widget.listview.itemview.TextDetailView
import yet.util.Util
import yet.util.app.App
import yet.util.log.xlog
import java.io.File
import java.util.*


class FileSelectPage : ListPage<File>() {

	var dir: File = Environment.getExternalStorageDirectory()
	lateinit var onValue: (File?) -> Unit

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar.showBack()
		titleBar.title = dir.absolutePath
		titleBar.addAction(CLEAR) {
			onValue(null)
			finish()
		}
		requestItems(Manifest.permission.READ_EXTERNAL_STORAGE)

	}

	override fun onItemsRefreshed() {
		super.onItemsRefreshed()
		titleBar.title = dir.absolutePath
		titleBar.commit()
	}

	override fun newView(context: Context, position: Int, item: File): View {
		return TextDetailView(context)
	}

	override fun onRequestItems(): List<File> {
		val files = dir.listFiles()
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

	override fun onBackPressed(): Boolean {
		if (dir.absolutePath == RootSD.absolutePath) {
			return super.onBackPressed()
		}
		goUp()
		return true
	}

	private fun goUp() {
		if (dir.absolutePath == RootSD.absolutePath) {
			return
		}
		val f = dir.parentFile
		if (f != null) {
			dir = f
			requestItems(Manifest.permission.READ_EXTERNAL_STORAGE)
		}
	}

	override fun bindView(position: Int, itemView: View, item: File) {
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
			dir = item
			requestItems()
		} else {
			onValue(item)
			finish()
		}
	}

	companion object {
		private val CLEAR = "清除"
		val RootSD = Environment.getExternalStorageDirectory()

		fun open(context: Context, onValue: (File?) -> Unit) {
			open(context, Environment.getExternalStorageDirectory(), onValue)
		}

		fun open(context: Context, dir: File, onValue: (File?) -> Unit) {
			if (App.debug && !dir.isDirectory) {
				xlog.fatal("应该给一个存在的目录做参数")
			}
			val page = FileSelectPage()
			page.dir = dir
			page.onValue = onValue
			Pages.open(context, page)
		}
	}

}

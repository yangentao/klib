package net.yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import java.util.*

/**
 * 简单字符串选择
 */
class StringSelectDialog {
	private val list = ArrayList<String>(64)

	var onConfig: (AlertDialog) -> Unit = {}
	var onSelect: (Int, String) -> Unit = { n, s -> }

	fun size(): Int {
		return list.size
	}

	fun addItems(vararg items: String) {
		for (s in items) {
			list.add(s)
		}
	}

	fun addItems(items: Collection<String>) {
		for (s in items) {
			list.add(s)
		}
	}

	fun show(context: Context, title: String? = null): AlertDialog {
		val data = list.toTypedArray()
		val builder = AlertDialog.Builder(context)
		builder.setTitle(title)
		builder.setItems(data) { dialog, which ->
			dialog.dismiss()
			onSelect(which, list[which])
		}
		builder.setCancelable(true)
		val dlg = builder.create()
		dlg.setCanceledOnTouchOutside(true)
		onConfig(dlg)
		dlg.show()
		return dlg
	}
}

package net.yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import net.yet.ui.ext.dp
import java.util.*

/**
 * 简单字符串选择
 */
abstract class ListViewDialog {
	private val list = ArrayList<String>(64)
	var gravityTop = false
	var topMargin = 45


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

		onConfigDialog(dlg)
		dlg.show()
		return dlg
	}

	open fun onConfigDialog(dlg: AlertDialog) {
		if (gravityTop) {
			val lp = dlg.window.attributes
			lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
			lp.y = dp(topMargin)
		}
	}

	abstract fun onSelect(index: Int, s: String)
}

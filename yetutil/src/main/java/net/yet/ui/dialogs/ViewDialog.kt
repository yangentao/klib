package net.yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import net.yet.R
import net.yet.locale.LibS

abstract class ViewDialog {
	var isOK = false
		private set
	var okText = LibS(R.string.ok)
	var cancelText = LibS(R.string.cancel)


	fun show(context: Context, view: View, title: String?) {
		create(context, view, title).show()
	}


	fun create(context: Context, view: View, title: String?): AlertDialog {
		val builder = AlertDialog.Builder(context)
		builder.setTitle(title)
		builder.setView(view)
		builder.setPositiveButton(okText, DialogInterface.OnClickListener { dlg, arg1 ->
			isOK = true
			dlg.dismiss()
			onOK(view)
		})
		builder.setNegativeButton(cancelText, DialogInterface.OnClickListener { dlg, arg1 -> dlg.dismiss() })
		val dlg = builder.create()
		dlg.setCanceledOnTouchOutside(true)
		dlg.setCancelable(true)
		dlg.setOnDismissListener { this@ViewDialog.onDismiss() }
		return dlg
	}

	fun onDismiss() {

	}

	abstract fun onOK(view: View)
}

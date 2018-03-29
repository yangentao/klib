package yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import net.yet.R
import yet.ui.res.Res

abstract class ViewDialog {
	var isOK = false
		private set
	var okText = Res.str(R.string.yet_ok)
	var cancelText = Res.str(R.string.yet_cancel)


	fun show(context: Context, view: View, title: String?) {
		create(context, view, title).show()
	}


	fun create(context: Context, view: View, title: String?): AlertDialog {
		val builder = AlertDialog.Builder(context)
		builder.setTitle(title)
		builder.setView(view)
		builder.setPositiveButton(okText, DialogInterface.OnClickListener { dlg, _ ->
			isOK = true
			dlg.dismiss()
			onOK(view)
		})
		builder.setNegativeButton(cancelText, DialogInterface.OnClickListener { dlg, _ -> dlg.dismiss() })
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

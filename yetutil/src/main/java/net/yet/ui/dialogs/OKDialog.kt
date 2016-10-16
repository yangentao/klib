package net.yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import net.yet.R
import net.yet.util.app.Res


open class OKDialog {
	var dlg: AlertDialog? = null
	var okText = Res.str(R.string.ok)

	fun setOKText(s: String): OKDialog {
		okText = s
		return this
	}

	@JvmOverloads fun show(context: Context, title: String, msg: String? = null) {
		create(context, title, msg).show()
	}

	fun create(context: Context, title: String, msg: String? = null): AlertDialog {
		val builder = AlertDialog.Builder(context)
		builder.setMessage(msg)
		builder.setTitle(title)
		builder.setPositiveButton(okText, DialogInterface.OnClickListener { dlg, arg1 ->
			dlg.dismiss()
			onOK()
		})
		dlg = builder.create()
		dlg!!.setCanceledOnTouchOutside(true)
		dlg!!.setCancelable(true)
		return dlg!!
	}

	fun dismiss() {
		if (dlg != null) {
			dlg!!.dismiss()
		}
	}

	open fun onOK() {

	}
}

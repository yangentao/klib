package yet.ui.dialogs

import android.app.ProgressDialog
import android.content.Context
import yet.util.mainThread

class SpinProgressDlg(context: Context) {
	var dlg: ProgressDialog = ProgressDialog(context)

	init {
		dlg.setCancelable(true)
		dlg.setCanceledOnTouchOutside(false)
	}

	fun cancelable(cancelable: Boolean): SpinProgressDlg {
		mainThread {
			dlg.setCancelable(cancelable)
		}
		return this
	}

	fun dismiss(): SpinProgressDlg {
		mainThread {
			dlg.dismiss()
		}
		return this
	}

	fun showLoading(): SpinProgressDlg {
		show("加载中...")
		return this
	}

	fun show(msg: String): SpinProgressDlg {
		mainThread {
			if (dlg.isShowing) {
				dlg.dismiss()
			}
			dlg.setMessage(msg)
			dlg.show()
		}
		return this
	}

	fun msg(msg: String): SpinProgressDlg {
		mainThread {
			dlg.setMessage(msg)
		}
		return this
	}

}

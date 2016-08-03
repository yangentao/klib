package net.yet.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import net.yet.util.TaskUtil;

public class SpinProgressDlg {
	public ProgressDialog dlg;

	public SpinProgressDlg(Context context) {
		dlg = new ProgressDialog(context);
		dlg.setCancelable(true);
		dlg.setCanceledOnTouchOutside(false);
	}

	public SpinProgressDlg setCancelable(boolean cancelable) {
		dlg.setCancelable(cancelable);
		return this;
	}

	public SpinProgressDlg dismiss() {
		dlg.dismiss();
		return this;
	}

	public SpinProgressDlg showLoading() {
		show("加载中...");
		return this;
	}

	public SpinProgressDlg show(String msg) {
		if (dlg.isShowing()) {
			dlg.dismiss();
		}
		dlg.setMessage(msg);
		dlg.show();
		return this;
	}

	public SpinProgressDlg setMsg(final String msg) {
		if (TaskUtil.inMainThread()) {
			dlg.setMessage(msg);
		} else {
			TaskUtil.fore(new Runnable() {
				@Override
				public void run() {
					dlg.setMessage(msg);
				}
			});
		}
		return this;
	}

}

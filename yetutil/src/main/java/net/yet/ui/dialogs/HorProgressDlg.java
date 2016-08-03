package net.yet.ui.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import net.yet.util.TaskUtil;

public class HorProgressDlg {
	private ProgressDialog dlg;
	private int value = 0;
	private long preTime = 0;
	private boolean cache = true;

	public HorProgressDlg(Context context) {
		dlg = new ProgressDialog(context);
		dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(true);
		dlg.setMax(100);
	}

	public HorProgressDlg setCancelable(boolean cancelable) {
		dlg.setCancelable(cancelable);
		return this;
	}

	public HorProgressDlg setTitle(final String title) {
		if (TaskUtil.inMainThread()) {
			dlg.setTitle(title);
		} else {
			TaskUtil.fore(new Runnable() {
				@Override
				public void run() {
					dlg.setTitle(title);
				}
			});
		}
		return this;
	}

	public HorProgressDlg show(final String title) {
		if (dlg.isShowing()) {
			dlg.dismiss();
		}
		dlg.setMax(100);
		dlg.setProgress(0);
		if (TaskUtil.inMainThread()) {
			dlg.setTitle(title);
			dlg.show();
		} else {
			TaskUtil.fore(new Runnable() {
				@Override
				public void run() {
					dlg.setTitle(title);
					dlg.show();
				}
			});
		}
		return this;
	}

	public HorProgressDlg dismiss() {
		dlg.dismiss();
		return this;
	}

	public int getMax() {
		return dlg.getMax();
	}

	public HorProgressDlg setMax(final long max) {
		dlg.setMax((int) max);
		dlg.setProgress(0);
		return this;
	}

	public int getValue() {
		return value;
	}

	public HorProgressDlg setValue(long progress) {
		this.value = (int) progress;
		if (!cache) {
			dlg.setProgress(this.value);
		} else {
			if (value == 0 || value == dlg.getMax()) {
				dlg.setProgress(this.value);
			} else {
				long cur = System.currentTimeMillis();
				if (cur - preTime > 100) {
					dlg.setProgress(value);
					preTime = cur;
				}
			}
		}
		return this;
	}

	public HorProgressDlg setCacheable(boolean cache) {
		this.cache = cache;
		return this;
	}

}

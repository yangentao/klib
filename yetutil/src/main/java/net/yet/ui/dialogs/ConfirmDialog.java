package net.yet.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import net.yet.R;
import net.yet.util.app.Res;

public abstract class ConfirmDialog {
	public String okText = Res.str(R.string.ok);

	public ConfirmDialog setOKText(String ok) {
		this.okText = ok;
		return this;
	}

	public AlertDialog show(Context context, String msg) {
		return show(context, null, msg);
	}

	public AlertDialog show(Context context, String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dlg, int arg1) {
				dlg.dismiss();
				onOK();

			}
		});
		builder.setNegativeButton(Res.str(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dlg, int arg1) {
				dlg.dismiss();
				onCancel();
			}
		});
		AlertDialog dlg = builder.create();
		dlg.setCanceledOnTouchOutside(true);
		dlg.setCancelable(true);
		dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				ConfirmDialog.this.onCancel();
			}
		});
		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				ConfirmDialog.this.onDismiss();
			}
		});
		dlg.show();
		return dlg;
	}

	protected void onDismiss() {

	}

	protected void onCancel() {

	}

	public abstract void onOK();
}

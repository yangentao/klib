package net.yet.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 简单字符串选择
 */
public abstract class StringSelectDialogX<T> {
    private ArrayList<T> list = new ArrayList<T>(64);

    public void addItems(T... items) {
        for (T s : items) {
            list.add(s);
        }
    }

    public void addItems(Collection<T> items) {
        for (T s : items) {
            list.add(s);
        }
    }

    public AlertDialog show(Context context) {
        List<String> ls = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); ++i) {
            T item = list.get(i);
            ls.add(makeString(i, item));
        }

        String[] data = ls.toArray(new String[ls.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(data, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onSelect(which, list.get(which));
            }
        });
        builder.setCancelable(true);
        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();
        return dlg;
    }

    abstract protected void onSelect(int index, T item);

    abstract protected String makeString(int index, T item);
}

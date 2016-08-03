package net.yet.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 简单字符串选择
 */
public abstract class ListViewDialog {
    private ArrayList<String> list = new ArrayList<String>(64);

    public int size() {
        return list.size();
    }

    public void addItems(String... items) {
        for (String s : items) {
            list.add(s);
        }
    }

    public void addItems(Collection<String> items) {
        for (String s : items) {
            list.add(s);
        }
    }

    public AlertDialog show(Context context) {
        return show(context, null);
    }

    public AlertDialog show(Context context, String title) {
        String[] data = list.toArray(new String[list.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
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

    abstract protected void onSelect(int index, String s);
}

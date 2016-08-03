package net.yet.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import net.yet.ui.widget.listview.XBaseAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 适合小数据, 几十个还可以
 * 大数据使用SelectDialog
 */
public class ViewSelectDialog {
    public interface ViewSelectDialogCallback {
        void onDialogSelectView(int position, View view);
    }

    private final ArrayList<View> items = new ArrayList<View>(64);

    public void addView(View view) {
        items.add(view);
    }

    public void reverseViews() {
        Collections.reverse(items);
    }

    public AlertDialog show(Context context, final ViewSelectDialogCallback callback) {
        final XBaseAdapter<View> adapter = new XBaseAdapter<View>() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return items.get(position);
            }
        };
        adapter.setItems(items);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callback.onDialogSelectView(which, adapter.getItem(which));
            }

        });
        builder.setCancelable(true);
        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();
        return dlg;
    }

}

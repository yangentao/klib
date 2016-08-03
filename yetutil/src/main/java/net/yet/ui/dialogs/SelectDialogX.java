package net.yet.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import net.yet.ui.widget.listview.ActionAdapter;
import net.yet.util.TaskUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SelectDialogX<T> {
    private List<T> items = new ArrayList<T>(64);
    AlertDialog dlg;

    private ActionAdapter<T> adapter = new ActionAdapter<T>() {
        @Override
        public void bindView(int position, View itemView, ViewGroup parent, T item, int viewType) {
            SelectDialogX.this.bindView(itemView, position, item);

        }

        @Override
        public View newView(Context context, int position, ViewGroup parent, int viewType) {
            return SelectDialogX.this.newView(context, position, getItem(position));
        }

        @Override
        protected void onItemAction(int position, int actionIndex, T item) {
            SelectDialogX.this.onItemAction(position, actionIndex, item);
            TaskUtil.fore(new Runnable() {
                @Override
                public void run() {
                    dlg.dismiss();
                }
            });
        }
    };

    public void setItems(List<T> data) {
        items.clear();
        items.addAll(data);
    }

    public void addItems(Collection<T> data) {
        items.addAll(data);
    }

    public void addItems(T... data) {
        for (T item : data) {
            items.add(item);
        }
    }

    public AlertDialog show(Context context) {
        return show(context, null);
    }
    public AlertDialog show(Context context, String title) {
        adapter.setItems(items);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {

                onSelectItem(which, adapter.getItem(which));
                TaskUtil.fore(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(true);
        dlg = builder.create();
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();
        return dlg;
    }

    public abstract void onSelectItem(int position, T item);

    public abstract void bindView(View view, int position, T item);

    public abstract View newView(Context context, int position, T item);

    protected abstract void onItemAction(int position, int actionIndex, T item);

}

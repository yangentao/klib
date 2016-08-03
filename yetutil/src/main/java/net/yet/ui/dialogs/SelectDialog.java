package net.yet.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import net.yet.ui.widget.listview.TypedAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SelectDialog<T> {
    private List<T> items = new ArrayList<T>(64);

    private TypedAdapter<T> adapter = new TypedAdapter<T>() {
        @Override
        public void bindView(int position, View itemView, ViewGroup parent, T item, int viewType) {
            SelectDialog.this.bindView(itemView, position, item);

        }

        @Override
        public View newView(Context context, int position, ViewGroup parent, int viewType) {
            return SelectDialog.this.newView(context, position, getItem(position));
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
        adapter.setItems(items);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onSelectItem(which, adapter.getItem(which));
            }
        });
        builder.setCancelable(true);
        AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(true);
        dlg.show();
        return dlg;
    }

    public abstract void onSelectItem(int position, T item);

    public abstract void bindView(View view, int position, T item);

    public abstract View newView(Context context, int position, T item);

}

package net.yet.ui.dialogs;

import android.content.Context;
import android.view.View;

import net.yet.ui.widget.listview.itemview.TextDetailView;

public abstract class TextDetailSelectDialog<T> extends SelectDialog<T> {

    @Override
    public View newView(Context context, int position, T item) {
        return new TextDetailView(context);
    }

    @Override
    public void bindView(View view, int position, T item) {
        onBindView((TextDetailView) view, position, item);
    }

    public abstract void onBindView(TextDetailView view, int position, T item);
}

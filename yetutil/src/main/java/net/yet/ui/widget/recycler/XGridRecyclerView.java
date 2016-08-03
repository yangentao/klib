package net.yet.ui.widget.recycler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import net.yet.theme.Colors;
import net.yet.ui.util.XView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yet on 2015/10/28.
 */
public abstract class XGridRecyclerView<T> extends RecyclerView {
    private final List<T> items = new ArrayList<>();
    private int normalColor = Color.WHITE;
    private int pressColor = Colors.Fade;
    private boolean enableItemBackColor = true;
    private GridLayoutManager layoutManager;

    public XGridRecyclerView(Context context, int columns) {
        super(context);
        layoutManager = new GridLayoutManager(context, columns);
        setLayoutManager(layoutManager);
        setAdapter(new GridAdapter());
        setBackgroundColor(Color.WHITE);
    }

    public void setReverse(boolean reverse) {
        layoutManager.setReverseLayout(reverse);
    }

    public void setSelector(boolean enable, int normalColor, int pressColor) {
        this.normalColor = normalColor;
        this.pressColor = pressColor;
        this.enableItemBackColor = enable;
    }

    public void notifyDataSetChanged() {
        this.getAdapter().notifyDataSetChanged();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void setItems(Collection<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        this.notifyDataSetChanged();
    }

    public int getItemCount() {
        return items.size();
    }

    protected long getItemId(int position) {
        return position;
    }

    protected int getItemViewType(int position) {
        return 0;
    }

    protected abstract View newView(Context context, int viewType, ViewGroup parent);

    protected abstract void bindView(GridHolder holder, View itemView, int position, T item);

    protected abstract void onItemClick(GridHolder holder, View itemView, int position, T item);

    public class GridHolder extends ViewHolder implements OnClickListener {
        public LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        public GridHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            onItemClick(this, this.itemView, pos, getItem(pos));
        }

    }

    private class GridAdapter extends Adapter<GridHolder> {

        @Override
        public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = XGridRecyclerView.this.newView(parent.getContext(), viewType, parent);
            if (enableItemBackColor) {
                XView.view(view).backColor(normalColor, pressColor);
            }
            return new GridHolder(view);
        }

        @Override
        public void onBindViewHolder(GridHolder holder, int position) {
            XGridRecyclerView.this.bindView(holder, holder.itemView, position, getItem(position));
            holder.itemView.setLayoutParams(holder.layoutParams);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return XGridRecyclerView.this.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return XGridRecyclerView.this.getItemId(position);
        }
    }
}

package net.yet.ui.widget.recycler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
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
public abstract class XActionRecyclerView<T> extends RecyclerView {
    private final List<T> items = new ArrayList<>();
    private int normalColor = Color.WHITE;
    private int pressColor = Colors.Fade;
    private boolean enableItemBackColor = true;
    private int actionColor = Colors.TRANS;
    private int actionPress = Colors.Fade;
    private boolean actionColorEnable = true;
    private LinearLayoutManager layoutManager;

    public XActionRecyclerView(Context context) {
        super(context);
        layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);
        setAdapter(new XListAdapter());
        setBackgroundColor(Color.WHITE);
        this.addItemDecoration(new DividerItemDecoration(context));
    }

    public void setReverse(boolean reverse) {
        layoutManager.setReverseLayout(reverse);
    }

    public void setActionSelector(boolean enable, int normalColor, int pressColor) {
        this.actionColorEnable = enable;
        this.actionColor = normalColor;
        this.actionPress = pressColor;
    }

    public void setSelector(boolean enable, int normalColor, int pressColor) {
        this.normalColor = normalColor;
        this.pressColor = pressColor;
        this.enableItemBackColor = enable;
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }

    public void setItems(Collection<T> ls) {
        this.items.clear();
        this.items.addAll(ls);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public int getItemCount() {
        return items.size();
    }

    public long getItemId(int position) {
        return position;
    }

    protected int getItemViewType(int position) {
        return 0;
    }

    protected abstract View newView(Context context, int viewType, ViewGroup parent);

    protected abstract void bindView(ListHolder holder, View itemView, int position, T item);

    protected abstract void onItemClick(ListHolder holder, View itemView, int position, T item);

    protected abstract void onItemActionClick(ListHolder holder, View itemView, int position, View actionView, int actionIndex, T item);

    protected boolean onItemLongClick(ListHolder holder, View itemView, int position, T item) {
        return false;
    }

    public class ListHolder extends ViewHolder implements OnClickListener, OnLongClickListener {
        public LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        private ActionItemView actionItemView;

        public ListHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            if (itemView instanceof ActionItemView) {
                actionItemView = (ActionItemView) itemView;
                for (int i = 0; i < actionItemView.getActionCount(); ++i) {
                    View v = actionItemView.getActionView(i);
                    v.setOnClickListener(this);
                }
            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (v == this.itemView) {
                onItemClick(this, this.itemView, pos, getItem(pos));
            } else if (null != actionItemView) {
                for (int i = 0; i < actionItemView.getActionCount(); ++i) {
                    View av = actionItemView.getActionView(i);
                    if (v == av) {
                        onItemActionClick(this, itemView, pos, av, i, getItem(pos));
                        break;
                    }
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int pos = getAdapterPosition();
            return onItemLongClick(this, this.itemView, pos, getItem(pos));
        }
    }

    private class XListAdapter extends Adapter<ListHolder> {

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = newView(parent.getContext(), viewType, parent);
            if (enableItemBackColor) {
                XView.view(view).backColor(normalColor, pressColor);
            }
            if (actionColorEnable && (view instanceof ActionItemView)) {
                ActionItemView vs = (ActionItemView) view;
                for (int i = 0; i < vs.getActionCount(); ++i) {
                    View v = vs.getActionView(i);
                    XView.view(v).backColor(actionColor, actionPress);
                }
            }
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            bindView(holder, holder.itemView, position, getItem(position));
            holder.itemView.setLayoutParams(holder.layoutParams);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return XActionRecyclerView.this.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return XActionRecyclerView.this.getItemId(position);
        }
    }
}

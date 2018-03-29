package yet.ui.widget;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import yet.util.BackFore;
import yet.util.TaskUtil;

/**
 * 类似ListView, 适合小数据.
 * 分割线的颜色就是背景色
 */
public class LinearAdapterPanel<T> extends LinearPanel {
    private final List<T> items = new ArrayList<>();
    private int limitCount = 0;//限制显示个数
    private LinearAdapter<T> adapter;

    public LinearAdapterPanel(Context context) {
        super(context);
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    public void setAdapter(LinearAdapter<T> adapter) {
        if (this.adapter != null) {
            setItems(null);
        }
        this.adapter = adapter;
    }

    public void setItems(List<T> items) {
        clean();
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        if (adapter != null) {
            int n = 0;
            for (T item : this.items) {
                ++n;
                if (limitCount > 0 && n > limitCount) {
                    break;
                }
                View view = adapter.onCreateItemView(getContext(), item);
                addItemView(view);
            }
        }
    }

    //请求数据集, 后台线程回调onQueryItems
    public void requestItems() {

        TaskUtil.backFore(new BackFore() {
            List<T> data;

            @Override
            public void onFore() {
                setItems(data);
            }

            @Override
            public void onBack() {
                if (adapter != null) {
                    data = adapter.onRequestItems();
                }
            }
        });
    }

}

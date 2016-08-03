package net.yet.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.yet.ui.util.LayerUtil;
import net.yet.ui.util.ShapeUtil;
import net.yet.ui.util.XView;
import net.yet.util.LazyTask;
import net.yet.util.MultiHashMapArray;
import net.yet.util.TaskUtil;
import net.yet.util.Util;
import net.yet.util.xlog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by yet on 2015/10/29.
 */
public abstract class ListIndexBar<T extends IIndexable> extends LinearLayout {

    private View selectView;
    private Drawable selectDrawable = bgDraw();
    private Comparator<Character> tagComparator = null;
    private ArrayList<Character> tagList;
    private int darkColor = Util.argb("#ccc");
    private int normalColor = Color.TRANSPARENT;
    private TextView feedbackView;
    private HashMap<Character, Integer> tagPosMap = new HashMap<>(30);
    private ListView listView;

    private Runnable hideFeedbackRun = new Runnable() {

        @Override
        public void run() {
            XView.view(feedbackView).gone();
        }
    };
    private Comparator<T> defComp = new Comparator<T>() {
        @Override
        public int compare(T lhs, T rhs) {
            Comparable c = (Comparable) lhs;
            return c.compareTo(rhs);
        }
    };
    private OnTouchListener touchListener = new OnTouchListener() {

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            int y = (int) event.getY();
            if (action == MotionEvent.ACTION_DOWN) {
                setBackgroundColor(darkColor);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                setBackgroundColor(normalColor);
                selectByY(y);
            } else if (action == MotionEvent.ACTION_MOVE) {
                selectByY(y);
            }
            return false;
        }
    };

    public ListIndexBar(Context context, RelativeLayout feedbackParentView, ListView listView) {
        super(context);
        this.listView = listView;
        XView.view(this).orientationVertical().gravityCenterHorizontal().padding(0, 0, 0, 0).clickable();
        setOnTouchListener(touchListener);
        feedbackView = XView.createTextView(feedbackParentView.getContext());
        Drawable d = ShapeUtil.round(10, Util.argb("#555"), 2, Util.argb("#ddd"));
        XView.view(feedbackView).textColor(Color.WHITE).textSize(50).gravityCenter()
                .backDrawable(d).gone();
        feedbackParentView.addView(feedbackView, XView.relativeParam().centerInParent().size(70).get());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0) {
                    Object obj = ListIndexBar.this.listView.getAdapter().getItem(firstVisibleItem);
                    if (obj instanceof IIndexable) {
                        IIndexable item = (IIndexable) obj;
                        select(item.getIndexTag());
                    } else {
                        xlog.e("TagIndexBar ! ", firstVisibleItem, obj, " is not IIndexable ", obj == null ? "null" : obj.getClass());
                    }
                }
            }
        });
    }

    protected abstract T makeTagItem(char tag);

    private void onIndexChanged(char newTag, int adapterPosition) {
        if (adapterPosition == 0) {
            listView.setSelection(0);
        } else {
            listView.setSelection(adapterPosition + listView.getHeaderViewsCount());
        }
    }

    private void selectByY(int y) {
        for (int i = 0; i < getChildCount(); ++i) {
            View itemView = getChildAt(i);
            if (y >= itemView.getTop() && y <= itemView.getBottom()) {
                if (selectView != itemView) {
                    TextView tv = (TextView) itemView;
                    final char tag = tv.getText().toString().charAt(0);
                    select(tag, true);
                    TaskUtil.fore(new Runnable() {
                        @Override
                        public void run() {
                            onIndexChanged(tag, tagPosMap.get(tag));
                        }
                    });
                }
                break;
            }
        }
    }

    public void select(char tag) {
        select(tag, false);
    }

    private void select(char tag, boolean feedback) {
        int tagIndex = tagList.indexOf(tag);
        if (tagIndex >= 0) {
            if (selectView != null) {
                selectView.setBackgroundColor(Color.TRANSPARENT);
            }
            selectView = getChildAt(tagIndex);
            selectView.setBackgroundDrawable(selectDrawable);

            String str = ((TextView) selectView).getText().toString();
            XView.view(feedbackView).text(str);
            if (feedbackView != null && feedback) {
                XView.view(feedbackView).visiable();
                LazyTask.schedule("tag.feedback", 650, hideFeedbackRun);
            }
        }
    }

    /**
     * @param items
     * @param autoHidenSize 10:表示当结果集小于10个的时候, 自动隐藏;  <=0表示不执行显示隐藏,保持原来的
     * @return
     */
    public List<T> processItems(final List<T> items, final int autoHidenSize, Comparator<T> itemComparator) {
        MultiHashMapArray<Character, T> multiMap = new MultiHashMapArray<>(30, 50);
        for (T item : items) {
            char tag = item.getIndexTag();
            multiMap.put(tag, item);
        }
        Set<Character> tagSet = multiMap.keySet();
        final ArrayList<Character> tagList = new ArrayList<>(tagSet.size() + 1);
        tagList.addAll(tagSet);
        if (tagComparator != null) {
            Collections.sort(tagList, tagComparator);
        } else {
            Collections.sort(tagList);
        }

        ArrayList<T> newItems = new ArrayList<>(items.size() + tagList.size() + 1);
        for (char tag : tagList) {
            tagPosMap.put(tag, newItems.size());
            ArrayList<T> ls = multiMap.get(tag);//不会出现ls是空的情况!
            if (itemComparator != null) {
                Collections.sort(ls, itemComparator);
            } else {
                if (ls.size() > 0) {//一定>0
                    T item = ls.get(0);
                    if (item instanceof Comparable) {
                        Collections.sort(ls, defComp);
                    }
                }

            }
            T tagItem = makeTagItem(tag);
            newItems.add(tagItem);
            for (T item : ls) {
                newItems.add(item);
            }
        }

        TaskUtil.fore(new Runnable() {
            @Override
            public void run() {
                if (autoHidenSize > 0) {
                    setVisibility(items.size() >= autoHidenSize ? View.VISIBLE : View.GONE);
                }
                buildTagViews(tagList);
            }
        });
        return newItems;
    }

    private void buildTagViews(ArrayList<Character> tagList) {
        this.tagList = tagList;
        removeAllViews();
        for (char s : tagList) {
            TextView v = XView.createTextView(getContext());
            v.setTag(s);
            XView.view(v).text(String.valueOf(s)).textSizeD().textColor(Color.BLACK).gravityCenter();
            addView(v, XView.linearParam().width(40).height(0).weight(1).gravityCenter().get());
        }
    }

    public void setTagComparator(Comparator<Character> tagComparator) {
        this.tagComparator = tagComparator;
    }

    private Drawable bgDraw() {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.GRAY);
        gd.setStroke(2, Color.WHITE);
        gd.setCornerRadius(5);

        LayerUtil lu = new LayerUtil();
        lu.add(gd, 6, 0, 6, 0);
        lu.add(new ColorDrawable(Color.TRANSPARENT));
        return lu.get();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibleChangeListener != null) {
            visibleChangeListener.onIndexBarVisiblityChanged(visibility);
        }
    }

    public interface IndexBarVisibleChangeListener {
        void onIndexBarVisiblityChanged(int visiblity);
    }

    private IndexBarVisibleChangeListener visibleChangeListener;

    public void setVisibleChangeListener(IndexBarVisibleChangeListener listener) {
        this.visibleChangeListener = listener;
    }

    public void postHide() {
        TaskUtil.fore(new Runnable() {
            @Override
            public void run() {
                setVisibility(View.GONE);
            }
        });
    }

    public void postShow() {
        TaskUtil.fore(new Runnable() {
            @Override
            public void run() {
                setVisibility(View.VISIBLE);
            }
        });
    }
}

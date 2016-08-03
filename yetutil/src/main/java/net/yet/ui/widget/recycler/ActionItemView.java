package net.yet.ui.widget.recycler;

import android.view.View;

/**
 * Created by yet on 2015/10/28.
 */
public interface ActionItemView {

    int getActionCount();

    View getActionView(int index);

}
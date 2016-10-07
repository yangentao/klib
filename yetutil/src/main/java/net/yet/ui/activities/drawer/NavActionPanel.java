package net.yet.ui.activities.drawer;

import android.content.Context;
import android.widget.LinearLayout;

import net.yet.ui.util.XView;

/**
 * Created by entaoyang@163.com on 16/6/27.
 */
public class NavActionPanel extends LinearLayout {
	public NavActionPanel(Context context) {
		super(context);
		XView.view(this).orientationVertical();
	}
}

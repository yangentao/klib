package net.yet.ui.util;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableListUtil {
	StateListDrawable sl = new StateListDrawable();

	private int count = 0;

	//如果d是null, 不执行任何动作, 忽略掉
	public void addDrawable(Drawable d, int... states) {
		if (d != null) {
			sl.addState(states, d);
			++count;
		}
	}

	public void addColor(Integer color, int... states) {
		if (color != null) {
			sl.addState(states, new ColorDrawable(color));
			++count;
		}
	}

	public int size() {
		return count;
	}

	public StateListDrawable get() {
		return sl;
	}
}

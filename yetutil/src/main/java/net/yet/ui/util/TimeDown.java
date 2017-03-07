package net.yet.ui.util;

import android.widget.TextView;

import net.yet.R;
import net.yet.util.RepeatCallback;
import net.yet.util.TaskUtil;
import net.yet.ui.res.Res;
import net.yet.util.log.xlog;

import java.util.HashMap;

/**
 * 用于验证码的倒计时
 * Created by yangentao on 2016-02-03.
 * entaoyang@163.com
 */
public class TimeDown {

	private static HashMap<String, TextView> map = new HashMap<>();
	private static HashMap<String, Integer> secondsMap = new HashMap<>();

	public static void updateView(String name, TextView view) {
		map.put(name, view);
		if (secondsMap.containsKey(name)) {
			view.setEnabled(false);
		} else {
			view.setEnabled(true);
		}
	}

	//要在主线程调用
	public static void startClick(final String name, final int secondsLimit, TextView view) {
		xlog.INSTANCE.d("startClick ", name, secondsLimit);
		map.put(name, view);
		view.setEnabled(false);
		if (!secondsMap.containsKey(name)) {
			secondsMap.put(name, secondsLimit);
			xlog.INSTANCE.d("倒计时开始", name, secondsLimit);
			TaskUtil.repeatFore(secondsLimit, 1000, new RepeatCallback() {

				@Override
				public boolean onRepeat(int index, long value) {
					int leftTimes = secondsLimit - index;
					secondsMap.put(name, leftTimes);

					TextView v = map.get(name);
					if (v != null) {
						String s = "" + leftTimes;
						s += Res.str(R.string.yet_retrive_again);
						v.setText(s);
					}
					return true;
				}

				public void onRepeatEnd() {
					secondsMap.remove(name);
					TextView v = map.get(name);
					if (v != null) {
						v.setEnabled(true);
						v.setText(Res.str(R.string.yet_retrive));
					}
				}
			});
		} else {
			xlog.INSTANCE.d("重复的点击");
		}
	}
}

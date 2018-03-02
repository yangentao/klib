package yet.ui.util;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;

public class DoubleBackKeyCheck {
	private long backPressTime = 0;

	// 如果2秒内连续按两次返回键, 返回true, 其他返回false
	public boolean keyDownCheck(Activity context, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return backPressed(context);
		}
		return false;
	}

	public boolean backPressed(Activity context) {
		long current = System.currentTimeMillis();
		if (current - backPressTime > 2000) {
			backPressTime = current;
			Toast.makeText(context, "再次按Back键退出程序", Toast.LENGTH_SHORT).show();
		} else {
			return true;
		}
		return false;
	}
}

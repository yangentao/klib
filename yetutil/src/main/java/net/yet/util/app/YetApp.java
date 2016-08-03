package net.yet.util.app;

import android.app.Application;

import net.yet.R;
import net.yet.ui.activities.AppVisibleListener;
import net.yet.util.xlog;

/**
 * Created by yet on 2015/10/10.
 */
public class YetApp extends Application implements AppVisibleListener {

	@Override
	public void onCreate() {
		this.setTheme(R.style.LibTheme);
		super.onCreate();
		App.init(this);
	}

	@Override
	public void onEnterForeground() {
		xlog.d("onEnterForeground");
	}

	@Override
	public void onEnterBackground() {
		xlog.d("onEnterBackground");
	}
}

package net.yet.util.app

import android.app.Application
import net.yet.theme.TextSize
import net.yet.ui.activities.AppVisibleListener

/**
 * Created by yet on 2015/10/10.
 */
open class YetApp : Application(), AppVisibleListener {

	override fun onCreate() {
		this.setTheme(net.yet.R.style.yetTheme)
		super.onCreate()
		App.init(this)
		if (OS.HUAWEI) {
			TextSize.addTextSize(-1)
		}
	}

	override fun onEnterForeground() {
	}

	override fun onEnterBackground() {
	}
}

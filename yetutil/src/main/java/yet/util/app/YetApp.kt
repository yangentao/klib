package yet.util.app

import android.app.Application
import android.os.Build
import android.os.StrictMode
import yet.theme.TextSize
import yet.ui.activities.AppVisibleListener

/**
 * Created by yet on 2015/10/10.
 */
open class YetApp : Application(), AppVisibleListener {

	override fun onCreate() {
		this.setTheme(net.yet.R.style.yetTheme)
		super.onCreate()
		val builder = StrictMode.VmPolicy.Builder()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			builder.detectFileUriExposure().penaltyLog()
		}
		StrictMode.setVmPolicy(builder.build())
		App.setInstance(this)
		if (OS.HUAWEI) {
			TextSize.addTextSize(-1)
		}
	}

	override fun onEnterForeground() {
	}

	override fun onEnterBackground() {
	}
}

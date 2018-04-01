package yet.util.app

import android.app.Application
import yet.ui.activities.AppVisibleListener

/**
 * Created by yet on 2015/10/10.
 */
open class YetApp : Application(), AppVisibleListener {

	override fun onCreate() {
		this.setTheme(net.yet.R.style.yetTheme)
		super.onCreate()
		App.setInstance(this)
	}

	override fun onEnterForeground() {
	}

	override fun onEnterBackground() {
	}
}

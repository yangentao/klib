package yet.util

import android.app.*
import android.content.*
import android.net.Uri
import android.os.Bundle
import yet.util.app.App

object AlarmUtil {

	fun serviceAt(time: Long, wakeup: Boolean, action: String, cls: Class<out Service>, uri: Uri? = null, bundle: Bundle? = null) {
		val it = Intent(action, uri, App.app, cls)
		if (bundle != null) {
			it.putExtras(bundle)
		}
		val pi = PendingIntent.getService(App.app, 0, it, PendingIntent.FLAG_ONE_SHOT)
		val am = App.app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		am.set(if (wakeup) AlarmManager.RTC_WAKEUP else AlarmManager.RTC, time, pi)
	}

	fun broadcastAt(time: Long, wakeup: Boolean, action: String, cls: Class<out BroadcastReceiver>, uri: Uri? = null, bundle: Bundle? = null) {
		val it = Intent(action, uri, App.app, cls)
		if (bundle != null) {
			it.putExtras(bundle)
		}
		val pi = PendingIntent.getBroadcast(App.app, 0, it, PendingIntent.FLAG_ONE_SHOT)
		val am = App.app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		am.set(if (wakeup) AlarmManager.RTC_WAKEUP else AlarmManager.RTC, time, pi)
	}
}

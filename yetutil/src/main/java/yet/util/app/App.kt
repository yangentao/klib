package yet.util.app

import android.app.ActivityManager
import android.app.Application
import android.app.DownloadManager
import android.app.Service
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.text.TextUtils
import yet.theme.TextSize
import yet.ui.activities.AnimConf
import yet.ui.ext.displayMetrics
import yet.util.MsgCenter
import yet.util.StrBuilder
import yet.util.Util
import yet.util.database.Values
import yet.util.log.logd
import yet.util.log.loge
import yet.util.log.xlog
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.ref.WeakReference

/**
 * Created by entaoyang@163.com on 2017-03-31.
 */

object App {
	val NETWORK_CONNECTED = "network.connected"
	private lateinit var inst: WeakReference<Application>

	val app: Application get() = inst.get()!!

	val debug: Boolean by lazy { 0 != (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) }
	val ic_launcher: Int by lazy { app.applicationInfo.icon }
	val density: Float by lazy { app.resources.displayMetrics.density }
	val scaledDensity: Float by lazy { app.resources.displayMetrics.scaledDensity }
	val app_name: String by lazy { app.applicationInfo.loadLabel(app.packageManager).toString() }
	val packageName: String by lazy { app.packageName }
	val versionCode: Int by lazy { app.packageManager.getPackageInfo(app.packageName, 0).versionCode }
	val versionName: String by lazy { app.packageManager.getPackageInfo(app.packageName, 0).versionName }
	val sdkVersion: Int by lazy { Build.VERSION.SDK_INT }
	val modelBuild: String by lazy { Build.MODEL }

	val resolver: ContentResolver by lazy { app.contentResolver }
	val contentResolver: ContentResolver  by lazy { app.contentResolver }
	val resource: Resources by lazy { app.resources }

	var animConfDefault = AnimConf()

	val screenWidthPx: Int by lazy {
		app.displayMetrics.widthPixels
	}
	val screenHeightPx: Int by lazy {
		app.displayMetrics.heightPixels
	}

	fun setInstance(inst: Application) {
		this.inst = WeakReference(inst)
		val builder = StrictMode.VmPolicy.Builder()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			builder.detectFileUriExposure().penaltyLog()
		}
		StrictMode.setVmPolicy(builder.build())
		if (OS.HUAWEI) {
			TextSize.addTextSize(-1)
		}

		Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
			val sb = StrBuilder(128)
			sb.append("uncaughtException:", thread.id, " ", thread.name)
			ex.printStackTrace()
			loge(ex)
			xlog.flush()
			System.exit(-1)
		}


		val connectivityReceiver = object : BroadcastReceiver() {

			override fun onReceive(context: Context, intent: Intent) {
				if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
					MsgCenter.fire(ConnectivityManager::class)
				}
				if (isNetworkConnected) {
					MsgCenter.fire(NETWORK_CONNECTED)
				}

			}
		}
		app.registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


	}

	fun openOrCreateDatabase(name: String): SQLiteDatabase {
		return app.openOrCreateDatabase(name, 0, null)
	}

	val sdAppRoot: File get() {
		return app.getExternalFilesDir(null)
	}


	fun sp2px(spValue: Float): Int {
		return (spValue * scaledDensity + 0.5f).toInt()
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	fun dp2px(dpValue: Float): Int {
		if (dpValue > 0) {
			return (dpValue * density + 0.5f).toInt()
		} else {
			return dpValue.toInt()
		}
	}

	fun dp2px(dpValue: Int): Int {
		if (dpValue > 0) {
			return (dpValue * density + 0.5f).toInt()
		} else {
			return dpValue
		}
	}

	fun isForeground(): Boolean {
		val am: ActivityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return false
		val cn = am.getRunningTasks(1)[0].topActivity
		val currentPackageName = cn.packageName
		return !TextUtils.isEmpty(currentPackageName) && currentPackageName == packageName
	}

	fun isAppShowing(): Boolean {
		return isForeground() && !Util.getKeyguardManager().inKeyguardRestrictedInputMode()
	}

	fun isAppTop(): Boolean {
		if (Util.getKeyguardManager().inKeyguardRestrictedInputMode()) {
			return false
		}
		val am: ActivityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return false
		for (info in am.runningAppProcesses) {

			if (info.processName == packageName) {
				logd(info.processName, info.importance)
				return info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
			}
		}
		return false
	}

	fun startService(cls: Class<out Service>, values: Values?) {
		val it = Intent(app, cls)
		it.`package` = packageName
		if (values != null) {
			it.putExtras(values.bundle(false))
		}
		app.startService(it)

	}

	/**
	 * App内广播

	 * @param action
	 * *
	 * @param values
	 */
	fun broadcastApp(action: String, values: Values?) {
		val it = Intent(action)
		it.`package` = packageName
		if (values != null) {
			it.putExtras(values.bundle(false))
		}
		try {
			app.sendOrderedBroadcast(it, null)
		} catch (e: Exception) {
			loge(e)
		}

	}


	@Throws(FileNotFoundException::class)
	fun openStream(uri: Uri): InputStream = app.contentResolver.openInputStream(uri)


	// 获取ApiKey
	fun getMetaValue(context: Context, metaKey: String): String? {
		try {
			val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
			return ai?.metaData?.getString(metaKey)
		} catch (e: PackageManager.NameNotFoundException) {
			e.printStackTrace()
			loge(e)
		}

		return null
	}

	fun metaValue(metaKey: String): String? {
		return getMetaValue(app, metaKey)
	}

	val imei: String? get() {
		try {
			val tm = app.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
			return tm?.deviceId
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
		return null
	}

	// 单位兆M
	val memLimit: Int by lazy {
		(app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass
	}

	val isNetworkConnected: Boolean get() {
		return (app.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected ?: false
	}

	val downloadManager: DownloadManager get() {
		return app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
	}

}

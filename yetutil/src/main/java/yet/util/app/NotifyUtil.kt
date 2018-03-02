package yet.util.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.util.SparseArray
import yet.util.BmpUtil
import yet.util.TaskUtil
import yet.util.Util
import yet.util.database.Values

/**
 * 图标默认是应用程序图标
 * 标题默认是应用程序名称
 * 默认自动取消

 * @author yangentao@gmail.com
 */
class NotifyUtil(val id: Int) {
	interface NotifyCallback {
		fun onNotifyClick(id: Int, intent: Intent)
	}

	private var defaults = 0

	var builder = NotificationCompat.Builder(App.app)

	init {
		autoCancel(true)
		title(App.app_name)
		iconSmall(if (smallIcon == 0) App.ic_launcher else smallIcon)
		iconLarge(if (largeIcon == 0) App.ic_launcher else largeIcon)
	}

	fun cancel() {
		val nm = Util.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.cancel(id)
	}

	/**
	 * 小图标, 状态栏显示, 如果没有设置大图标,这个小图标也会在下拉后的信息栏左侧显示

	 * @param resId
	 * *
	 * @return
	 */
	fun iconSmall(resId: Int): NotifyUtil {
		builder.setSmallIcon(resId)
		return this
	}

	fun iconLarge(icon: Int): NotifyUtil {
		return iconLarge(BmpUtil.fromRes(icon))
	}

	/**
	 * 在下拉后的信息栏左侧显示, 可以不设置

	 * @return
	 */
	fun iconLarge(icon: Bitmap?): NotifyUtil {
		if(icon != null) {
			builder.setLargeIcon(icon)
		}
		return this
	}

	/**
	 * 第一行大点的标题

	 * @param title
	 * *
	 * @return
	 */
	fun title(title: String): NotifyUtil {
		builder.setContentTitle(title)
		return this
	}

	/**
	 * 第二行文本

	 * @param msg
	 * *
	 * @return
	 */
	fun msg(msg: String): NotifyUtil {
		builder.setContentText(msg)
		return this
	}

	fun msgAndTicker(msg: String): NotifyUtil {
		msg(msg)
		return ticker(msg)
	}

	/**
	 * 状态栏滚动文本

	 * @param ticker
	 * *
	 * @return
	 */
	fun ticker(ticker: String): NotifyUtil {
		builder.setTicker(ticker)
		return this
	}

	/**
	 * info在通知的时间下面, 小字, 用于状态等

	 * @param info
	 * *
	 * @return
	 */
	fun info(info: String): NotifyUtil {
		builder.setContentInfo(info)
		return this
	}

	/**
	 * 点击时打开Activity

	 * @param cls
	 * *
	 * @param values
	 * *
	 * @return
	 */
	@JvmOverloads fun clickActivity(cls: Class<out Activity>, values: Values? = null): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.setContentIntent(IntentUtil.pendingActivity(cls, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 点击时发出广播

	 * @param action
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun clickBroadcast(action: String, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.setContentIntent(IntentUtil.pendingBroadcast(action, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 点击时启动Service

	 * @param cls
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun clickService(cls: Class<out Service>, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.setContentIntent(IntentUtil.pendingService(cls, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 清除时启动Activity

	 * @param cls
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun deleteActivity(cls: Class<out Activity>, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.setDeleteIntent(IntentUtil.pendingActivity(cls, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 清除时发广播

	 * @param action
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun deleteBroadcast(action: String, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.setDeleteIntent(IntentUtil.pendingBroadcast(action, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 清除时启动Service

	 * @param cls
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun deleteService(cls: Class<out Service>, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.setDeleteIntent(IntentUtil.pendingService(cls, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 点击按钮时 打开Activity

	 * @param icon
	 * *
	 * @param title
	 * *
	 * @param cls
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun actionActivity(icon: Int, title: String, cls: Class<out Activity>, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.addAction(icon, title, IntentUtil.pendingActivity(cls, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 点击按钮时发广播

	 * @param icon
	 * *
	 * @param title
	 * *
	 * @param action
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun actionBroadcast(icon: Int, title: String, action: String, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.addAction(icon, title, IntentUtil.pendingBroadcast(action, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 点击按钮时启动Service

	 * @param icon
	 * *
	 * @param title
	 * *
	 * @param cls
	 * *
	 * @param values
	 * *
	 * @return
	 */
	fun actionService(icon: Int, title: String, cls: Class<out Service>, values: Values?): NotifyUtil {
		var vs = values
		if (vs == null) {
			vs = Values(ID_KEY to  id)
		} else {
			vs.put(ID_KEY, id)
		}
		builder.addAction(icon, title, IntentUtil.pendingService(cls, PendingIntent.FLAG_UPDATE_CURRENT, vs))
		return this
	}

	/**
	 * 默认的震动

	 * @return
	 */
	fun vib(): NotifyUtil {
		this.defaults = this.defaults or Notification.DEFAULT_VIBRATE
		return this
	}

	/**
	 * 默认的声音

	 * @return
	 */
	fun sound(): NotifyUtil {
		this.defaults = this.defaults or Notification.DEFAULT_SOUND
		return this
	}

	/**
	 * 是否自动清除

	 * @param autoCancel
	 * *
	 * @return
	 */
	fun autoCancel(autoCancel: Boolean): NotifyUtil {
		builder.setAutoCancel(autoCancel)
		return this
	}

	/**
	 * 显示数字, 跟info在同一个位置, 互斥

	 * @param num
	 * *
	 * @return
	 */
	fun num(num: Int): NotifyUtil {
		builder.setNumber(num)
		return this
	}

	/**
	 * 持续的通知

	 * @param ongoing
	 * *
	 * @return
	 */
	fun ongoing(ongoing: Boolean): NotifyUtil {
		builder.setOngoing(ongoing)
		return this
	}

	@JvmOverloads fun progress(max: Int, progress: Int, indeterminate: Boolean = false): NotifyUtil {
		builder.setProgress(max, progress, indeterminate)
		return this
	}

	fun `when`(`when`: Long): NotifyUtil {
		builder.setWhen(`when`)
		return this
	}

	fun show() {
		if (defaults != 0) {
			builder.setDefaults(defaults)
		}
		val n = builder.build()
		val nm = Util.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.notify(id, n)
	}

	fun callback(callback: NotifyCallback): NotifyUtil {
		registerCallback(id, callback)
		return this
	}

	companion object {

		private val callbackMap = SparseArray<NotifyCallback>()

		private const val ID_KEY = "notify.id"

		private var smallIcon = 0
		private var largeIcon = 0

		@JvmStatic fun registerCallback(id: Int, callback: NotifyCallback) {
			callbackMap.put(id, callback)
		}

		@JvmStatic fun removeCallback(id: Int) {
			callbackMap.remove(id)
		}

		@JvmStatic fun setDefaultIconSmall(res: Int) {
			smallIcon = res
		}

		@JvmStatic fun setDefaultIconLarge(res: Int) {
			largeIcon = res
		}

		@JvmStatic fun processNotifyIntent(intent: Intent): Boolean {
			val id = getId(intent)
			if (id > 0) {
				val c = callbackMap.get(id)
				if (c != null) {
					TaskUtil.fore(Runnable { c.onNotifyClick(id, intent) })
					return true
				}
			}
			return false
		}

		/**
		 * 获取通知ID

		 * @return 没有返回0
		 */
		@JvmStatic fun getId(intent: Intent): Int {
			return intent.getIntExtra(ID_KEY, 0)
		}

		@JvmStatic fun id(id: Int): NotifyUtil {
			return NotifyUtil(id)
		}

		@JvmStatic fun cancel(id: Int) {
			val nm = Util.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			nm.cancel(id)
		}
	}
}
/**
 * 显示进度条

 * @param max
 * *
 * @param progress
 * *
 * @return
 */

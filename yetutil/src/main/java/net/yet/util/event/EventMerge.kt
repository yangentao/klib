package net.yet.util.event

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import net.yet.util.*
import net.yet.util.app.App
import java.util.*

/**
 * 监听到Uri改变或MsgCenter发出的特定消息时, 激发另一个新消息或前后台回调
 * task总是先与msg被执行

 * @author yangentao@gmail.com
 */
class EventMerge {
	// 要激发的新消息
	private val msgFireSet = HashSet<String>()
	private var backRun: Runnable? = null
	private var foreRun: Runnable? = null
	private var backFore: BackFore? = null

	fun clear() {
		msgFireSet.clear()
		backRun = null
		foreRun = null
		backFore = null
		App.getContentResolver().unregisterContentObserver(observer)
		MsgCenter.remove(msgListener)
	}

	private val invoker = object : BackFore {

		override fun onFore() {
			if (backFore != null) {
				backFore!!.onFore()
			}
			if (foreRun != null) {
				TaskUtil.fore(foreRun)
			}
			for (s in msgFireSet) {
				MsgCenter.fire(s)
			}
		}

		override fun onBack() {
			if (backRun != null) {
				backRun!!.run()
			}
			if (backFore != null) {
				backFore!!.onBack()
			}
		}
	}

	private val amCallback = Runnable { TaskUtil.backFore(invoker) }

	private val am = ActionMerger(0, amCallback)

	private val msgListener = object : MsgListener {

		override fun onMsg(msg: Msg) {
			am.trigger()

		}
	}

	private val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
		override fun onChange(selfChange: Boolean) {
			am.trigger()
		}
	}

	/**
	 * 监听Uri改变

	 * @param uri
	 * *
	 * @return
	 */
	fun watch(vararg uris: Uri): EventMerge {
		for (uri in uris) {
			App.getContentResolver().registerContentObserver(uri, true, observer)
		}
		return this
	}

	/**
	 * 监听消息

	 * @param cls
	 * *
	 * @return
	 */
	fun listen(vararg clses: Class<*>): EventMerge {
		MsgCenter.listen(msgListener, *clses)
		return this
	}

	/**
	 * 监听消息

	 * @param msg
	 * *
	 * @return
	 */
	fun listen(msgs: String): EventMerge {
		MsgCenter.listen(msgListener, msgs)
		return this
	}

	/**
	 * 监听到消息或Uri时, 激发这个新消息

	 * @param callback
	 * *
	 * @return
	 */
	fun fire(newMsg: String): EventMerge {
		msgFireSet.add(newMsg)
		return this
	}

	/**
	 * 监听到消息或Uri时, 激发这个新消息

	 * @param callback
	 * *
	 * @return
	 */
	fun fire(newCls: Class<*>): EventMerge {
		msgFireSet.add(newCls.name)
		return this
	}

	fun taskBack(backRun: Runnable): EventMerge {
		this.backRun = backRun
		return this
	}

	fun taskFore(foreRun: Runnable): EventMerge {
		this.foreRun = foreRun
		return this
	}

	fun task(backFore: BackFore): EventMerge {
		this.backFore = backFore
		return this
	}

	fun task(foreRun: Runnable): EventMerge {
		return taskFore(foreRun)
	}

	companion object {

		/**
		 * 回调或激发新消息时 的合并延时
		 * 比如在10秒中内, 收到短信Uri改变时, 无论多少次, 都只激发一次新消息(或回调)

		 * @param millSeconds
		 * *            <=0 立即回调; >0 延时合并回调
		 * *
		 * @return
		 */
		@JvmOverloads fun delay(millSeconds: Int = 100): EventMerge {
			val inst = EventMerge()
			inst.am.setDelay(millSeconds)
			return inst
		}
	}

}
/**
 * 默认延时100毫秒

 * @return
 */

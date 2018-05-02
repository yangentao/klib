@file:JvmMultifileClass
@file:JvmName("KUtil")

package yet.util

import android.os.Handler
import android.os.Looper
import yet.database.MapTable
import yet.util.app.App
import yet.util.log.xlog
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by yet on 2015-11-20.
 */


private fun uncaughtException(thread: Thread, ex: Throwable): Unit {
	ex.printStackTrace()
	xlog.e(ex)
	xlog.flush()
	System.exit(-1)
}

private object ContextHelper {
	val handler = Handler(Looper.getMainLooper())
	val mainThread: Thread = Looper.getMainLooper().thread
	val es: ExecutorService = Executors.newCachedThreadPool { r ->
		val t = Thread(r)
		t.isDaemon = false
		t.priority = Thread.NORM_PRIORITY - 1
		t.setUncaughtExceptionHandler(::uncaughtException)
		t
	}
}

fun mainThread(block: () -> Unit) {
	if (InMainThread) {
		block()
	} else {
		fore(block)
	}
}

fun fore(callback: () -> Unit) {
	ContextHelper.handler.post(callback)
}

fun foreDelay(delay: Long, callback: () -> Unit) {
	ContextHelper.handler.postDelayed(callback, delay)
}

fun back(callback: () -> Unit) {
	ContextHelper.es.submit(callback)
}

fun backDelay(delay: Long, callback: () -> Unit) {
	foreDelay(delay) {
		back(callback)
	}
}


val InMainThread: Boolean get() = Thread.currentThread().id == Looper.getMainLooper().thread.id

fun debugMustInMainThread(msg: String = "必须在主线程调用") {
	if (App.debug) {
		if (!InMainThread) {
			debugThrow(msg)
		}
	}
}

inline fun <R> sync(lock: Any, block: () -> R): R {
	return synchronized(lock, block)
}

private val onceSet = HashSet<String>()

fun resetRunOnce(key: String) {
	onceSet.remove(key)
}

//进程内只执行一次
fun runOnce(key: String, block: () -> Unit) {
	sync(onceSet) {
		if (key in onceSet) {
			return
		}
		onceSet.add(key)
	}
	block.invoke()
}

fun backOnce(key: String, block: () -> Unit) {
	back {
		runOnce(key, block)
	}
}

fun foreOnce(key: String, block: () -> Unit) {
	fore {
		runOnce(key, block)
	}
}

//每个版本只运行一次
fun runOnceVer(key: String, block: () -> Unit): Boolean {
	val fname = "once_${App.versionCode}"
	val p = Prefer(fname)
	if (p.getBool(key, false)) {
		p.edit {
			putBoolean(key, true)
		}
		block()
		return true
	}
	return false

}

fun isVersionFirst(key: String): Boolean {
	val newKey = "${key}_${App.versionCode}"
	val v = MapTable.config.getBool(newKey) ?: false
	if (!v) {
		MapTable.config.put(newKey, true)
		return true
	}
	return false
}

//app的当前版本只执行一次

//inline fun verOnce(key: String, block: () -> Unit) {
//
//}
//
//app安装后, 只执行一次
//inline fun appOnce(key: String, block: () -> Unit) {
//
//}




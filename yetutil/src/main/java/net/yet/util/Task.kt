@file:JvmMultifileClass
@file:JvmName("KUtil")

package net.yet.util

import android.os.Handler
import android.os.Looper
import net.yet.util.app.App
import java.util.*
import java.util.concurrent.Executors

/**
 * Created by yet on 2015-11-20.
 */


private fun uncaughtException(thread: Thread, ex: Throwable): Unit {
	xlog.e(ex)
	xlog.flush()
	System.exit(-1)
}

private object ContextHelper {
	val handler = Handler(Looper.getMainLooper())
	val mainThread = Looper.getMainLooper().thread
	val es = Executors.newCachedThreadPool { r ->
		val t = Thread(r)
		t.isDaemon = false
		t.priority = Thread.NORM_PRIORITY - 1
		t.setUncaughtExceptionHandler(::uncaughtException)
		t
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

fun inMainThread(): Boolean {
	return Thread.currentThread() == ContextHelper.mainThread
}

fun debugMustInMainThread(msg:String = "必须在主线程调用") {
	if(App.debug) {
		if(!inMainThread()) {
			debugThrow(msg)
		}
	}
}

inline fun <R> sync(lock: Any, block: () -> R): R {
	return synchronized(lock, block)
}

private val onceSet = HashSet<String>()
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

//app的当前版本只执行一次

//inline fun verOnce(key: String, block: () -> Unit) {
//
//}
//
//app安装后, 只执行一次
//inline fun appOnce(key: String, block: () -> Unit) {
//
//}




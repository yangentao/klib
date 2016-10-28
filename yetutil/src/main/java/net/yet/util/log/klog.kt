package net.yet.util.log

import net.yet.util.log.xlog

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */


fun logd(vararg args: Any?) {
	xlog.d(*args)
}

fun loge(vararg args: Any?) {
	xlog.e(*args)
}

fun log(vararg args: Any?) {
	xlog.d(*args)
}
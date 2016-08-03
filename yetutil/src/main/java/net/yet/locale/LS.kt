package net.yet.locale

import net.yet.util.app.App

/**
 * Created by entaoyang@163.com on 16/5/26.
 */

fun LibS(resId: Int): String {
	return App.resString(resId)
}

fun LibS(resId: Int, arg: String): String {
	val s = App.resString(resId)
	return String.format(s, arg)
}


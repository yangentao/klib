package net.yet.ui.res

import android.support.annotation.StringRes
import net.yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */

fun ResStr(@StringRes redId:Int):String {
	return App.get().getString(redId)
}
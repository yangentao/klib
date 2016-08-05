package net.yet.ui.activities

import android.content.Context
import net.yet.ui.page.BaseFragment
import net.yet.util.database.Values
import net.yet.util.xlog

/**
 * Created by entaoyang@163.com on 2016-07-26.
 */

object PageUtil {
	@JvmStatic fun open(context: Context, fragment: BaseFragment) {
		OpenActivity.openFragment(context, fragment)
	}

	@JvmStatic fun open(context: Context, cls: Class<out BaseFragment>) {
		open(context, cls, null)
	}

	@JvmStatic fun open(context: Context, cls: Class<out BaseFragment>, args: Values?) {
		try {
			val f = cls.newInstance()
			f.args.addAll(args)
			open(context, f)
		} catch (e: InstantiationException) {
			e.printStackTrace()
			xlog.fatal(e)
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
			xlog.fatal(e)
		}

	}

}


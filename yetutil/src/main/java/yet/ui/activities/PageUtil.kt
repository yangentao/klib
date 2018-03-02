package yet.ui.activities

import android.content.Context
import yet.ui.page.BaseFragment
import yet.util.Msg
import yet.util.database.Values
import yet.util.log.xlog
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2016-07-26.
 */

object PageUtil {
	val MSG_CLOSE_PAGE  = "pages.close"

	fun closePage(vararg classes: KClass<*>){
		for(c in classes) {
			Msg(MSG_CLOSE_PAGE).clazz(c).fire()
		}
	}
	 fun open(context: Context, fragment: BaseFragment) {
		OpenActivity.openFragment(context, fragment)
	}

	 fun open(context: Context, cls: Class<out BaseFragment>) {
		open(context, cls, null)
	}

	 fun open(context: Context, cls: Class<out BaseFragment>, args: Values?) {
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


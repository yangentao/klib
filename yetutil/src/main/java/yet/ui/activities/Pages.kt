package yet.ui.activities

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import yet.ext.hasBits
import yet.ext.extraInt
import yet.ext.removeBits
import yet.ui.page.BaseFragment
import yet.util.InMainThread
import yet.util.Msg
import yet.util.log.xlog
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2016-07-26.
 */

object Pages {
	private val KEY = "page.temp.key"
	private var order: Int = 1
	private val pageList = ArrayList<Pair<Int, WeakReference<BaseFragment>>>()

	private val lastPageClass: KClass<*>?
		get() {
			pageList.removeAll { it.second.get() == null }
			val p = pageList.lastOrNull() ?: return null
			val pc = p.second.get() ?: return null
			return pc::class
		}

	fun onCreate(act: BaseActivity): BaseFragment? {
		val key: Int = act.intent.extraInt(KEY)
		val p = pageList.find { it.first == key } ?: return null
		return p.second.get()
	}

	fun onDestroy(page: BaseFragment) {
		pageList.removeAll { it.second.get() == page || it.second.get() == null }
	}

	fun open(context: Context, fragment: BaseFragment, block:Intent.()->Unit = {}) {
		assert(InMainThread)
		var n = fragment.openFlag
		if (n.hasBits(Intent.FLAG_ACTIVITY_SINGLE_TOP)) {
			if (lastPageClass == fragment::class) {
				return
			}
			n = n.removeBits(Intent.FLAG_ACTIVITY_SINGLE_TOP)
		}

		val intent = Intent(context, PageActivity::class.java)
		val wColor: Int? = fragment.windowBackColor
		if (wColor != null) {
			val alpha: Int = Color.alpha(wColor)
			if (alpha != 0xff) {
				intent.component = ComponentName(context, TransPageActivity::class.java)
			}
		}

		if (n != 0) {
			intent.flags = n
		}
		var key = order++
		pageList.add(Pair(key, WeakReference(fragment)) )
		intent.putExtra(KEY, key)
		intent.block()
		context.startActivity(intent)
		val ac = fragment.activityAnim
		if (ac != null && context is Activity) {
			context.overridePendingTransition(ac.startEnter, ac.startExit)
		}
	}

	fun open(context: Context, cls: KClass<out BaseFragment>, block:Intent.()->Unit = {}) {
		try {
			val f = cls.java.newInstance()
			Pages.open(context, f, block)
		} catch (e: InstantiationException) {
			e.printStackTrace()
			xlog.fatal(e)
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
			xlog.fatal(e)
		}

	}


	val MSG_CLOSE_PAGE  = "pages.close"

	fun closePage(vararg classes: KClass<*>){
		for(c in classes) {
			Msg(MSG_CLOSE_PAGE).clazz(c).fire()
		}
	}
}




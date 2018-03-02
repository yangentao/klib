package yet.ui.activities

import android.app.Activity
import android.content.*
import android.graphics.Color
import yet.ext.hasBits
import yet.ext.removeBits
import yet.ui.page.BaseFragment
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-07-26.
 */

object OpenActivity {
	private val KEY = "page.temp.key"
	private var map = Hashtable<Long, BaseFragment>()
	var lastPageClass: Class<out Any>? = null

	fun pop(it: Intent): BaseFragment? {
		val key: Long = it.getLongExtra(KEY, 0L)
		return map.remove(key)
	}

	fun openFragment(context: Context, fragment: BaseFragment) {
		var n = fragment.openFlag
		if (n.hasBits(Intent.FLAG_ACTIVITY_SINGLE_TOP)) {
			if (lastPageClass == fragment.javaClass) {
				return
			}
			n = n.removeBits(Intent.FLAG_ACTIVITY_SINGLE_TOP)
		}

		val intent = Intent(context, PageActivity::class.java)
		val wColor:Int? = fragment.windowBackColor
		if (wColor != null) {
			val alpha:Int = Color.alpha(wColor)
			if(alpha != 0xff) {
				intent.component = ComponentName(context, TransPageActivity::class.java)
			}
		}

		if (n != 0) {
			intent.flags = n
		}
		var key = System.currentTimeMillis()
		map.put(key, fragment)
		intent.putExtra(KEY, key)
		context.startActivity(intent)
		val ac = fragment.activityAnim
		if (ac != null && context is Activity) {
			context.overridePendingTransition(ac.startEnter, ac.startExit)
		}
	}
}


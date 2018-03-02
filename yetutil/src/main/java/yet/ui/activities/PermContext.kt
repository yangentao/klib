package yet.ui.activities

import android.app.Activity
import yet.util.app.Perm
import yet.util.mainThread

/**
 * Created by entaoyang@163.com on 2017-03-24.
 */

interface PermContext {
	fun reqPerm(req: Perm)
	fun getActivityContext(): Activity

	fun needPerm(vararg ps: String, block: (Boolean) -> Unit = {}) {
		val p = Perm(*ps)
		p.onAllowed = block
		mainThread {
			p.request(this)
		}
	}
}
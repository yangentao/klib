package yet.util.app

import android.app.Activity
import android.app.Fragment
import android.os.Build
import yet.util.fore
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by entaoyang@163.com on 2016-11-17.
 */

open class Perm(val perms: HashSet<String>) {

	var onResult: (Map<String, Boolean>) -> Unit = {}

	private val resultMap = HashMap<String, Boolean>()

	init {
		perms.forEach {
			resultMap[it] = App.app.hasPerm(it)
		}
	}

	fun req(act: Activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val set = resultMap.filter { !it.value }.keys
			if (set.isEmpty()) {
				callback()
			} else {
				permStack.add(this)
				act.requestPermissions(set.toTypedArray(), PERM_CODE)
			}
		} else {
			callback()
		}
	}

	fun req(f: Fragment) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			val set = resultMap.filter { !it.value }.keys
			if (set.isEmpty()) {
				callback()
			} else {
				permStack.add(this)
				f.requestPermissions(set.toTypedArray(), PERM_CODE)
			}
		} else {
			callback()
		}
	}

	private fun callback() {
		perms.forEach {
			resultMap[it] = App.app.hasPerm(it)
		}
		fore {
			onResult(resultMap)
		}
	}

	companion object {
		val PERM_CODE = 79
		var permStack: LinkedList<Perm> = LinkedList()

		fun onPermResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
			if (requestCode == PERM_CODE) {
				permStack.pollFirst()?.callback()
			}
		}
	}
}

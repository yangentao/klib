package yet.util.app

import android.content.pm.PackageManager
import android.os.Build
import yet.ui.activities.PermContext

/**
 * Created by entaoyang@163.com on 2016-11-17.
 */
//Manifest.permission.READ_CALL_LOG

fun hasPerm(p: String, before60: Boolean): Boolean {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		return PackageManager.PERMISSION_GRANTED == App.app.checkSelfPermission(p)
	}
	return before60
}

fun hasPerms(ls: Collection<String>, before60: Boolean): Boolean {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		ls.forEach { s ->
			val b = PackageManager.PERMISSION_GRANTED == App.app.checkSelfPermission(s)
			if (!b) {
				return false
			}
		}
		return true
	}
	return before60
}

open class Perm(vararg p: String) {
	//填上这个数组
	var perms: HashSet<String> = HashSet()
	//这个不要动, 会自动填
	var denyPerms: HashSet<String> = HashSet()
	//所有权限都有的时候会调用这个, 弹框,用户都允许了.
	//参数true:版本<23;  FALSE:版本>=23并且全部权限已经赋给
	var onAllowed: (Boolean) -> Unit = {}
	//只要有没被赋予的权限就会调用这个, 未被赋予的权限在denyPerms里
	var onDeny: () -> Unit = {}

	init {
		perms.addAll(p)
	}

	fun checkSelf() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			denyPerms = perms.filter {
				PackageManager.PERMISSION_GRANTED != App.app.checkSelfPermission(it)
			}.toHashSet()
		}
	}

	fun request(permContext: PermContext){
		permContext.reqPerm(this)
	}
}

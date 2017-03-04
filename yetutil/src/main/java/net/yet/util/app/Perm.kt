package net.yet.util.app

import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by entaoyang@163.com on 2016-11-17.
 */
fun hasPerm(p: String, before60: Boolean): Boolean {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		return PackageManager.PERMISSION_GRANTED == App.get().checkSelfPermission(p)
	}
	return before60
}

fun hasPerms(ls: Collection<String>, before60: Boolean): Boolean {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		ls.forEach { s ->
			val b = PackageManager.PERMISSION_GRANTED == App.get().checkSelfPermission(s)
			if (!b) {
				return false
			}
		}
		return true
	}
	return before60
}
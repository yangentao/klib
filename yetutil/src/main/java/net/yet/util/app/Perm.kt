package net.yet.util.app

import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by entaoyang@163.com on 2016-11-17.
 */
//6.0之前返回false,
fun hasPerm(p: String): Boolean {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		return PackageManager.PERMISSION_GRANTED == App.get().checkSelfPermission(p)
	}
	return false
}

fun hasPerms(ls: Collection<String>): Boolean {
	ls.forEach { s ->
		if (!hasPerm(s)) {
			return false
		}
	}
	return true
}
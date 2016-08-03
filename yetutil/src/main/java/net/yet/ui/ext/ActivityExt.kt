package net.yet.ui.ext

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import net.yet.util.app.OS

/**
 * Created by entaoyang@163.com on 16/5/23.
 */


fun <T : Activity> T.openActivity(cls: Class<out Activity>) {
	this.startActivity(Intent(this, cls))
}

fun <T : Activity> T.hasPermission(p: String): Boolean {
	if (OS.GE60) {
		return this.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED
	}
	return true
}
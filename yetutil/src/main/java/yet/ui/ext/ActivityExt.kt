package yet.ui.ext

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import yet.util.app.OS
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 16/5/23.
 */


fun <T : Fragment> T.openActivity(cls: KClass<out Activity>) {
	activity.startActivity(Intent(activity, cls.java))
}

fun <T : Activity> T.openActivity(cls: KClass<out Activity>) {
	this.startActivity(Intent(this, cls.java))
}


fun <T : Activity> T.openActivity(cls: Class<out Activity>) {
	this.startActivity(Intent(this, cls))
}

fun <T : Activity> T.hasPermission(p: String): Boolean {
	if (OS.GE60) {
		return this.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED
	}
	return true
}


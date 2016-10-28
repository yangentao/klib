package net.yet.yetlibdemo

import net.yet.ext.truck
import net.yet.util.app.YetApp
import net.yet.util.log.log

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */

class MyApp : YetApp() {
	override fun onCreate() {
		super.onCreate()

		var s: String = "0123456789A0123456789B0123456789C0123"
		val ls = s.truck(11)
		for (ss in ls) {
			log(ss)
		}


	}
}
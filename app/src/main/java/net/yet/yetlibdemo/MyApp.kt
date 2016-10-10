package net.yet.yetlibdemo

import net.yet.ext.notNull
import net.yet.util.DO
import net.yet.util.app.YetApp
import net.yet.util.log

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */

class MyApp : YetApp() {
	override fun onCreate() {
		super.onCreate()

		var s: String? = null

		s.DO {
			log("DO: ", it)
		}
		s.notNull {
			log("notNull: ", it)
		}
		s.let {
			log("let: ", it)
		}
		s.apply {
			log("apply: ", this)
		}
		s.run {
			log("run: ", this)
		}
	}
}
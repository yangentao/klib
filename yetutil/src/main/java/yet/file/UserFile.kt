package yet.file

import yet.util.app.App
import yet.util.database.DBMap
import java.io.File

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */

object UserFile {
	private val dbmap = DBMap("user_dir_map")

	fun dir(userName: String): File {
		var dirname = dbmap[userName]
		if (dirname == null) {
			dirname = "u" + System.currentTimeMillis()
			dbmap.put(userName, dirname)
		}
		val dir = File(App.app.filesDir, dirname)
		if (!dir.exists()) {
			dir.mkdir()
		}
		return dir
	}

	fun file(userName: String, filename: String): File {
		return File(dir(userName), filename)
	}
}
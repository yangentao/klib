package net.yet.file

import net.yet.util.app.App
import net.yet.util.database.DBMap
import java.io.File

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

object AppFile : AbsFile {
	private val dbmap = DBMap("user_dir_map")

	override val root: File
		get() = App.get().filesDir


	fun userDir(userName: String): File {
		var dirname = dbmap.get(userName)
		if (dirname == null) {
			dirname = "u" + System.currentTimeMillis()
			dbmap.put(userName, dirname)
		}
		val dir = File(root, dirname)
		if (!dir.exists()) {
			dir.mkdir()
		}
		return dir
	}

	fun userFile(userName: String, filename: String): File {
		return File(userDir(userName), filename)
	}
}

package net.yet.file

import net.yet.util.app.App
import java.io.File

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

object SdAppFile : AbsFile {
	override val root: File
		get() = App.sdAppRoot()

	fun log(file: String): File {
		return File(dir("xlog"), file)
	}
}


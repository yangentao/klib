package net.yet.file

import net.yet.util.app.App
import java.io.File

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

object SdAppFileVer : AbsFile {
	override val root: File
		get() = File(App.sdAppRoot(), "V" + App.getVersionCode())

	init {
		root.mkdirs()
		root.mkdir()
	}
}


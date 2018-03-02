package yet.file

import yet.util.app.App
import java.io.File

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

object AppFileVer : AbsFile {
	override val root: File
		get() = File(App.app.filesDir, "V" + App.versionCode)

	init {
		root.mkdirs()
		root.mkdir()
	}
}

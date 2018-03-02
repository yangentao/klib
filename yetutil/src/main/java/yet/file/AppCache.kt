package yet.file

import yet.util.app.App
import java.io.File

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

object AppCache : AbsFile {
	override val root: File
		get() = App.app.cacheDir
}

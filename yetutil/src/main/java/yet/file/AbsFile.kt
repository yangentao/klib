package yet.file

import yet.util.MyDate
import yet.util.log.logd
import java.io.File
import java.io.IOException

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

interface AbsFile {
	val root: File get

	fun dir(dir: String): File {
		val f = File(root, dir)
		if (!f.exists()) {
			f.mkdirs()
			f.mkdir()
			try {
				File(f, ".nomedia").createNewFile()
			} catch (e: IOException) {
				logd(f.absolutePath)
				e.printStackTrace()
			}
		}
		return f
	}

	fun tempFile():File{
		return tempFile(".tmp")
	}

	//tempFile(".txt") => 20160130_120159_300.txt
	fun tempFile(ext: String): File {
		var dotExt = ".tmp"
		if (ext.length > 0) {
			if (ext[0] == '.') {//.x
				dotExt = ext
			} else {
				dotExt = "." + ext
			}
		}
		return temp(MyDate.tmpFile() + dotExt)
	}

	fun temp(file: String): File {
		return File(dir("temp"), file)
	}

	fun image(file: String): File {
		return File(dir("image"), file)
	}

	fun video(file: String): File {
		return File(dir("video"), file)
	}

	fun audio(file: String): File {
		return File(dir("audio"), file)
	}

	fun doc(file: String): File {
		return File(dir("doc"), file)
	}
}
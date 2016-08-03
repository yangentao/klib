package net.yet.file

import android.os.Environment
import java.io.File

object SD : AbsFile {
	override val root: File
		get() = Environment.getExternalStorageDirectory()


	fun file(filaname: String): File {
		return File(root, filaname)
	}
}

package yet.net

import android.net.Uri
import yet.util.Progress

/**
 * Created by entaoyang@163.com on 2016-11-12.
 */

//file, key, filename, mime都不能是空
class FileParam(val key: String,
                val file: Uri,
                var filename: String = file.lastPathSegment,
                var mime: String = "application/octet-stream"
) {


	var progress: Progress? = null


	fun mime(mime: String?): FileParam {
		if (mime != null) {
			this.mime = mime
		}
		return this
	}

	fun fileName(filename: String?): FileParam {
		if (filename != null) {
			this.filename = filename
		}
		return this
	}

	fun progress(progress: Progress?): FileParam {
		this.progress = progress
		return this
	}

	override fun toString(): String {
		return "key=$key, filename=$filename, mime=$mime, file=${file.toString()}"
	}
}

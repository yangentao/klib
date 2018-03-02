package yet.util.app

import android.app.DownloadManager.Request
import android.net.Uri
import com.google.gson.JsonObject
import yet.database.MapTable
import yet.file.SdAppFile
import yet.json.getValue
import yet.json.setValue
import yet.util.Util
import yet.util.log.logd
import java.io.File

class DownloadTask(val jo: JsonObject) {

	var id: Long by jo
	var url: String by jo
	var action: String by jo

	var filepath: String by jo


	fun processAction(di: DownloadInfo) {
		val file = File(filepath)
//		val file = File(di.localUri!!.path)
		if (action == ACTION_INSTALLL) {
			Util.installApk(file)
		}
	}


	companion object {
		val downMap = MapTable("downloadTasks")

		val ACTION_INSTALLL = "actionInstall"

		fun onComplete(di: DownloadInfo) {

			val jo = downMap.getJsonObject("${di.id}")
			downMap.remove("${di.id}")
			if (jo != null) {
				val task = DownloadTask(jo)
				if (di.success) {
					task.processAction(di)
				} else {
					logd("下载失败 ", task.url)
				}
			}
		}

		fun downloadAndInstall(url: String, title: String, extName: String, desc: String = "") {
			val task = DownloadTask(JsonObject())
			task.url = url
			task.action = ACTION_INSTALLL

			val file = SdAppFile.tempFile(extName)
			task.filepath = file.absolutePath

			val dm = App.downloadManager
			val req = Request(Uri.parse(url))
			if (title.isNotEmpty()) {
				req.setTitle(title)
			}
			req.setDestinationUri(Uri.fromFile(file))
			if (desc.isNotEmpty()) {
				req.setDescription(desc)
			}
			task.id = dm.enqueue(req)

			downMap.put(task.id.toString(), task.jo.toString())

		}

	}
}
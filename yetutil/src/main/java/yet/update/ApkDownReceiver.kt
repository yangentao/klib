package yet.update

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import yet.database.MapTable
import yet.util.app.App
import yet.util.log.logd

class ApkDownReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
		logd("Down Receiver ID: ", id)
		if (id == -1L) {
			return
		}
		downMap.getString(id.toString()) ?: return
		val fileUri = App.downloadManager.getUriForDownloadedFile(id) ?: return
		logd(fileUri)
		val i = Intent(Intent.ACTION_VIEW)
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		i.setDataAndType(fileUri, "application/vnd.android.package-archive")
		context.startActivity(i)
	}


	companion object {
		val downMap = MapTable("downloadTasks")

		fun downloadAndInstall(url: String, title: String, desc: String = "") {
			val dm = App.downloadManager
			val req = DownloadManager.Request(Uri.parse(url))
			if (title.isNotEmpty()) {
				req.setTitle(title)
			}
			req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${App.app_name}.apk")
			if (desc.isNotEmpty()) {
				req.setDescription(desc)
			}
			val downId = dm.enqueue(req)
			logd("DownloadTask enqueue: ", downId)
			downMap.put(downId.toString(), url)

		}

	}
}

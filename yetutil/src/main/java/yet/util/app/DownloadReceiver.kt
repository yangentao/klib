package yet.util.app

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import yet.util.database.CursorResult

class DownloadReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
		if (id == -1L) {
			return
		}
		val query = DownloadManager.Query()
		query.setFilterById(id)
		query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL or DownloadManager.STATUS_FAILED)
		val dm = App.downloadManager
		val c = dm.query(query)
		val cw = CursorResult(c)
		val jo = cw.jsonObject()
		if (jo != null) {
			val di = DownloadInfo(jo)
			DownloadTask.onComplete(di)
		}
	}
}

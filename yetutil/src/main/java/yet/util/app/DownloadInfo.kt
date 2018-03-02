package yet.util.app

import android.app.DownloadManager
import android.net.Uri
import com.google.gson.JsonObject
import yet.anno.Name
import yet.json.getValue

/**
 * Created by entaoyang@163.com on 2017-05-27.
 */
//{
//	"_id": 157,
//	"local_filename": "/storage/emulated/0/Android/data/yet.hare/files/temp/20170527_072853_374_1.apk",
//	"mediaprovider_uri": null,
//	"destination": 4,
//	"title": "Party.apk",
//	"description": "mcall update",
//	"uri": "http://app800.cn/dav/mcall/mcall-39000.apk",
//	"status": 8,
//	"hint": "file:///storage/emulated/0/Android/data/yet.hare/files/temp/20170527_072853_374_1.apk",
//	"media_type": "application/vnd.android.package-archive",
//	"total_size": 17348874,
//	"last_modified_timestamp": 1495841337316,
//	"bytes_so_far": 17348874,
//	"allow_write": 0,
//	"local_uri": "file:///storage/emulated/0/Android/data/yet.hare/files/temp/20170527_072853_374_1.apk",
//	"reason": "placeholder"
//}
class DownloadInfo(val jo: JsonObject) {
	@Name(DownloadManager.COLUMN_ID)
	val id: Long by jo
	@Name(DownloadManager.COLUMN_URI)
	val uri: Uri by jo
	@Name(DownloadManager.COLUMN_LOCAL_URI)
	val localUri: Uri? by jo
	@Name(DownloadManager.COLUMN_STATUS)
	val status: Int by jo

	@Name(DownloadManager.COLUMN_TITLE)
	val title: Int by jo
	@Name(DownloadManager.COLUMN_DESCRIPTION)
	val desc: Int by jo

	@Name(DownloadManager.COLUMN_MEDIA_TYPE)
	val mediaType: Int by jo
	@Name(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
	val totalSize: Long by jo


	val success:Boolean get() = DownloadManager.STATUS_SUCCESSFUL == status
}
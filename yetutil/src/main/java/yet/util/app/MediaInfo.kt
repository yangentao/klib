package yet.util.app

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import yet.util.database.UriQuery
import yet.yson.YsonObject

/**
 * Created by entaoyang@163.com on 16/5/12.
 */

class MediaInfo(val uri: Uri) {
	var displayName: String? = null
	var mimeType: String? = null
	var path: String? = null
	var size = 0
	var width = 0
	var height = 0
	var record: YsonObject? = null
	var found = false


	init {
		////content://media/external/images/media/10025
		if (uri.host == "media" && uri.scheme == "content") {
			val jo = UriQuery(uri).resultYsonObject()
			if (jo != null) {
				displayName = jo.str(MediaStore.MediaColumns.DISPLAY_NAME)
				mimeType = jo.str(MediaStore.MediaColumns.MIME_TYPE)
				path = jo.str(MediaStore.MediaColumns.DATA)
				size = jo.int(MediaStore.MediaColumns.SIZE) ?: 0
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					width = jo.int(MediaStore.MediaColumns.WIDTH) ?: 0
					height = jo.int(MediaStore.MediaColumns.HEIGHT) ?: 0
				}
				record = jo
				found = true
			}
		}
	}

}

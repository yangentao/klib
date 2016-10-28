package net.yet.util.app

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.gson.JsonObject
import net.yet.ext.optInt
import net.yet.ext.optString
import net.yet.util.database.UriQuery
import net.yet.util.log.log

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
	var record: JsonObject? = null
	var found = false


	init {
		log(uri)
		val jo = UriQuery(uri).resultJsonObject()
		if (jo != null) {
			displayName = jo.optString(MediaStore.MediaColumns.DISPLAY_NAME)
			mimeType = jo.optString(MediaStore.MediaColumns.MIME_TYPE)
			path = jo.optString(MediaStore.MediaColumns.DATA)
			size = jo.optInt(MediaStore.MediaColumns.SIZE)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				width = jo.optInt(MediaStore.MediaColumns.WIDTH)
				height = jo.optInt(MediaStore.MediaColumns.HEIGHT)
			}
			record = jo
			found = true

			log(path)
			log(displayName)
			log(mimeType)
			log(path)
			log(size)
			log(width)
			log(height)

		}
	}

}

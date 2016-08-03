package net.yet.net

import android.content.ContentUris
import android.net.Uri

/**
 * Created by yet on 2015/10/6.
 */
object UriUtil {
	// with path
	fun uriPath(uri: Uri, pathSegment: String): Uri {
		return Uri.withAppendedPath(uri, Uri.encode(pathSegment))
	}

	// with id
	fun uriId(uri: Uri, id: Long): Uri {
		return ContentUris.withAppendedId(uri, id)
	}

	// with param
	fun uriParam(uri: Uri, key: String, value: String): Uri {

		return uri.buildUpon().appendQueryParameter(key, value).build()
	}

	fun uriId(uri: Uri): Long {
		return ContentUris.parseId(uri)
	}
}

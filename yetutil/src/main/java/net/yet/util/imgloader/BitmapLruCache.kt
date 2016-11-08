package net.yet.util.imgloader

import android.graphics.Bitmap
import android.util.LruCache

/**
 * Created by entaoyang@163.com on 2016-11-08.
 */

class BitmapLruCache(size: Int = 4 * 1024 * 1024) : LruCache<String, Bitmap>(size) {
	override fun sizeOf(key: String, value: Bitmap): Int {
		return value.rowBytes * value.height
	}
}
package net.yet.ui.res

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import net.yet.util.StreamUtil
import net.yet.util.Util
import net.yet.util.app.App
import net.yet.util.xlog
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */

object Asset {
	val manager: AssetManager get() = App.get().assets

	fun streamBuffered(name: String): InputStream? {
		return manager.open(name, AssetManager.ACCESS_BUFFER)
	}

	fun stream(name: String): InputStream? {
		return manager.open(name, AssetManager.ACCESS_STREAMING)
	}

	fun streamZip(name: String): ZipInputStream? {
		val inStream = stream(name) ?: return null
		val bis = BufferedInputStream(inStream, 32 * 1024)
		return ZipInputStream(bis)
	}

	fun uri(filename: String): Uri {
		return Uri.parse("file:///android_asset/" + filename)
	}

	fun list(path: String): Array<String> {
		var path2 = path
		try {
			if (path2.endsWith("/")) {
				path2 = path2.substring(0, path2.length - 1)
			}
			return manager.list(path2)
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return arrayOf()
	}

	// 读取assets目录下的utf8文件
	fun text(path: String, encoding: String = Util.UTF8): String? {
		try {
			val inStream = streamBuffered(path) ?: return null
			return  StreamUtil.readString(inStream, encoding)
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return null
	}

	// 读取图片 , 适合小图片!! 设置密度是hdpi, 不支持9png!!!!
	fun bitmap(path: String): Bitmap? {
		var inStream: InputStream? = null
		try {
			inStream = streamBuffered(path) ?: return null
			val bmp = BitmapFactory.decodeStream(inStream)
			if (bmp != null) {
				bmp.density = DisplayMetrics.DENSITY_HIGH
			}
			return bmp
		} catch (e: IOException) {
			e.printStackTrace()
			xlog.e(e)
		} finally {
			Util.close(inStream)
		}
		return null
	}

	// //读取图片 , 适合小图片!! 设置密度是hdpi, 支持9png
	fun drawable(path: String): Drawable? {
		var inStream: InputStream? = null
		try {
			inStream = streamBuffered(path) ?: return null
			val opts = BitmapFactory.Options()
			opts.inScreenDensity = DisplayMetrics.DENSITY_HIGH
			return Drawable.createFromResourceStream(App.getResources(), null, inStream, null, opts)
		} catch (e: Exception) {
			e.printStackTrace()
			xlog.e(e)
		} finally {
			Util.close(inStream)
		}
		return null
	}


}
package yet.util

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import yet.util.app.App
import yet.util.log.xlog
import java.io.*
import java.util.zip.ZipInputStream

object AssetUtil {
	fun mgr(): AssetManager {
		return App.app.assets
	}

	@Throws(IOException::class)
	fun read(name: String): InputStream? {
		return readBuffer(name)
	}

	@Throws(IOException::class)
	fun readBuffer(name: String): InputStream? {
		return mgr().open(name, AssetManager.ACCESS_BUFFER)
	}

	@Throws(IOException::class)
	fun readStream(name: String): InputStream? {
		return mgr().open(name, AssetManager.ACCESS_STREAMING)
	}

	@Throws(IOException::class)
	fun readZip(name: String): ZipInputStream? {
		val inStream = readStream(name)
		val bis = BufferedInputStream(inStream, 32 * 1024)
		return ZipInputStream(bis)
	}

	fun uri(filename: String): Uri {
		return Uri.parse("file:///android_asset/" + filename)
	}

	fun list(path: String): Array<String> {
		var p = path
		try {
			if (p.endsWith("/")) {
				p = p.substring(0, p.length - 1)
			}
			return mgr().list(p)
		} catch (e: IOException) {
			e.printStackTrace()
		}

		return arrayOf()
	}

	// 读取assets目录下的utf8文件
	fun textUTF8(path: String): String? {
		try {
			val inStream = readBuffer(path)
			val s = StreamUtil.readString(inStream, Util.UTF8)
			return s
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return null
	}

	// 读取图片 , 适合小图片!! 设置密度是hdpi, 不支持9png!!!!
	fun bitmap(path: String): Bitmap? {
		var inStream: InputStream? = null
		try {
			inStream = readBuffer(path)
			if (inStream != null) {
				val bmp = BitmapFactory.decodeStream(inStream)
				if (bmp != null) {
					bmp.density = DisplayMetrics.DENSITY_HIGH
				}
				return bmp
			}
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
			inStream = readBuffer(path)
			if (inStream != null) {
				val opts = BitmapFactory.Options()
				opts.inScreenDensity = DisplayMetrics.DENSITY_HIGH
				return Drawable.createFromResourceStream(App.resource, null, inStream, null, opts)
			}
			// return Drawable.createFromResourceStream(null, null, is, null, null);
		} catch (e: Exception) {
			e.printStackTrace()
			xlog.e(e)
		} finally {
			Util.close(inStream)
		}
		return null
	}

}

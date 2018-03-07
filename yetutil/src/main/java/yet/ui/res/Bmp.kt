package yet.ui.res

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import yet.util.Util
import yet.util.app.App
import java.io.*

/**
 * Created by entaoyang@163.com on 2018-03-06.
 */

class Bmp(val bmp: Bitmap) {

	val width: Int get() = bmp.width
	val height: Int get() = bmp.height

	val drawable: BitmapDrawable
		get() {
			return BitmapDrawable(App.resource, bmp)
		}

	fun rotate(degree: Int): Bitmap? {
		val matrix = Matrix()
		matrix.postRotate(degree.toFloat())
		return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true)
	}

	//限制最大的高宽, 等比例缩放, 比如, 原图 300 * 400, limi(200,200)将会将图片变为原来的1/2, 150* 200
	fun limit(maxWidth: Int, maxHeight: Int = maxWidth): Bmp {
		if (width < maxWidth && height < maxHeight) {
			return this
		}
		var fw = if (width > maxWidth) {
			maxWidth * 1.0 / width
		} else 1.0
		var fh = if (height > maxHeight) {
			maxHeight * 1.0 / height
		} else 1.0
		var f = Math.min(fw, fh).toFloat()
		return scale(f)
	}

	fun scale(fWidth: Float, fHeight: Float): Bmp {
		val matrix = Matrix()
		matrix.postScale(fWidth, fHeight)
		val b = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
		return Bmp(b)
	}

	fun scaleTo(newWidth: Int, newHeight: Int): Bmp {
		val scaleWidth = newWidth.toFloat() / width
		val scaleHeight = newHeight.toFloat() / height
		return scale(scaleWidth, scaleHeight)
	}

	fun scale(scale: Float): Bmp {
		return scale(scale)
	}


	fun savePng(saveTo: File): Boolean {
		try {
			val fos = FileOutputStream(saveTo)
			return savePng(fos)
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		}

		return false
	}

	fun savePng(saveTo: OutputStream): Boolean {
		try {
			return bmp.compress(Bitmap.CompressFormat.PNG, 100, saveTo)
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			Util.close(saveTo)
		}
		return false
	}

	fun saveJpg(saveTo: File): Boolean {
		try {
			val fos = FileOutputStream(saveTo)
			return saveJpg(fos)
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		}

		return false
	}

	fun saveJpg(saveTo: OutputStream): Boolean {
		try {
			return bmp.compress(Bitmap.CompressFormat.JPEG, 100, saveTo)
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			Util.close(saveTo)
		}
		return false
	}


	companion object {
		fun res(id: Int): Bmp {
			val b = BitmapFactory.decodeResource(App.resource, id)!!
			return Bmp(b)
		}

		fun degress(file: File): Int {
			val path = file.absolutePath
			var degree = 0
			try {
				val exif = ExifInterface(path)
				val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
				when (orientation) {
					ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
					ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
					ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
				}
			} catch (e: IOException) {
				e.printStackTrace()
			}

			return degree
		}
	}
}
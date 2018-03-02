package yet.util

import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.Config
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.util.DisplayMetrics
import yet.file.SdAppFile
import yet.ui.ext.dp
import yet.util.app.App
import java.io.*

object BmpUtil {


	fun fromAsset(path: String): Bitmap? {
		return AssetUtil.bitmap(path)
	}

	fun fromRes(resId: Int): Bitmap? {
		return BitmapFactory.decodeResource(App.resource, resId)
	}

	fun scaleTo(resId: Int, newWidth: Int, newHeight: Int): Bitmap? {
		return scaleTo(fromRes(resId), newWidth, newHeight)
	}

	/**
	 * @param old       非空, 高宽都大于0
	 * *
	 * @param newWidth  >0
	 * *
	 * @param newHeight >0
	 * *
	 * @return
	 */
	fun scaleTo(old: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
		if (old == null) {
			return null
		}
		var width = old.width
		var height = old.height
		if (width == 0) {
			width = 1
		}
		if (height == 0) {
			height = 1
		}
		val scaleWidth = newWidth.toFloat() / width
		val scaleHeight = newHeight.toFloat() / height
		val matrix = Matrix()
		matrix.postScale(scaleWidth, scaleHeight)
		return Bitmap.createBitmap(old, 0, 0, width, height, matrix, true)
	}

	/**
	 * 按比例缩小图片

	 * @param old
	 * *
	 * @param scale 0-1.0之间, 0.5缩小 1倍
	 * *
	 * @return
	 */
	fun scale(old: Bitmap?, scale: Float): Bitmap? {
		if (old == null) {
			return null
		}
		val width = old.width
		val height = old.height
		if (width == 0 || height == 0) {
			return null
		}
		val matrix = Matrix()
		matrix.postScale(scale, scale)
		return Bitmap.createBitmap(old, 0, 0, width, height, matrix, true)
	}

	fun rotate(old: Bitmap?, degree: Int): Bitmap? {
		if (old == null) {
			return null
		}
		val width = old.width
		val height = old.height
		if (width == 0 || height == 0) {
			return null
		}
		val matrix = Matrix()
		matrix.postRotate(degree.toFloat())
		return Bitmap.createBitmap(old, 0, 0, width, height, matrix, true)
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

	fun line(width: Int, height: Int, color: Int): Bitmap {
		val target = Bitmap.createBitmap(width, height, Config.ARGB_8888)
		target.density = DisplayMetrics.DENSITY_HIGH
		val canvas = Canvas(target)
		canvas.drawColor(color)
		return target
	}

	fun lineDraw(width: Int, height: Int, color: Int): BitmapDrawable {
		return BitmapDrawable(App.resource, line(width, height, color))
	}
	// 高和宽较最小的值做直径,
	fun tint(source: Bitmap, color: Int): Bitmap {
		val w = source.getScaledWidth(App.resource.displayMetrics)
		val h = source.getScaledHeight(App.resource.displayMetrics)
		val target = Bitmap.createBitmap(w, h, Config.ARGB_8888)
		val canvas = Canvas(target)
		val paint = Paint()
		paint.color = color
		val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
		canvas.drawRect(rect, paint)
		paint.isAntiAlias = true
		paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
		val rect2 = Rect(0, 0, w, h)
		canvas.drawBitmap(source, rect2, rect2, paint)
		return target
	}
	// 高和宽较最小的值做直径,
	fun roundSqure(source: Bitmap?, cornerDp: Int): Bitmap? {
		if (source == null) {
			return null
		}
		val corner = dp(cornerDp).toFloat()
		val w = source.getScaledWidth(App.resource.displayMetrics)
		val h = source.getScaledHeight(App.resource.displayMetrics)
		val paint = Paint()
		paint.isAntiAlias = true

		val d = Math.min(w, h)
		val target = Bitmap.createBitmap(d, d, Config.ARGB_8888)
		val canvas = Canvas(target)
		val rect = RectF(0f, 0f, d.toFloat(), d.toFloat())
		canvas.drawRoundRect(rect, corner, corner, paint)
		paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
		canvas.drawBitmap(source, (-(w - d) / 2).toFloat(), (-(h - d) / 2).toFloat(), paint)
		return target
	}


	//指定圆角
	fun round(source: Bitmap?, cornerDp: Int): Bitmap? {
		if (source == null) {
			return null
		}
		val corner = dp(cornerDp).toFloat()
		val w = source.getScaledWidth(App.resource.displayMetrics)
		val h = source.getScaledHeight(App.resource.displayMetrics)
		val paint = Paint()
		paint.isAntiAlias = true
		val target = Bitmap.createBitmap(w, h, Config.ARGB_8888)
		val canvas = Canvas(target)

		val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
		canvas.drawRoundRect(rect, corner, corner, paint)
		paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
		canvas.drawBitmap(source, 0f, 0f, paint)
		return target
	}


	//高宽的一半做圆角
	fun round(source: Bitmap?): Bitmap? {
		if (source == null) {
			return null
		}
		val w = source.getScaledWidth(App.resource.displayMetrics)
		val h = source.getScaledHeight(App.resource.displayMetrics)

		val paint = Paint()
		paint.isAntiAlias = true
		val target = Bitmap.createBitmap(w, h, Config.ARGB_8888)
		val canvas = Canvas(target)
		val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
		canvas.drawRoundRect(rect, (w / 2).toFloat(), (h / 2).toFloat(), paint)
		paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
		canvas.drawBitmap(source, 0f, 0f, paint)
		return target
	}

	//圆,  高和宽较最小的值做直径,
	fun oval(source: Bitmap?): Bitmap? {

		if (source == null) {
			return null
		}
		val w = source.getScaledWidth(App.resource.displayMetrics)
		val h = source.getScaledHeight(App.resource.displayMetrics)
		val paint = Paint()
		paint.isAntiAlias = true

		val d = Math.min(w, h)
		val corner = d / 2
		val target = Bitmap.createBitmap(d, d, Config.ARGB_8888)
		val canvas = Canvas(target)
		val rect = RectF(0f, 0f, d.toFloat(), d.toFloat())
		canvas.drawRoundRect(rect, corner.toFloat(), corner.toFloat(), paint)
		paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
		canvas.drawBitmap(source, (-(w - d) / 2).toFloat(), (-(h - d) / 2).toFloat(), paint)
		return target
	}


	@Throws(FileNotFoundException::class)
	fun sizeOf(uri: Uri): MySize? {
		return sizeOfStream(App.openStream(uri))
	}

	@Throws(FileNotFoundException::class)
	fun sizeOf(file: File): MySize? {
		return sizeOfStream(FileInputStream(file))
	}

	//会关闭流
	private fun sizeOfStream(inputStream: InputStream): MySize? {
		try {
			val opts = BitmapFactory.Options()
			opts.inJustDecodeBounds = true
			BitmapFactory.decodeStream(inputStream, null, opts)
			return MySize(opts.outWidth, opts.outHeight)
		} catch (ex: Exception) {
			ex.printStackTrace()
		} finally {
			Util.close(inputStream)
		}
		return null
	}

	//会关闭流
	private fun decodeStream(inputStream: InputStream, inSampleSize: Int, config: Bitmap.Config): Bitmap {
		val opts = BitmapFactory.Options()
		opts.inJustDecodeBounds = false
		opts.inSampleSize = inSampleSize
		opts.inPreferredConfig = config
		opts.inInputShareable = true
		opts.inPurgeable = true
		val bmp = BitmapFactory.decodeStream(inputStream, null, opts)
		Util.close(inputStream)
		return bmp
	}

	private fun decodeStream(inputStream: InputStream): Bitmap? {
		val bmp = BitmapFactory.decodeStream(inputStream)
		Util.close(inputStream)
		return bmp
	}

	/**
	 * 图片高*宽不超过maxSize

	 * @param uri
	 * *
	 * @param maxSize newWidth*newHeight
	 * *
	 * @return
	 */
	fun fromUri(uri: Uri?, maxSize: Int, config: Bitmap.Config): Bitmap? {
		if (uri == null) {
			return null
		}
		try {
			val size = sizeOf(uri) ?: return null
			if (size.area() > maxSize && maxSize > 0) {
				// 300 * 200 /(100*100) ==> 6
				var scale = size.area() * 1.0f / maxSize
				scale = Math.sqrt(scale.toDouble()).toFloat()// 2.4
				val n = Math.round(scale)// 2
				return decodeStream(App.openStream(uri), n, config)
			} else {
				return decodeStream(App.openStream(uri))
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}

		return null
	}

	fun fromUri(uri: Uri?): Bitmap? {
		if (uri == null) {
			return null
		}
		try {
			return decodeStream(App.openStream(uri))
		} catch (ex: Exception) {
			ex.printStackTrace()
		}

		return null
	}

	//maxSize是指的高*宽, 最终bitmap占用的内存是  maxSize*每个像素字节数
	//hiQulity是true, 每像素占用4字节,  false:2字节
	fun fromFile(imageFile: File?, maxSize: Int, config: Bitmap.Config): Bitmap? {
		if (null == imageFile || !imageFile.exists()) {
			return null
		}
		try {
			val size = sizeOf(imageFile) ?: return null
			if (size.area() > maxSize && maxSize > 0) {
				// 300 * 200 /(100*100) ==> 6
				var scale = size.area() * 1.0f / maxSize
				scale = Math.sqrt(scale.toDouble()).toFloat()// 2.4
				val n = Math.round(scale)// 2
				return decodeStream(FileInputStream(imageFile), n, config)
			} else {
				return decodeStream(FileInputStream(imageFile))
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}

		return null
	}

	fun savePng(bmp: Bitmap, saveTo: File): Boolean {
		try {
			val fos = FileOutputStream(saveTo)
			return savePng(bmp, fos)
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		}

		return false
	}

	fun savePng(bmp: Bitmap, saveTo: OutputStream): Boolean {
		try {
			return bmp.compress(CompressFormat.PNG, 100, saveTo)
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			Util.close(saveTo)
		}
		return false
	}

	fun saveJpg(bmp: Bitmap, saveTo: File): Boolean {
		try {
			val fos = FileOutputStream(saveTo)
			return saveJpg(bmp, fos)
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		}

		return false
	}

	fun saveJpg(bmp: Bitmap, saveTo: OutputStream): Boolean {
		try {
			return bmp.compress(CompressFormat.JPEG, 100, saveTo)
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			Util.close(saveTo)
		}
		return false
	}

	fun compressJpg(from: Uri, maxSize: Int): File? {
		val bmp = fromUri(from, maxSize, Config.ARGB_8888)
		if (bmp != null) {
			val tofile = SdAppFile.tempFile(".jpg")
			val b = saveJpg(bmp, tofile)
			if (b && tofile.exists()) {
				return tofile
			} else {
				tofile.delete()
			}
		}
		return null
	}

	fun compressPng(from: Uri, maxSize: Int): File? {
		val bmp = fromUri(from, maxSize, Config.ARGB_8888)
		if (bmp != null) {
			val tofile = SdAppFile.tempFile(".png")
			val b = savePng(bmp, tofile)
			if (b && tofile.exists()) {
				return tofile
			} else {
				tofile.delete()
			}
		}
		return null
	}


	//返回"image/png", "image/jpeg",或null
	fun mimeType(uri: Uri): String? {
		var `is`: InputStream? = null
		try {
			`is` = App.openStream(uri)
			val opts = BitmapFactory.Options()
			opts.inJustDecodeBounds = true
			BitmapFactory.decodeStream(`is`, null, opts)
			return opts.outMimeType
		} catch (e: Exception) {
			e.printStackTrace()
		} finally {
			Util.close(`is`)
		}
		return null
	}
}

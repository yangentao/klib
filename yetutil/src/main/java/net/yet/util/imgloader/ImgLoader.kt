package net.yet.util.imgloader

import android.graphics.Bitmap
import android.widget.ImageView
import net.yet.util.BmpUtil
import net.yet.util.fore
import java.io.File
import java.lang.ref.WeakReference

/**
 * Created by entaoyang@163.com on 2016-11-02.
 */

object ImgLoader {
	var cache = BitmapLruCache()

	object Local {
		fun remove(url: String) {
			file(url)?.delete()
		}

		fun exists(url: String): Boolean {
			return file(url)?.exists() ?: false
		}

		fun file(url: String): File? {
			return FileDownloader.findLocal(url)
		}

		fun bitmap(url: String, config: BmpConfig): Bitmap? {
			val file = file(url) ?: return null
			if (file.exists()) {
				return BmpUtil.fromFile(file, config.maxSize, config.quility)
			}
			return null
		}
	}

	fun findCache(url: String, config: BmpConfig): Bitmap? {
		val key = url + config.toString()
		var bmp: Bitmap? = cache.get(key)
		if (bmp != null) {
			return bmp
		}
		bmp = Local.bitmap(url, config)
		if (bmp != null) {
			if (config.maxSize < 480 * 800) {
				cache.put(key, bmp)
			}
		}
		return bmp
	}

	fun retrive(url: String, block: (File?) -> Unit) {
		FileDownloader.retrive(url, block)
	}

	fun bitmap(url: String, config: BmpConfig, block: (Bitmap?) -> Unit) {
		val b = findCache(url, config)
		if (b != null) {
			block(b)
			return
		}
		FileDownloader.retrive(url) {
			block(findCache(url, config))
		}
	}

	fun display(imageView: ImageView, url: String, config: BmpConfig) {
		if (!Local.exists(url)) {
			if (config.defaultResId != 0) {
				imageView.setImageResource(config.defaultResId)
			}
		}
		val weekView = WeakReference<ImageView>(imageView)
		bitmap(url, config) {
			fore {
				if (it != null) {
					weekView.get()?.setImageBitmap(it)
				} else if (config.failedResId != 0) {
					weekView.get()?.setImageResource(config.failedResId)
				}
			}
		}
	}

	fun displaySmall(imageView: ImageView, url: String) {
		display(imageView, url) {
			small()
		}
	}

	fun displayMid(imageView: ImageView, url: String) {
		display(imageView, url) {
			mid()
		}
	}

	fun displayBig(imageView: ImageView, url: String) {
		display(imageView, url) {
			big()
		}
	}

	fun displayLarge(imageView: ImageView, url: String) {
		display(imageView, url) {
			large()
		}
	}

	fun display(imageView: ImageView, url: String, block: BmpConfig.() -> Unit) {
		val c = BmpConfig()
		c.block()
		display(imageView, url, c)
	}


}
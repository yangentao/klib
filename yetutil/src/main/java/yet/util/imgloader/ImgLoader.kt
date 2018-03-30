package yet.util.imgloader

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import yet.ui.res.Bmp
import yet.ui.res.roundSqure
import yet.util.fore
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
				val bmp = Bmp.file(file, config.maxEdge, config.quility) ?: return null
				if (config.corner > 0) {
					return bmp.roundSqure(config.corner)
				}else {
					return bmp
				}

			}
			return null
		}
	}

	private fun removeCache(url: String, config: BmpConfig) {
		val key = url + config.toString()
		cache.remove(key)
	}

	fun findCache(url: String, config: BmpConfig): Bitmap? {
		val key = url + config.toString()
		var bmp: Bitmap? = cache.get(key)
		if (bmp != null) {
			return bmp
		}
		bmp = Local.bitmap(url, config)
		if (bmp != null) {
			if (config.maxEdge <=  800) {
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
			if (config.forceDownload) {
				FileDownloader.download(url) {
					removeCache(url, config)
					block(findCache(url, config))
				}
			} else {
				block(b)
			}
		} else {
			FileDownloader.retrive(url) {
				block(findCache(url, config))
			}
		}

	}

	fun displayUri(imageView: ImageView, uri: Uri?, block: BmpConfig.() -> Unit) {
		val c = BmpConfig()
		c.block()
		displayUri(imageView, uri, c)
	}

	fun displayUri(imageView: ImageView, uri: Uri?, config: BmpConfig) {
		if (uri == null) {
			imageView.setImageResource(config.failedResId)
			return
		}
		val key = uri.toString() + config.toString()
		var bmpCache: Bitmap? = cache.get(key)
		var bmp = bmpCache ?: Bmp.uri(uri, config.maxEdge, config.quility)
		if (bmp != null) {
			imageView.setImageBitmap(bmp)
			if (bmpCache == null && config.maxEdge <=  800) {
				cache.put(key, bmp)
			}
		} else {
			imageView.setImageResource(config.failedResId)
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

	fun display(imageView: ImageView, url: String, block: BmpConfig.() -> Unit) {
		val c = BmpConfig()
		c.block()
		display(imageView, url, c)
	}

	fun displaySmall(imageView: ImageView, url: String) {
		display(imageView, url) {
			small64()
		}
	}

	fun displayMid(imageView: ImageView, url: String) {
		display(imageView, url) {
			mid128()
		}
	}

	fun displayBig(imageView: ImageView, url: String) {
		display(imageView, url) {
			big256()
		}
	}

	fun displayLarge(imageView: ImageView, url: String) {
		display(imageView, url) {
			large480()
		}
	}


}
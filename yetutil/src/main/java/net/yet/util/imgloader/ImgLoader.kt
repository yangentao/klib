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
	var config: BmpConfig = BmpConfig()

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

	fun retrive(url:String, block:(File?)->Unit){
		FileDownloader.retrive(url, block)
	}

	fun bitmap(url: String, config: BmpConfig, block: (Bitmap?) -> Unit) {
		FileDownloader.retrive(url) {
			block(Local.bitmap(url, config))
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

	fun display(imageView: ImageView, url: String) {
		display(imageView, url, config)
	}

	fun display(imageView: ImageView, url: String, block: BmpConfig.() -> Unit) {
		val c = BmpConfig()
		c.block()
		display(imageView, url, c)
	}


}
package net.yet.util.imgloader

import android.graphics.Bitmap
import net.yet.R

/**
 * Created by entaoyang@163.com on 2016-11-02.
 */

class BmpConfig {
	//最大高*宽
	var maxSize: Int = BmpSize.MID
	var quility: Bitmap.Config = Bitmap.Config.RGB_565
	//加载失败的图片
	var failedResId: Int = R.drawable.image_miss
	//默认的图片, 下载前
	var defaultResId: Int = R.drawable.image_miss

	var forceDownload: Boolean = false

	override fun toString(): String {
		return Integer.toString(maxSize) + quility.toString()
	}

	fun forceDownload(): BmpConfig {
		this.forceDownload = true
		return this
	}

	fun small(): BmpConfig {
		return maxSize(BmpSize.SMALL)
	}

	fun mid(): BmpConfig {
		return maxSize(BmpSize.MID)
	}

	fun big(): BmpConfig {
		return maxSize(BmpSize.BIG)
	}

	fun large(): BmpConfig {
		return maxSize(BmpSize.LARGE)
	}

	fun maxSize(n: Int): BmpConfig {
		maxSize = n
		return this
	}

	fun argb8888(): BmpConfig {
		quility = Bitmap.Config.ARGB_8888
		return this
	}

	fun rgb565(): BmpConfig {
		quility = Bitmap.Config.RGB_565
		return this
	}

	fun onFailed(id: Int): BmpConfig {
		failedResId = id
		return this
	}

	fun onDefault(id: Int): BmpConfig {
		defaultResId = id
		return this
	}
}
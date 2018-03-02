package yet.util.imgloader

import android.graphics.Bitmap
import net.yet.R
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-11-02.
 */

class BmpConfig {
	//最大高*宽
	var maxSize: Int = BmpSize.MID128
	var quility: Bitmap.Config = Bitmap.Config.RGB_565
	//加载失败的图片
	var failedResId: Int = R.drawable.yet_image_miss
	//默认的图片, 下载前
	var defaultResId: Int = R.drawable.yet_image_miss

	var forceDownload: Boolean = false
	var corner = 0

	override fun toString(): String {
		return Integer.toString(maxSize) + "_" + quility.toString() + "_" + corner.toString()
	}

	fun portrait():BmpConfig{
		big256().round(4).onFailed(net.yet.R.drawable.yet_portrait).onDefault(net.yet.R.drawable.yet_portrait)
		return this
	}

	fun round(cornerDp: Int): BmpConfig {
		this.corner = App.dp2px(cornerDp)
		return this
	}
	fun forceDownload(): BmpConfig {
		this.forceDownload = true
		return this
	}
	fun noCache(): BmpConfig {
		return forceDownload()
	}
	fun small64(): BmpConfig {
		return maxSize(BmpSize.SMALL64)
	}

	fun mid128(): BmpConfig {
		return maxSize(BmpSize.MID128)
	}

	fun big256(): BmpConfig {
		return maxSize(BmpSize.BIG256)
	}

	fun large480(): BmpConfig {
		return maxSize(BmpSize.LARGE480)
	}
	fun sizeUpload1000(): BmpConfig {
		return maxSize(BmpSize.UPLOAD1000)
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
package net.yet.media

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import net.yet.util.Util
import net.yet.util.log.xlog
import java.io.File
import java.io.IOException

/**
 * Created by entaoyang@163.com on 16/7/20.
 */
object MediaUtil {
	/**
	 * 调用系统播放器

	 * @param context
	 * *
	 * @param file
	 */
	fun playSound(context: Context, file: File) {
		val n = file.absolutePath.lastIndexOf('.')
		var ext: String? = null
		if (n > 0) {
			ext = file.absolutePath.substring(n + 1)
		}
		playSound(context, file, ext)
	}

	fun playSound(context: Context, file: File, extType: String?) {
		var extType = extType
		val intent = Intent()
		intent.action = android.content.Intent.ACTION_VIEW
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		if (Util.empty(extType)) {
			extType = "*"
		}
		intent.setDataAndType(Uri.fromFile(file), "audio/" + extType)
		context.startActivity(intent)
	}

	fun playSound(file: File, callback: (MediaPlayer) -> Unit): MediaPlayer? {
		if (!file.exists()) {
			return null
		}
		val player = MediaPlayer()
		player.setOnCompletionListener { mp ->
			mp.release()
			callback(mp)
		}
		try {
			player.setDataSource(file.absolutePath)
			player.prepare()
			player.start()
			return player
		} catch (e: IOException) {
			e.printStackTrace()
			xlog.e(e)
			player.release()
		}

		return null
	}
}

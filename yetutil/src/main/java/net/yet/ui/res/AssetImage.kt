package net.yet.ui.res

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.LruCache
import net.yet.ui.util.StateImage
import net.yet.util.AssetUtil
import net.yet.util.Util
import net.yet.util.app.App
import net.yet.util.debugThrow
import java.util.*

/**
 * /assets/images/user.png
 * 所有文件名须小写
 * 可以使用全名"user.png", 或去掉扩展名"user" => "user.png"
 * Created by entaoyang@163.com on 2016-07-22.
 */

//Assets/images/user.png
object AssetImage {
	private val path = "images/"
	private val LIMIT_SIZE_M = 6
	private val PNG = ".png"
	private val JPG = ".jpg"
	/**
	 * 目录下的文件 如: "user.png"
	 */
	private val fileSet = HashSet<String>(256)

	// 图片缓存 "a.select.png"->bmp
	private val bmpCache = object : LruCache<String, Bitmap>(Util.M * LIMIT_SIZE_M) {
		override fun create(file: String?): Bitmap? {
			return if (file == null) null else AssetUtil.bitmap(path + file)
		}

		override fun sizeOf(key: String, value: Bitmap): Int {
			return value.rowBytes * value.height
		}
	}

	init {
		val files = AssetUtil.list(this.path)
		for (s in files) {
			fileSet.add(s.toLowerCase())
		}
	}

	private fun fullname(file: String): String? {
		if (file.endsWith(PNG) || file.endsWith(JPG)) {
			return file
		}
		if ((file + PNG) in fileSet) {
			return file + PNG
		}
		if ((file + JPG) in fileSet) {
			return file + JPG
		}
		return null
	}

	private fun statedFullname(file: String, state: State): String? {
		if (file.endsWith(PNG) || file.endsWith(JPG)) {
			if (App.debug) {
				if (state.stateString() !in file) {
					debugThrow("错误的文件:" + file + " State:" + state.stateString())
				}
			}
			return file
		}
		val f1 = file + "." + state.stateString() + PNG
		if (f1 in fileSet) {
			return f1
		}
		val f2 = file + "." + state.stateString() + JPG
		if (f2 in fileSet) {
			return f2
		}
		return null
	}

	//"user" => "user.png"
	//"user.png" => "user.png"
	fun bitmap(file: String): Bitmap? {
		val name = fullname(file)
		if (name != null) {
			val b = bmpCache.get(name)
			if (b != null) {
				return b
			}
		}
		return null
	}

	//bitmap("user", State.Selected) => "user.selected.png"
	fun bitmap(file: String, state: State): Bitmap? {
		val name = statedFullname(file, state)
		if (name != null) {
			val b = bmpCache.get(name)
			if (b != null) {
				return b
			}
		}
		return null
	}

	//"user.png"
	fun drawable(file: String): BitmapDrawable? {
		val bmp = bitmap(file)
		if (bmp != null) {
			return BitmapDrawable(App.getResources(), bmp)
		}
		return null
	}

	//drawable("user", State.Selected) => "user.selected.png"
	fun drawable(file: String, state: State): BitmapDrawable? {
		val bmp = bitmap(file, state)
		if (bmp != null) {
			return BitmapDrawable(App.getResources(), bmp)
		}
		return null
	}


	// 获取name对应的图片, 支持pressed/selected等状态 ( StateListDrawable)
	fun drawable(name: String, withStates: Boolean): Drawable? {
		val normal = drawable(name)
		if (!withStates) {
			return normal
		}
		val pressed = drawable(name, State.Pressed)
		val selected = drawable(name, State.Selected)
		val focused = drawable(name, State.Focused)
		val checked = drawable(name, State.Checked)
		val disabled = drawable(name, State.Disabled)
		if (normal != null) {
			return StateImage(normal)
					.pressed(pressed, true)
					.selected(selected, true)
					.focused(focused, true)
					.checked(checked, true)
					.enabled(disabled, false)
					.value
		}
		return null
	}
}
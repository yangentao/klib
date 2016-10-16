package net.yet.ui.res

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import net.yet.util.BmpUtil
import net.yet.util.app.App
import java.io.File

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */

fun ResStr(@StringRes resId: Int): String {
	return Res.str(resId)
}

fun ResColor(@ColorRes resId: Int): Int {
	return Res.color(resId)
}

fun ResDrawable(@DrawableRes resId: Int): Drawable {
	return Res.drawable(resId)
}

fun FileBitmap(file: File, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
	return BmpUtil.fromFile(file, maxSize, config)
}

fun FileDrawable(file: File, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): BitmapDrawable? {
	val bmp = BmpUtil.fromFile(file, maxSize, config) ?: return null
	return BitmapDrawable(App.getResources(), bmp)
}

fun UriBitmap(uri: Uri, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
	return BmpUtil.fromUri(uri, maxSize, config)
}

fun UriDrawable(uri: Uri, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): BitmapDrawable? {
	val bmp = BmpUtil.fromUri(uri, maxSize, config) ?: return null
	return BitmapDrawable(App.getResources(), bmp)
}

fun AssetBitmap(name: String, state: State): Bitmap? {
	return AssetImage.bitmap(name, state)
}

fun AssetBitmap(name: String): Bitmap? {
	return AssetImage.bitmap(name)
}

fun AssetDrawable(name: String): BitmapDrawable? {
	return AssetImage.drawable(name)
}

fun AssetDrawable(name: String, state: State): BitmapDrawable? {
	return AssetImage.drawable(name, state)
}
fun AssetDrawable(name: String, state: Boolean): Drawable? {
	return AssetImage.drawable(name, state)
}
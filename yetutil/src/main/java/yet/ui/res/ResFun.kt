package yet.ui.res

import android.graphics.Bitmap
import android.graphics.drawable.*
import android.net.Uri
import android.support.annotation.*
import net.yet.R
import yet.util.BmpUtil
import yet.util.app.App
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
	return Img.res(resId)
}

fun FileBitmap(file: File, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
	return BmpUtil.fromFile(file, maxSize, config)
}

fun FileDrawable(file: File, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): BitmapDrawable? {
	val bmp = BmpUtil.fromFile(file, maxSize, config) ?: return null
	return BitmapDrawable(App.resource, bmp)
}

fun UriBitmap(uri: Uri, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
	return BmpUtil.fromUri(uri, maxSize, config)
}

fun UriDrawable(uri: Uri, maxSize: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): BitmapDrawable? {
	val bmp = BmpUtil.fromUri(uri, maxSize, config) ?: return null
	return BitmapDrawable(App.resource, bmp)
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

fun DrawablePressed(normal:Drawable, pressed:Drawable): StateListDrawable {
	return ImageStated(normal).pressed(pressed).value
}
fun DrawablePressed(@DrawableRes normalId: Int, @DrawableRes pressedId: Int): StateListDrawable {
	return ImageStated(ResDrawable(normalId)).pressed(ResDrawable(pressedId)).value
}

fun DrawableSelected(@DrawableRes normalId: Int, @DrawableRes selectedId: Int): StateListDrawable {
	return ImageStated(ResDrawable(normalId)).selected(ResDrawable(selectedId)).value
}

fun DrawableFocused(@DrawableRes normalId: Int, @DrawableRes focusId: Int): StateListDrawable {
	return ImageStated(ResDrawable(normalId)).focused(ResDrawable(focusId)).value
}

fun DrawableDisabled(@DrawableRes normalId: Int, @DrawableRes disabledId: Int): StateListDrawable {
	return ImageStated(ResDrawable(normalId)).enabled(ResDrawable(disabledId), false).value
}

fun DrawableChecked(@DrawableRes normalId: Int, @DrawableRes checkedId: Int): StateListDrawable {
	return ImageStated(ResDrawable(normalId)).checked(ResDrawable(checkedId)).value
}

fun CheckBoxDrawable(): Drawable {
	return DrawableChecked(R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
}

fun DrawableSized(@DrawableRes resId: Int, size: Int): Drawable {
	return Img.resSized(resId,size)
}
//@file:JvmMultifileClass
//@file:JvmName("UIKit")
package net.yet.ui.ext

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.view.View
import net.yet.util.app.App
import java.io.Serializable

/**
 * Created by yet on 2015-11-20.
 */

val Context.defaultSharedPreferences: SharedPreferences
	get() = PreferenceManager.getDefaultSharedPreferences(this)

val Fragment.defaultSharedPreferences: SharedPreferences
	get() = PreferenceManager.getDefaultSharedPreferences(activity)

val Fragment.act: Activity
	get() = activity

val Fragment.ctx: Context
	get() = activity

val Context.ctx: Context
	get() = this

val Activity.act: Activity
	get() = this

inline fun <reified T : View> View.find(id: Int): T = findViewById(id) as T
inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T
inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T

inline fun <reified T : View> View.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Activity.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Fragment.findOptional(id: Int): T? = view?.findViewById(id) as? T

fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any>): T {
	arguments = bundleOf(*params)
	return this
}

fun bundleOf(vararg params: Pair<String, Any>): Bundle {
	val b = Bundle()
	for (p in params) {
		val (k, v) = p
		when (v) {
			is Boolean -> b.putBoolean(k, v)
			is Byte -> b.putByte(k, v)
			is Char -> b.putChar(k, v)
			is Short -> b.putShort(k, v)
			is Int -> b.putInt(k, v)
			is Long -> b.putLong(k, v)
			is Float -> b.putFloat(k, v)
			is Double -> b.putDouble(k, v)
			is String -> b.putString(k, v)
			is CharSequence -> b.putCharSequence(k, v)
			is Parcelable -> b.putParcelable(k, v)
			is Serializable -> b.putSerializable(k, v)
			is BooleanArray -> b.putBooleanArray(k, v)
			is ByteArray -> b.putByteArray(k, v)
			is CharArray -> b.putCharArray(k, v)
			is DoubleArray -> b.putDoubleArray(k, v)
			is FloatArray -> b.putFloatArray(k, v)
			is IntArray -> b.putIntArray(k, v)
			is LongArray -> b.putLongArray(k, v)
			is Array<*> -> {
				@Suppress("UNCHECKED_CAST")
				when {
					v.isArrayOf<Parcelable>() -> b.putParcelableArray(k, v as Array<out Parcelable>)
					v.isArrayOf<CharSequence>() -> b.putCharSequenceArray(k, v as Array<out CharSequence>)
					v.isArrayOf<String>() -> b.putStringArray(k, v as Array<out String>)
					else -> throw IllegalArgumentException("Unsupported bundle component (${v.javaClass})")
				}
			}
			is ShortArray -> b.putShortArray(k, v)
			is Bundle -> b.putBundle(k, v)
			else -> throw IllegalArgumentException("Unsupported bundle component (${v.javaClass})")
		}
	}

	return b
}


val Context.displayMetrics: android.util.DisplayMetrics
	get() = resources.displayMetrics

val Context.configuration: android.content.res.Configuration
	get() = resources.configuration

val android.content.res.Configuration.portrait: Boolean
	get() = orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

val android.content.res.Configuration.landscape: Boolean
	get() = orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

//val android.content.res.Configuration.long: Boolean
//    get() = (screenLayout and android.content.res.Configuration.SCREENLAYOUT_LONG_YES) != 0


//--------------dp------------

fun Context.dp(dp: Int): Int {
	val v = dp * displayMetrics.density + 0.5f
	return v.toInt()
}

fun Context.sp(sp: Int): Float {
	return sp * displayMetrics.scaledDensity + 0.5f
}

fun dp(dp: Int): Int {
	return App.dp2px(dp)
}

fun dp2(dp: Int?): Int? {
	if (dp != null) {
		return App.dp2px(dp)
	}
	return null
}

fun sp(sp: Int): Float {
	return sp * App.get().displayMetrics.scaledDensity + 0.5f
}

fun px2dp(px: Int): Int {
	return App.px2dp(px)
}


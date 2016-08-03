package net.yet.ui.ext

import android.widget.ImageView

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


inline fun <reified T : ImageView> T.scaleCenterInside(): T {
	this.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return this
}

inline fun <reified T : ImageView> T.scaleFitXY(): T {
	this.scaleType = ImageView.ScaleType.FIT_XY
	return this
}

inline fun <reified T : ImageView> T.scaleFitCenter(): T {
	this.scaleType = ImageView.ScaleType.FIT_CENTER
	return this
}
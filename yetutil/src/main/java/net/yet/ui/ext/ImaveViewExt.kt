package net.yet.ui.ext

import android.widget.ImageView

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

fun <T : ImageView> T.scaleCenter(): T {
	this.scaleType = ImageView.ScaleType.CENTER
	return this
}

fun <T : ImageView> T.scaleCenterInside(): T {
	this.scaleType = ImageView.ScaleType.CENTER_INSIDE
	return this
}

fun <T : ImageView> T.scaleCenterCrop(): T {
	this.scaleType = ImageView.ScaleType.CENTER_CROP
	return this
}


fun <T : ImageView> T.scaleFitXY(): T {
	this.scaleType = ImageView.ScaleType.FIT_XY
	return this
}

fun <T : ImageView> T.scaleFitCenter(): T {
	this.scaleType = ImageView.ScaleType.FIT_CENTER
	return this
}

fun <T : ImageView> T.scaleFitStart(): T {
	this.scaleType = ImageView.ScaleType.FIT_START
	return this
}

fun <T : ImageView> T.scaleFitEnd(): T {
	this.scaleType = ImageView.ScaleType.FIT_END
	return this
}
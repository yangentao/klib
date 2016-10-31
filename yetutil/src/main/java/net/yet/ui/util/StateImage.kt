package net.yet.ui.util

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable

/**
 * Created by entaoyang@163.com on 16/6/3.
 */

class StateImage(val normal: Drawable) {
	private var lu = DrawableListUtil()

	val value: StateListDrawable get() {
		return this.get()
	}

	constructor(colorNormal: Int) : this(ColorDrawable(colorNormal)) {

	}


	fun selected(color: Int, selected: Boolean = true): StateImage {
		return selected(ColorDrawable(color), selected)
	}

	fun selected(d: Drawable?, selected: Boolean = true): StateImage {
		if (d == null) {
			return this
		}
		lu.addDrawable(d, if (selected) android.R.attr.state_selected else -android.R.attr.state_selected)
		return this
	}


	fun pressed(color: Int, pressed: Boolean = true): StateImage {
		return pressed(ColorDrawable(color), pressed)
	}

	fun pressed(d: Drawable?, pressed: Boolean = true): StateImage {
		if (d == null) {
			return this
		}
		lu.addDrawable(d, if (pressed) android.R.attr.state_pressed else -android.R.attr.state_pressed)
		return this
	}

	fun disabled(color:Int):StateImage {
		return enabled(color, false)
	}

	fun enabled(color: Int, enabled: Boolean = true): StateImage {
		return enabled(ColorDrawable(color), enabled)
	}

	fun disabled(d:Drawable?):StateImage {
		return enabled(d, false)
	}

	fun enabled(d: Drawable?, enabled: Boolean = true): StateImage {
		if (d == null) {
			return this
		}
		lu.addDrawable(d, if (enabled) android.R.attr.state_enabled else -android.R.attr.state_enabled)
		return this
	}


	fun checked(color: Int, checked: Boolean = true): StateImage {
		return checked(ColorDrawable(color), checked)
	}

	fun checked(d: Drawable?, checked: Boolean = true): StateImage {
		if (d == null) {
			return this
		}
		lu.addDrawable(d, if (checked) android.R.attr.state_checked else -android.R.attr.state_checked)
		return this
	}


	fun focused(color: Int, focused: Boolean = true): StateImage {
		return focused(ColorDrawable(color), focused)
	}

	fun focused(d: Drawable?, focused: Boolean = true): StateImage {
		if (d == null) {
			return this
		}
		lu.addDrawable(d, if (focused) android.R.attr.state_focused else -android.R.attr.state_focused)
		return this
	}


	fun get(): StateListDrawable {
		lu.addDrawable(normal)
		return lu.get()
	}

}
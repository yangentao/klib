package yet.ui.util

import android.content.res.ColorStateList

/**
 * Created by entaoyang@163.com on 16/6/3.
 */

class StateColor(val normal: Int) {
	private var lu = ColorListUtil()

	val value: ColorStateList get() {
		return this.get()
	}


	fun selected(c: Int, selected: Boolean = true): StateColor {
		lu.addColor(c, if (selected) android.R.attr.state_selected else -android.R.attr.state_selected)
		return this
	}

	fun pressed(c: Int, pressed: Boolean = true): StateColor {
		lu.addColor(c, if (pressed) android.R.attr.state_pressed else -android.R.attr.state_pressed)
		return this
	}

	fun enabled(c: Int, enabled: Boolean = true): StateColor {
		lu.addColor(c, if (enabled) android.R.attr.state_enabled else -android.R.attr.state_enabled)
		return this
	}

	fun checked(c: Int, checked: Boolean = true): StateColor {
		lu.addColor(c, if (checked) android.R.attr.state_checked else -android.R.attr.state_checked)
		return this
	}

	fun focused(c: Int, focused: Boolean = true): StateColor {
		lu.addColor(c, if (focused) android.R.attr.state_focused else -android.R.attr.state_focused)
		return this
	}

	fun get(): ColorStateList {
		lu.addColor(normal)
		return lu.get()!!
	}


}

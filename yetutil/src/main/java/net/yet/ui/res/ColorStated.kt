package net.yet.ui.res

import android.content.res.ColorStateList
import net.yet.util.log.xlog

/**
 * Created by entaoyang@163.com on 2016-10-31.
 */


class ColorStated(val normal: Int) {
	private val colors = IntArray(10)
	private val states = arrayOfNulls<IntArray>(10)
	private var index = 0

	val value: ColorStateList get() {
		return this.get()
	}

	fun get(): ColorStateList {
		addColor(normal)
		return make()!!
	}

	private fun addColor(color: Int?, vararg states: Int) {
		if (color != null) {
			if (index >= 10) {
				xlog.e("max color num is 10")
				return
			}
			colors[index] = color
			this.states[index] = states
			++index
		}
	}

	private fun make(): ColorStateList? {
		if (index <= 0) {
			return null
		}
		val a = Array<IntArray>(index) {
			states[it]!!
		}
		val b = IntArray(index) {
			colors[it]
		}
		return ColorStateList(a, b)
	}


	fun selected(c: Int, selected: Boolean = true): ColorStated {
		addColor(c, if (selected) android.R.attr.state_selected else -android.R.attr.state_selected)
		return this
	}

	fun pressed(c: Int, pressed: Boolean = true): ColorStated {
		addColor(c, if (pressed) android.R.attr.state_pressed else -android.R.attr.state_pressed)
		return this
	}

	fun disabled(c: Int): ColorStated {
		return enabled(c, false)
	}

	fun enabled(c: Int, enabled: Boolean = true): ColorStated {
		addColor(c, if (enabled) android.R.attr.state_enabled else -android.R.attr.state_enabled)
		return this
	}

	fun checked(c: Int, checked: Boolean = true): ColorStated {
		addColor(c, if (checked) android.R.attr.state_checked else -android.R.attr.state_checked)
		return this
	}

	fun focused(c: Int, focused: Boolean = true): ColorStated {
		addColor(c, if (focused) android.R.attr.state_focused else -android.R.attr.state_focused)
		return this
	}


}

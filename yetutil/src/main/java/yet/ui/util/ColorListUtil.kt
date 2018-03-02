package yet.ui.util

import android.content.res.ColorStateList
import yet.util.log.xlog
import java.util.*

class ColorListUtil {
	private val colors = IntArray(10)
	private val states = arrayOfNulls<IntArray>(10)

	private var index = 0

	//add if color != null
	fun addColor(color: Int?, vararg states: Int) {
		if (color != null) {
			if (index >= 10) {
				xlog.e("max color num is 10")
				return
			}
			colors[index] = color.toInt()
			this.states[index] = states
			++index
		}
	}

	fun get(): ColorStateList? {
		if (index <= 0) {
			return null
		}
		val ss = arrayOfNulls<IntArray>(index)
		for (i in 0..index - 1) {
			ss[i] = states[i]
		}
		return ColorStateList(ss, Arrays.copyOf(colors, index))
	}
}

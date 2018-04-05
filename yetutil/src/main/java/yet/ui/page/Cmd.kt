package yet.ui.page

import android.view.View
import yet.ui.ext.HeightWrap
import yet.ui.ext.LParam
import yet.ui.ext.WidthFill

class Cmd(val cmd: String) {
	lateinit var view: View
	val param = LParam.WidthFill.HeightWrap

	var clickable = true
	var onClick: (Cmd) -> Unit = {}

	companion object {
		const val Sep = "_sep_"

	}
}
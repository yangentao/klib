package net.yet.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import net.yet.theme.Colors
import net.yet.ui.ext.createLinearVertical
import net.yet.ui.ext.fill
import net.yet.ui.ext.linearParam
import net.yet.ui.ext.set

// vertical
class LinearFragment : BaseFragment() {
	private var _rootView: LinearLayout? = null
	val rootView: LinearLayout get() = _rootView!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
		_rootView = this.createLinearVertical()
		linearParam().fill().set(rootView)
		rootBackColorWhite()
		return rootView
	}

	fun rootBackColorWhite() {
		rootView.setBackgroundColor(Color.WHITE)
	}

	fun rootBackColorPage() {
		rootView.setBackgroundColor(Colors.PageGray)
	}
}

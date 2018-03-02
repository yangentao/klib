package yet.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import yet.theme.Colors
import yet.ui.ext.createLinearVertical
import yet.ui.ext.fill
import yet.ui.ext.linearParam
import yet.ui.ext.set

// vertical
open class LinearFragment : BaseFragment() {
	lateinit  var rootView: LinearLayout

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		rootView = this.createLinearVertical()
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

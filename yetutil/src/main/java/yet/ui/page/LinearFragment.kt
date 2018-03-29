package yet.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.viewcreator.createLinearVertical

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

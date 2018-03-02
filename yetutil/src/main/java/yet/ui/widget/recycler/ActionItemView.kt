package yet.ui.widget.recycler

import android.view.View

/**
 * Created by yet on 2015/10/28.
 */
interface ActionItemView {

	val actionCount: Int

	fun getActionView(index: Int): View

}
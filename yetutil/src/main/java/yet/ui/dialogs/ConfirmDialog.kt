package yet.ui.dialogs

import android.content.Context
import yet.theme.Str

/**
 * Created by entaoyang@163.com on 2017-01-04.
 */

class ConfirmDialog : BaseDialog() {
	init {
		okRisk().ok(Str.OK).cancel(Str.CANCEL)
	}

	fun show(context: Context, title: String?, msg: String) {
		title(title)
		show(context, createMessageView(context, msg))
	}
}

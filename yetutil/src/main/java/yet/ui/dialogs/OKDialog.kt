package yet.ui.dialogs

import android.content.Context
import yet.theme.Str

class OKDialog : BaseDialog() {
	init {
		cancel(null).ok(Str.OK)
	}

	fun show(context: Context, title: String?, msg: String) {
		title(title)
		show(context, createMessageView(context, msg))
	}

	fun show(context: Context, msg: String) {
		show(context, createMessageView(context, msg))
	}
}

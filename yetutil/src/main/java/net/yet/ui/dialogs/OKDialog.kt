package net.yet.ui.dialogs

import net.yet.theme.Str

class OKDialog : CustomDialog() {
	init {
		cancel(null).ok(Str.OK)
	}
}

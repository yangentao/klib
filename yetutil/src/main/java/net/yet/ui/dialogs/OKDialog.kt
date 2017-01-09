package net.yet.ui.dialogs

import net.yet.theme.Str

class OKDialog : MyDialog() {
	init {
		cancel(null).ok(Str.OK)
	}
}

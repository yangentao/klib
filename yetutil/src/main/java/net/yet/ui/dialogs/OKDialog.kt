package net.yet.ui.dialogs

import net.yet.theme.Str

class OKDialog : TitleMsgDialog() {
	init {
		cancel(null).ok(Str.OK)
	}
}

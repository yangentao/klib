package net.yet.ui.dialogs

import net.yet.theme.Str

/**
 * Created by entaoyang@163.com on 2017-01-04.
 */

class ConfirmDialog : TitleMsgDialog() {
	init {
		risk().ok(Str.OK).cancel(Str.CANCEL)
	}
}

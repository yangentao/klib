package yet.ui.ext

import android.widget.EditText
import yet.theme.Str
import yet.ui.res.ResConst

/**
 * Created by entaoyang@163.com on 2017-01-06.
 */

fun <T : EditText> T.styleSearch(): T {
	this.textSizeB().backDrawable(ResConst.inputSearch())
	this.padding(15, 2, 15, 2)
	this.hint(Str.SEARCH)
	return this
}
package net.yet.ui.ext

import android.widget.TableLayout
import android.widget.TableRow

/**
 * Created by entaoyang@163.com on 2016-08-03.
 */

fun tableParam():TableLayout.LayoutParams {
	return TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
}
fun rowParam():TableRow.LayoutParams {
	return TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
}
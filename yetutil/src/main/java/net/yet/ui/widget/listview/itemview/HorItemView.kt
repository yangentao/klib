package net.yet.ui.widget.listview.itemview

import android.content.Context
import android.graphics.Color
import android.widget.LinearLayout
import net.yet.theme.Colors
import net.yet.ui.ext.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

open class HorItemView(context: Context) : LinearLayout(context) {
    var positionBind = 0

    init {
        genId()
        horizontal().gravityCenterVertical().padding(20, 10, 20, 5).backColor(Color.WHITE, Colors.Fade)
    }

}
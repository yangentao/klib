package net.yet.ui.widget

import android.graphics.Color
import net.yet.theme.Colors

/**
 * Created by entaoyang@163.com on 16/4/28.
 */

data class TabBarStyle(
		var textColor: Int = Colors.TextColorMinor,
		var textPressedColor: Int = Colors.Fade,
		var textSelectedColor: Int = Colors.Fade,
		var backColor: Int = Color.WHITE,
		var backPressedColor: Int = Colors.Fade,
		var backSelectedColor: Int = Color.WHITE,
		var lineColor: Int = Color.LTGRAY,
		var imageSize: Int = 24
)
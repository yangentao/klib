package yet.ui.widget

import android.graphics.Color
import yet.theme.Colors

/**
 * Created by entaoyang@163.com on 16/4/28.
 */

class TabBarStyle(
		var textColor: Int = Colors.TextColorMinor,
		var textPressedColor: Int = TabBarStyle.TextPressedColor,
		var textSelectedColor: Int = TabBarStyle.TextSelectedColor,
		var backColor: Int = Color.WHITE,
		var backPressedColor: Int = Colors.Fade,
		var backSelectedColor: Int = Color.WHITE,
		var lineColor: Int = Color.LTGRAY,
		var imageSize: Int = 24
) {
	companion object {
		var TextPressedColor: Int = Colors.Theme
		var TextSelectedColor: Int = Colors.Theme
	}
}
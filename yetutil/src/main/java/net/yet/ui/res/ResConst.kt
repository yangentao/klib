package net.yet.ui.res

import android.graphics.Color
import android.graphics.drawable.Drawable
import net.yet.R
import net.yet.ext.size
import net.yet.theme.Colors
import net.yet.theme.Dim
import net.yet.theme.InputSize
import net.yet.ui.util.RectDrawable
import net.yet.ui.util.ShapeUtil
import net.yet.ui.widget.EditTextX

/**
 * Created by yet on 2015/10/16.
 */
object ResConst {

	fun editClear(): Drawable {
		return Res.image(R.drawable.edit_clear).size(EditTextX.IMAGE_WIDTH)
	}

	fun back(): Drawable {
		return Res.drawable(R.drawable.back).size(Dim.iconSize)
	}

	fun arrowRight(): Drawable {
		return Res.drawable(R.drawable.arrow_right).size(Dim.iconSizeMin)
	}

	fun checkbox(): Drawable {
		return ImageStated(Res.drawable(R.drawable.checkbox)).checked(Res.drawable(R.drawable.checkbox_checked)).value.size(15)
//		return Img.namedStates("checkbox", true)
	}

	fun redPoint(): Drawable {
		return ShapeUtil.oval(10, Color.rgb(255, 128, 0))
	}

	fun input(): Drawable {
		val normal = RectDrawable(Colors.WHITE).corner(InputSize.EditCorner).stroke(1, Colors.GRAY).value
		val focused = RectDrawable(Colors.WHITE).corner(InputSize.EditCorner).stroke(2, Colors.EditFocus).value
		return ImageStated(normal).focused(focused, true).get()
	}

	fun greenButton(corner: Int = InputSize.ButtonCorner): Drawable {
		return colorButton(Colors.Safe, corner)
	}

	fun redButton(corner: Int = InputSize.ButtonCorner): Drawable {
		return colorButton(Colors.RedMajor, corner)
	}

	fun whiteButton(corner: Int = InputSize.ButtonCorner): Drawable {
		return colorButton(Colors.WHITE, corner)
	}

	fun colorButton(color: Int, corner: Int = InputSize.ButtonCorner): Drawable {
		val normal = RectDrawable(color).corner(corner).value
		val pressed = RectDrawable(Colors.Fade).corner(corner).value
		val enable_false = RectDrawable(Colors.Disabled).corner(corner).value
		return ImageStated(normal).pressed(pressed, true).enabled(enable_false, false).get()
	}
}

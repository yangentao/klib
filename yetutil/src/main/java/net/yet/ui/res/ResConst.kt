package net.yet.ui.res

import android.graphics.Color
import android.graphics.drawable.Drawable
import net.yet.theme.Colors
import net.yet.theme.Dim
import net.yet.theme.InputSize
import net.yet.ui.util.RectDrawable
import net.yet.ui.util.ShapeUtil
import net.yet.ui.util.StateImage

/**
 * Created by yet on 2015/10/16.
 */
object ResConst {

	fun back(): Drawable {
		return Img.namedStatesSize("back", false, Dim.iconSize)
	}

	fun arrowRight(): Drawable {
		return Img.namedStatesSize("arrow_right", false, Dim.iconSizeMin)
	}

	fun checkbox(): Drawable {
		return Img.namedStates("checkbox", true)
	}

	fun redPoint(): Drawable {
		return ShapeUtil.oval(10, Color.rgb(255, 128, 0))
	}

	fun input(): Drawable {
		val normal = RectDrawable(Colors.WHITE).corner(InputSize.EditCorner).stroke(1, Colors.GRAY).value
		val focused = RectDrawable(Colors.WHITE).corner(InputSize.EditCorner).stroke(2, Colors.EditFocus).value
		return StateImage(normal).focused(focused, true).get()
	}

	fun greenButton(corner: Int =  InputSize.ButtonCorner): Drawable {
		return colorButton(Colors.Safe, corner)
	}

	fun redButton(corner: Int =  InputSize.ButtonCorner): Drawable {
		return colorButton(Colors.RedMajor, corner)
	}

	fun whiteButton(corner: Int =  InputSize.ButtonCorner): Drawable {
		return colorButton(Colors.WHITE, corner)
	}

	fun colorButton(color: Int, corner: Int = InputSize.ButtonCorner): Drawable {
		val normal = RectDrawable(color).corner(corner).value
		val pressed = RectDrawable(Colors.Fade).corner(corner).value
		val enable_false = RectDrawable(Colors.Disabled).corner(corner).value
		return StateImage(normal).pressed(pressed, true).enabled(enable_false, false).get()
	}
}

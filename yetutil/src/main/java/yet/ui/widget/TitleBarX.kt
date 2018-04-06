package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.page.Cmd
import yet.ui.res.D
import yet.ui.res.Res
import yet.ui.res.sized
import yet.ui.res.tintedWhite
import yet.ui.viewcreator.createImageView
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createTextViewA


class TitleBarX(context: Context) : RelativeLayout(context) {
	val leftCmds = ArrayList<Cmd>()
	val rightCmds = ArrayList<Cmd>()
	val menuItems = ArrayList<Cmd>()
	var titleView: View? = null
	var titleCenter = TitleBarX.TitleCenter

	init {
		backColor(Colors.Theme)
	}

	fun build() {
		removeAllViews()
		var leftLinear: LinearLayout? = null
		if (leftCmds.isNotEmpty()) {
			val ll = this.createLinearHorizontal()
			for (c in leftCmds) {
				ll.addView(c.view, c.param)
			}
			addView(ll, RParam.ParentLeft.HeightFill.WidthWrap)
			leftLinear = ll
		}
		var rightLinear: LinearLayout? = null
		if (rightCmds.isNotEmpty()) {
			val ll = this.createLinearHorizontal()
			for (c in rightCmds) {
				ll.addView(c.view, c.param)
			}
			addView(ll, RParam.ParentRight.HeightFill.WidthWrap)
			rightLinear = ll
		}
		val v = titleView
		if (v != null) {
			if (titleCenter) {
				addView(v, RParam.CenterInParent.HeightFill.WidthWrap)
			} else {
				if (leftLinear == null) {
					addView(v, RParam.CenterVertical.ParentLeft.HeightFill.WidthWrap.margins(15, 0, 0, 0))
				} else {
					addView(v, RParam.CenterVertical.toRightOf(leftLinear).HeightFill.WidthWrap.margins(15, 0, 0, 0))
				}
			}
		}

	}

	fun title(text: String) {
		val tv = createTextViewA()
		tv.textColorWhite()
		tv.text = text
		titleView = tv
	}

	fun titleImage(@DrawableRes resId: Int) {
		val iv = createImageView()
		iv.setImageResource(resId)
		iv.scaleCenterCrop()
		titleView = iv
	}

	fun showBack(): Cmd {
		leftCmds.removeAll { it.cmd == BACK }
		val iv = createImageItemView()
		iv.setImageResource(Res.back)
		val c = Cmd(BACK)
		c.view = iv
		c.param = LParam.size(HEIGHT)
		iv.setOnClickListener {
			c.onClick(c)
		}
		leftCmds.add(c)
		return c
	}

	fun imageCmd(cmd: String, resId: Int): Cmd {
		return imageCmd(cmd, Res.drawable(resId))
	}

	fun imageCmd(cmd: String, d: Drawable): Cmd {
		val iv = createImageItemView()
		iv.setImageDrawable(d.tintedWhite)
		val c = Cmd(cmd)
		c.view = iv
		c.param = LParam.heightFill().width(HEIGHT)
		iv.setOnClickListener {
			c.onClick(c)
		}
		rightCmds.add(c)
		return c
	}

	fun textCmd(cmd: String, text: String): Cmd {
		val tv = createTextItemView()
		tv.text = text
		val c = Cmd(cmd)
		c.view = tv
		c.param = LParam.HeightFill.WidthWrap
		tv.setOnClickListener {
			c.onClick(c)
		}
		rightCmds.add(c)
		return c
	}

	fun menu(block: () -> Unit) {
		imageCmd(MENU, Res.menu)
		block()
	}

	fun menuItem(cmd: String, text: String, iconRes: Int): Cmd {
		if (iconRes != 0) {
			return menuItem(cmd, text, Res.drawable(iconRes))
		} else {
			return menuItem(cmd, text, null)
		}

	}

	fun menuItem(cmd: String, text: String, icon: Drawable?): Cmd {
		val tv = context.createTextViewA()
		tv.singleLine()
		tv.gravityLeftCenter().padding(5, 5, 20, 5)
		tv.text = text
		var d = icon
		if (d == null) {
			d = D.color(Color.TRANSPARENT)
		}
		d = d.sized(ImgSize)
		tv.compoundDrawablePadding = dp(10)
		tv.setCompoundDrawables(d, null, null, null)
		tv.textColorWhite()

		val c = Cmd(cmd)
		c.view = tv
		tv.setOnClickListener {
			c.onClick(c)
		}
		return c
	}

	fun createImageItemView(): ImageView {
		val iv = ImageView(context)
		iv.scaleCenterCrop()
		iv.backColorTransFade()
		iv.padding(PAD_HOR, PAD_VER, PAD_HOR, PAD_VER)
		return iv
	}

	fun createTextItemView(): TextView {
		val iv = createTextViewA()
		iv.backColorTransFade()
		iv.textColorWhite()
		iv.gravityCenter()
		iv.minimumWidth = dp(HEIGHT)
		return iv
	}


	companion object {
		const val BACK = "back"
		const val MENU = "menu"
		const val ImgSize = 24
		const val HEIGHT = 50// dp
		const val PAD_VER = (HEIGHT - ImgSize) / 2
		const val PAD_HOR = (HEIGHT - ImgSize) / 2
		var TitleCenter = true
	}
}
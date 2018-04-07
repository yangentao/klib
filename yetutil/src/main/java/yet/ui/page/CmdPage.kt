package yet.ui.page

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.widget.Button
import android.widget.LinearLayout
import yet.ui.ext.*
import yet.ui.viewcreator.*
import yet.ui.widget.listview.itemview.TextDetailView
import yet.ui.widget.listview.itemview.TextItemView

abstract class CmdPage : TitlePageX() {
	var divider: Divider = Divider()
	val cmdList = ArrayList<Cmd>()
	lateinit var cmdPanel: LinearLayout

	var defaultItemHeight: Int = 50
	var defaultIconSize: Int = 32

	init {
		enableContentScroll = true
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		cmdPanel = createLinearVertical()
		contentView.addView(cmdPanel, LParam.WidthFill.HeightWrap)
		cmdPanel.setBackgroundColor(Color.WHITE)
		cmdPanel.divider(divider)
	}

	override fun onContentCreated() {
		super.onContentCreated()
		commit()
	}

	fun commit() {
		cmdPanel.removeAllViews()
		for (c in cmdList) {
			cmdPanel.addView(c.view, c.param)
			c.view.tag = c
			if (c.clickable) {
				c.view.onClick {
					val cc = it.tag as Cmd
					cc.onClick(cc)
				}
			}
		}
	}

	fun textItemView(block: TextItemView.() -> Unit): TextItemView {
		val v = TextItemView(activity)
		v.backColorWhiteFade()

		v.block()
		return v
	}

	fun textDetailItemView(block: TextDetailView.() -> Unit): TextDetailView {
		val v = TextDetailView(activity)
		v.backColorWhiteFade()
		v.block()
		return v
	}

	fun buttonItemView(block: Button.() -> Unit): Button {
		val b = createButton()
		b.block()
		return b
	}

	fun findCmd(cmd: String): Cmd? {
		return cmdList.find { it.cmd == cmd }
	}

	fun cmd(cmd: String, block: Cmd.() -> Unit): Cmd {
		val a = Cmd(cmd)
		cmdList.add(a)
		a.block()
		return a
	}

	fun cmdTextView(cmd: String, block: TextItemView.() -> Unit): Cmd {
		return cmd(cmd) {
			view = textItemView {
				minimumHeight = dp(defaultItemHeight)
				this.block()
			}
		}
	}

	fun cmdText(text: String, @DrawableRes leftIcon: Int = 0, @DrawableRes rightIcon: Int = 0, rightSize: Int = 16, cmd: String = text): Cmd {
		val c = cmdTextView(cmd) {
			this.text = text
			if (leftIcon != 0) {
				this.leftImage(leftIcon, defaultIconSize)
			}
			if (rightIcon != 0) {
				this.rightImage(rightIcon, rightSize)
			}
		}
		return c
	}

	fun sep(sepHeight: Int = 10): CmdPage {
		val a = Cmd(Cmd.Sep)
		a.clickable = false
		a.param.height(sepHeight)
		a.view = createView().backColor(divider.color)
		cmdList.add(a)
		return this
	}


}

package yet.ui.page

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.LinearLayout
import yet.ui.ext.*
import yet.ui.viewcreator.createButton
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createView
import yet.ui.widget.listview.itemview.TextDetailView
import yet.ui.widget.listview.itemview.TextItemView

abstract class CmdPage : TitlePageX() {
	var divider: Divider = Divider()
	val cmdList = ArrayList<Cmd>()
	lateinit var cmdPanel: LinearLayout

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


	fun sep(sepHeight: Int = 10): CmdPage {
		val a = Cmd(Cmd.Sep)
		a.clickable = false
		a.param.height(sepHeight)
		a.view = createView().backColor(divider.color)
		cmdList.add(a)
		return this
	}


}

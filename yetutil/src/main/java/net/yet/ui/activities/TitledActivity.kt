package net.yet.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import net.yet.ui.ext.createLinearVertical
import net.yet.ui.widget.TitleBar

/**
 * Created by entaoyang@163.com on 16/4/14.
 */

abstract class TitledActivity() : BaseActivity() {
	private var _rootView: LinearLayout? = null
	private var _titleBar: TitleBar? = null

	val rootView: LinearLayout get() = _rootView!!
	val titleBar: TitleBar get() = _titleBar!!

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_rootView = this.createLinearVertical()
		setContentView(_rootView)
		_titleBar = TitleBar(this)
		rootView.addView(titleBar)
		onCreateContent(rootView)
	}

	abstract fun onCreateContent(contentView: LinearLayout);
}
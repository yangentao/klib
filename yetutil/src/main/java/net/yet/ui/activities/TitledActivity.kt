package net.yet.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import net.yet.ui.ext.createLinearVertical
import net.yet.ui.widget.Action
import net.yet.ui.widget.TitleBar

/**
 * Created by entaoyang@163.com on 16/4/14.
 */

abstract class TitledActivity() : BaseActivity() {
	lateinit var rootView: LinearLayout
	lateinit var titleBar: TitleBar

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		rootView = this.createLinearVertical()
		setContentView(rootView)
		titleBar = TitleBar(this)
		rootView.addView(titleBar)
		titleBar.onAction = { bar, a ->
			onTitleBarAction(a)
		}
		titleBar.onActionNav = { bar, a ->
			onTitleBarActionNav(a)
		}
		onCreateContent(rootView)
		titleBar.commit()
	}

	open fun onTitleBarAction(action: Action) {

	}

	open fun onTitleBarActionNav(action: Action) {

	}

	open fun onCreateContent(contentView: LinearLayout) {

	}
}
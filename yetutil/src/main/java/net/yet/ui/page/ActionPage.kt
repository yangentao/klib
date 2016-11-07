package net.yet.ui.page

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import net.yet.ui.ext.*
import net.yet.ui.widget.Action
import net.yet.ui.widget.ActionSetView
import net.yet.ui.widget.add

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

/**
 * 用于数量较少的可点击行的页面， 比如设置页面

 * @author yangentao@gmail.com
 */
abstract class ActionPage : TitledPage() {
	lateinit var scrollView: ScrollView
	lateinit var scrollContentView: LinearLayout
	lateinit var actionSetView: ActionSetView

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		contentView.setBackgroundColor(Color.WHITE)
		scrollView = ScrollView(activity).genId().backColorPage()
		contentView.addViewParam(scrollView) { widthFill().heightDp(0).weight(1f) }

		scrollContentView = context.createLinearVertical()
		scrollView.addView(scrollContentView, layoutParam().widthFill().heightWrap())

		actionSetView = ActionSetView(context)
		actionSetView.padding(0, 0, 0, 1)
		actionSetView.onItemAction = {
			onPageAction(it)
		}
		addScrollItemView(actionSetView, actionViewParam())
	}

	override fun onContentCreated() {
		super.onContentCreated()
		commit()
	}


	fun addTailButton(text: String, onclick: (Button) -> Unit): Button {
		val tailButton = scrollContentView.createButton(text).themeRed(20)
		scrollContentView.addViewParam(tailButton) {
			widthDp(160).heightDp(40).gravityCenter().marginTB(20, 10)
		}
		tailButton.onClick {
			onclick.invoke(tailButton)
		}
		return tailButton
	}

	protected abstract fun onPageAction(action: Action)

	fun indexOfActionSetView(): Int {
		return scrollContentView.indexOfChild(actionSetView)
	}

	fun actionViewParam(): LinearLayout.LayoutParams {
		return linearParam().widthFill().heightWrap()
	}

	fun addScrollItemView(view: View, params: LinearLayout.LayoutParams) {
		scrollContentView.addView(view, params)
	}

	fun addScrollItemView(view: View, index: Int, params: LinearLayout.LayoutParams) {
		scrollContentView.addView(view, index, params)
	}

	fun findAction(tag: String): Action {
		return actionSetView.findTag(tag)!!
	}

	fun addAction(tag: String, block: (Action) -> Unit): Action {
		val a = Action(tag)
		block(a)
		actionSetView.add(a)
		return a
	}

	fun addAction(tag: String): Action {
		val a = Action(tag)
		actionSetView.add(a)
		return a
	}

	fun addActions(vararg actions: Action): ActionPage {
		actionSetView.add(*actions)
		return this
	}

	fun newGroup(): ActionPage {
		actionSetView.newGroup()
		return this
	}

	fun commit() {
		actionSetView.commit()
	}

	fun cleanActions() {
		actionSetView.cleanActions()
	}

}

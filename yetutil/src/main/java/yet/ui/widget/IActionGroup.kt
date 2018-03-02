package yet.ui.widget

import net.yet.R
import yet.util.Util
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/15.
 */


interface IActionGroup {
	var allActions: ArrayList<Action> get
	fun onActionUpdate(action: Action)

	val actionCount: Int
		get() = allActions.size

	val visibleAcitons: List<Action> get() = allActions.filter { !it.hidden }


	fun find(p: (Action) -> Boolean): Action? {
		allActions.forEach {
			if (p(it)) {
				return it
			}
			it.children.forEach { item ->
				if (p(item)) {
					return item
				}
			}
		}
		return null
	}

	fun findTag(tag: String): Action? {
		return find { it.isTag(tag) }
	}

	fun findMenuAction(): Action {
		for (a in allActions) {
			if (a.isTag(Action.MENU)) {
				return a
			}
		}
		val menuAction = Action(Action.MENU).icon(R.drawable.yet_menu)
		add(menuAction)
		return menuAction
	}
	fun addMenuItem(labelTag: String, block: (Action) -> Unit): Action {
		val m = findMenuAction()
		val a = m.addAction(labelTag)
		a.onAction = block
		return a
	}

	fun addMenuItem(labelTag: String): Action {
		val m = findMenuAction()
		return m.addAction(labelTag)
	}

	fun addMenuItem(action: Action): Action {
		val m = findMenuAction()
		m.add(action)
		return action
	}

	fun addMenuItems(vararg labelTags: String) {
		val m = findMenuAction()
		for (s in labelTags) {
			m.addAction(s)
		}
	}

	fun addMenuItems(vararg actions: Action) {
		val m = findMenuAction()
		for (a in actions) {
			m.add(a)
		}
	}


	fun addAction(labelTag: String): Action {
		val a = Action(labelTag)
		addAction(a)
		return a
	}
	fun addAction(labelTag: String, block: (Action) -> Unit): Action {
		val a = Action(labelTag)
		a.onAction = block
		addAction(a)
		return a
	}

	fun addActions(vararg labelTags: String) {
		for (s in labelTags) {
			addAction(s)
		}
	}

	fun addAction(action: Action) {
		action.onUpdate = {
			onActionUpdate(it)
		}
		allActions.add(action)
	}

}


fun  <T : IActionGroup> T.add(vararg ls: Action): T {
	add(Util.asList(*ls))
	return this
}

fun  <T : IActionGroup> T.add(ls: List<Action>): T {
	for (a in ls) {
		addAction(a)
	}
	return this
}

fun  <T : IActionGroup> T.add(index: Int, action: Action): T {
	allActions.add(index, action)
	action.onUpdate = {
		onActionUpdate(it)
	}
	return this
}


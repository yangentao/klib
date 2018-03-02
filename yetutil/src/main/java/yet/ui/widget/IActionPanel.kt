package yet.ui.widget

import android.view.View
import yet.ui.ext.gone
import yet.ui.ext.visiable
import yet.util.mergeAction

/**
 * actions , navAction, title改变后， 要调用updaeActions来刷新界面
 *
 *
 * startMode开启新的模式, startModeDefault则加载默认模式的数据. 默认模式create后自动创建
 * 如果指定的模式已经存在, 则会加载它. 可以通过empty来判断是否创建过:
 * bar.startMode("edit");
 * if(bar.empty()){
 * bar.addAction(...)
 * }
 * Created by yet on 2015/10/12.
 */
interface IActionPanel : IActionGroup {
	val actionPanelView: View get
	fun onRebuild()

	override fun onActionUpdate(action: Action) {
		rebuild()
	}

	fun cleanActions() {
		allActions.forEach {
			it.onAction = {}
		}
		allActions.clear()
		commit()
	}

	fun rebuild() {
		mergeAction("actionGroup.rebuild" + this::class.qualifiedName + this.hashCode(), 50) {
			onRebuild()
		}
	}


	fun hide() {
		actionPanelView.gone()
	}

	fun show() {
		actionPanelView.visiable()
	}

	fun commit() {
		rebuild()
	}
}



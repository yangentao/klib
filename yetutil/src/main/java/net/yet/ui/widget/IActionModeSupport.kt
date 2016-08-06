package net.yet.ui.widget

import net.yet.util.loge

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
interface IActionModeSupport : IActionPanel {
	var modeName: String

	fun onResotreDefault()
	fun onBackupDefault()
	fun onCleanData()

	fun pushMode(name: String) {
		if (isMode(name)) {
			loge("you already in mode $name")
			return
		}
		onBackupDefault()
		onCleanData()
		modeName = name
		commit()
	}

	fun popMode() {
		onCleanData()
		onResotreDefault()
		commit()
	}

	fun isMode(mode: String): Boolean {
		return modeName == mode
	}

	val isModeDefault: Boolean
		get() = modeName.isEmpty()

}

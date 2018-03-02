package yet.ui.widget

import yet.util.Progress

/**
 * Created by entaoyang@163.com on 16/6/18.
 */

class BarProgress(val bar: MyProgressBar) : Progress {
	override fun onStart(total: Int) {
		bar.postShow(100)
	}

	override fun onProgress(current: Int, total: Int, percent: Int) {
		bar.postProgress(percent)
	}

	override fun onFinish() {
		bar.postHide()
	}

}
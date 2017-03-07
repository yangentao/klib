package net.yet.ui.activities

import net.yet.R

/**
 * Created by entaoyang@163.com on 16/4/15.
 */
class AnimConf {
	var startEnter = R.anim.yet_slide_in_right
	var startExit = R.anim.yet_out_alpha
	var finishEnter = 0
	var finishExit = R.anim.yet_slide_out_right

	fun onStart(enter: Int, exit: Int): AnimConf {
		startEnter = enter
		startExit = exit
		return this
	}

	fun onFinish(enter: Int, exit: Int): AnimConf {
		finishEnter = enter
		finishExit = exit
		return this
	}
}

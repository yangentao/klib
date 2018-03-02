package yet.ui.util

import android.widget.TextView
import net.yet.R
import yet.ui.res.Res
import yet.util.RepeatCallback
import yet.util.TaskUtil
import yet.util.log.xlog
import java.util.*

/**
 * 用于验证码的倒计时
 * Created by yangentao on 2016-02-03.
 * entaoyang@163.com
 */
object TimeDown {

	private val map = HashMap<String, TextView>()
	private val secondsMap = HashMap<String, Int>()

	fun updateView(name: String, view: TextView) {
		map.put(name, view)
		if (secondsMap.containsKey(name)) {
			view.isEnabled = false
		} else {
			view.isEnabled = true
		}
	}

	//要在主线程调用
	fun startClick(name: String, secondsLimit: Int, view: TextView) {
		xlog.d("startClick ", name, secondsLimit)
		map.put(name, view)
		view.isEnabled = false
		if (!secondsMap.containsKey(name)) {
			secondsMap.put(name, secondsLimit)
			xlog.d("倒计时开始", name, secondsLimit)
			TaskUtil.repeatFore(secondsLimit, 1000, object : RepeatCallback() {

				override fun onRepeat(index: Int, value: Long): Boolean {
					val leftTimes = secondsLimit - index
					secondsMap.put(name, leftTimes)

					val v = map[name]
					if (v != null) {
						var s = "" + leftTimes
						s += Res.str(R.string.yet_retrive_again)
						v.text = s
					}
					return true
				}

				override fun onRepeatEnd() {
					secondsMap.remove(name)
					val v = map[name]
					if (v != null) {
						v.isEnabled = true
						v.text = Res.str(R.string.yet_retrive)
					}
				}
			})
		} else {
			xlog.d("重复的点击")
		}
	}
}

package net.yet.kutil.ui.activities

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import net.yet.ui.activities.BaseActivity
import net.yet.ui.ext.openActivity
import net.yet.ui.widget.ImageResPager
import net.yet.util.*

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


abstract class BaseWelcomeActivity : BaseActivity() {
	private var isGuide = false

	/**
	 * 欢迎页的图片

	 * @return
	 */
	protected abstract val resDrawable: Int

	/**
	 * 介绍页的图片, 只在第一次运行时展示

	 * @return
	 */
	protected abstract val resImages: IntArray

	/**
	 * 要跳转的下一个页面

	 * @return
	 */
	protected abstract val nextActivity: Class<out Activity>

	//毫秒
	open var minTime:Long = 0

	/**
	 * 进行app的初始化/预加载工作
	 */
	protected abstract fun onBackTask()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val first = Util.runOnceVer("ver-first-welcome", null)
		val images = resImages
		isGuide = first && Util.length(images) > 0
		if (isGuide) {
			val pager = ImageResPager(this)
			setContentView(pager)
			pager.setItems(Util.asList(*images))
			pager.onLastPageClick = {
				openActivity(nextActivity)
				finish()
			}
			return
		} else {
			val iv = ImageView(this)
			iv.setImageResource(resDrawable)
			iv.adjustViewBounds = true
			iv.scaleType = ImageView.ScaleType.FIT_XY
			this.setContentView(iv)
		}

	}

	override fun onResume() {
		super.onResume()
		back {
			val t = Tick()
			onBackTask()
			val delta = t.end("")
			if(delta < minTime) {
				sleep(minTime - delta)
			}
			fore {
				if (!isGuide) {
					openActivity(nextActivity)
					finish()
				}
			}
		}
	}

	override fun onBackPressed() {
		// 不可退出
	}
}

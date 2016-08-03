package net.yet.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import net.yet.R
import net.yet.util.*
import net.yet.util.app.App
import net.yet.util.event.EventMerge
import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

open class BaseActivity : AppCompatActivity(), MsgListener {
	private val eventMerges = ArrayList<EventMerge>()

	protected fun createMerge(millSec: Int): EventMerge {
		val em = EventMerge.delay(millSec)
		eventMerges.add(em)
		return em
	}

	protected fun listenMsg(vararg msgs: String) {
		for (m in msgs) {
			MsgCenter.listen(m, this)
		}
	}

	protected fun listenMsg(vararg clses: Class<*>) {
		for (c in clses) {
			MsgCenter.listen(c, this)
		}
	}

	override fun onMsg(msg: Msg) {
		xlog.d("Message:", msg)
	}


//    fun openActivity(activityClass: Class<out Activity>) {
//        startActivity(Intent(this, activityClass))
//    }


	override fun startActivity(intent: Intent) {
		super.startActivity(intent)
	}


	val findRootView: View
		get() = (findViewById(android.R.id.content) as ViewGroup).getChildAt(0)

	fun toast(text: String) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		this.setTheme(R.style.LibTheme_NoActionBar)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		xlog.dTag("yet", "onCreate:", this.toString())
		super.onCreate(savedInstanceState)
	}

	override fun onDestroy() {
		xlog.dTag("yet", "onDestroy:", this.toString())
		super.onDestroy()
		MsgCenter.remove(this)
		for (m in eventMerges) {
			m.clear()
		}
		eventMerges.clear()
	}


	override fun onResume() {
		xlog.dTag("yet", "onResume:", this.toString())
		super.onResume()
	}

	override fun onPause() {
		xlog.dTag("yet", "onPause:", this.toString())
		super.onPause()
	}

	override fun onStart() {
		visiableActivityCount += 1
		if (visiableActivityCount == 1) {
			val app = App.get()
			if (app != null && app is AppVisibleListener) {
				fore {
					app.onEnterForeground()
				}
			}
			Msg(MsgEnterForeground).fireCurrent()
		}
		xlog.dTag("yet", "onStart:", visiableActivityCount, this.toString())
		_topActivity = this
		super.onStart()
	}

	override fun onRestart() {
		xlog.dTag("yet", "onRestart:", this.toString())
		super.onRestart()
	}

	override fun onStop() {
		visiableActivityCount -= 1
		if (visiableActivityCount <= 0) {
			_topActivity = null
			val app = App.get()
			if (app != null && app is AppVisibleListener) {
				fore {
					app.onEnterBackground()
				}
			}
			Msg(MsgEnterBackground).fireCurrent()
		}
		xlog.dTag("yet", "onStop:", visiableActivityCount, this.toString())
		super.onStop()
	}

	companion object {
		val MsgEnterBackground = "enter_background"
		val MsgEnterForeground = "enter_foreground"
		var visiableActivityCount: Int = 0
			private set
		private var _topActivity: BaseActivity? = null

		val topVisibleActivity: BaseActivity? get() {
			if (visiableActivityCount > 0) {
				return _topActivity
			}
			return null
		}

	}

}

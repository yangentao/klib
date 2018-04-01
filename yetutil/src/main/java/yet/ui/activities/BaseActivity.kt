package yet.ui.activities

import android.app.Activity
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import yet.theme.Colors
import yet.ui.MyColor
import yet.ui.dialogs.OKDialog
import yet.util.*
import yet.util.app.App
import yet.util.app.Perm
import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

open class BaseActivity : Activity(), MsgListener, PermContext {
	val PERM_REQ = 79

	var fullScreen = false

	var permStack: Stack<Perm> = Stack()
	val watchMap = HashMap<Uri, ContentObserver>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.setTheme(net.yet.R.style.yetTheme)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		if (fullScreen) {
			setWindowFullScreen()
		}
		statusBarColorFromTheme()
		MsgCenter.listenAll(this)
	}
	fun setWindowFullScreen(){
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN)
	}


	fun unWatch(uri: Uri) {
		val ob = watchMap[uri]
		if (ob != null) {
			contentResolver.unregisterContentObserver(ob)
		}
	}


	fun watch(uri: Uri, block: (Uri) -> Unit = {}) {
		if (watchMap.containsKey(uri)) {
			return
		}
		val ob = object : ContentObserver(Handler(Looper.getMainLooper())) {

			override fun onChange(selfChange: Boolean, uri: Uri) {
				mergeAction("watchUri:$uri") {
					block(uri)
					onUriChanged(uri)
				}
			}
		}
		watchMap[uri] = ob
		contentResolver.registerContentObserver(uri, true, ob)
	}

	fun onUriChanged(uri: Uri) {

	}


	override fun getActivityContext(): Activity {
		return this
	}

	override fun reqPerm(req: Perm) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			req.checkSelf()
			if (req.denyPerms.isEmpty()) {
				req.onAllowed(false)
			} else {
				permStack.push(req)
				requestPermissions(req.denyPerms.toTypedArray(), PERM_REQ)
			}
		} else {
			req.onAllowed(true)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (requestCode == PERM_REQ) {
			if (permStack.isNotEmpty()) {
				val req = permStack.pop()
				req.checkSelf()
				if (req.denyPerms.isEmpty()) {
					req.onAllowed(false)
				} else {
					req.onDeny()
				}
			}
		}
	}


	fun statusBarColor(color: Int) {
		val w = window ?: return
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.statusBarColor = color
		}
	}

	fun statusBarColorFromTheme() {
		val c = MyColor(Colors.Theme)
		statusBarColor(c.multiRGB(0.7))
	}


	override fun onMsg(msg: Msg) {
		if (msg.isMsg(Pages.MSG_CLOSE_PAGE)) {
			if (this::class == msg.cls) {
				finish()
				return
			}
		}
	}

	fun alert(title: String, msg: String) {
		val dlg = OKDialog()
		dlg.show(this, title, msg)
	}

	fun alert(msg: String) {
		val dlg = OKDialog()
		dlg.show(this, msg)
	}

	override fun startActivity(intent: Intent) {
		super.startActivity(intent)
	}


	val findRootView: View
		get() = (findViewById(android.R.id.content) as ViewGroup).getChildAt(0)

	fun toast(text: String) {
		fore {
			Toast.makeText(this, text, Toast.LENGTH_LONG).show()
		}
	}


	override fun onDestroy() {
		super.onDestroy()
		MsgCenter.remove(this)
		for (ob in watchMap.values) {
			contentResolver.unregisterContentObserver(ob)
		}
		watchMap.clear()
	}


	override fun onResume() {
		super.onResume()
	}

	override fun onPause() {
		super.onPause()
	}

	override fun onStart() {
		visiableActivityCount += 1
		if (visiableActivityCount == 1) {
			val app = App.app
			if (app != null && app is AppVisibleListener) {
				fore {
					app.onEnterForeground()
				}
			}
			Msg(MsgEnterForeground).fireCurrent()
		}
		_topActivity = this
		super.onStart()
	}

	override fun onRestart() {
		super.onRestart()
	}

	override fun onStop() {
		visiableActivityCount -= 1
		if (visiableActivityCount <= 0) {
			_topActivity = null
			val app = App.app
			if (app is AppVisibleListener) {
				fore {
					app.onEnterBackground()
				}
			}
			Msg(MsgEnterBackground).fireCurrent()
		}
		super.onStop()
	}

	companion object {
		val MsgEnterBackground = "enter_background"
		val MsgEnterForeground = "enter_foreground"
		var visiableActivityCount: Int = 0
			private set
		private var _topActivity: BaseActivity? = null

		val topVisibleActivity: BaseActivity?
			get() {
				if (visiableActivityCount > 0) {
					return _topActivity
				}
				return null
			}

	}

}

package net.yet.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import net.yet.ui.dialogs.OKDialog
import net.yet.util.Msg
import net.yet.util.MsgCenter
import net.yet.util.MsgListener
import net.yet.util.app.App
import net.yet.util.app.OS
import net.yet.util.event.EventMerge
import net.yet.util.fore
import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

open class BaseActivity : AppCompatActivity(), MsgListener {
	private val eventMerges = ArrayList<EventMerge>()


	private val PERM_CODE = 58
	val reqPermSet = HashSet<String>()
	val grantedPermSet = HashSet<String>()
	val denyPermSet = HashSet<String>()
	private var permCallback: ((Set<String>) -> Unit)? = null

	fun statusBarColor(color: Int) {
		val w = window ?: return
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.statusBarColor = color
		}
	}


	//6.0之前返回false,
	fun hasPerm(p: String): Boolean {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return PackageManager.PERMISSION_GRANTED == checkSelfPermission(p)
		}
		return false
	}

	//检查权限, 返回获得的权限
	//6.0之前返回空集合
	fun checkPerm(ps: Collection<String>): Set<String> {
		val set = HashSet<String>(8)
		for (s in ps) {
			if (hasPerm(s)) {
				set.add(s)
			}
		}
		return set
	}

	//ps:要请求的权限android.Manifest.permision.xxx
	//block, 获得的权限
	fun reqPerm(ps: Collection<String>, block: (Set<String>) -> Unit) {
		if (ps.isEmpty() || !OS.GE60) {
			return
		}
		reqPermSet.clear()
		grantedPermSet.clear()
		denyPermSet.clear()
		reqPermSet.addAll(ps)
		grantedPermSet.addAll(checkPerm(reqPermSet))
		if (grantedPermSet.size == reqPermSet.size) {
			block(grantedPermSet)
			return
		}
		permCallback = block
		denyPermSet.addAll(reqPermSet - grantedPermSet)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(denyPermSet.toTypedArray(), PERM_CODE)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == requestCode) {
			for (i in permissions.indices) {
				val p = permissions[i]
				val ok = grantResults[i] == PackageManager.PERMISSION_GRANTED
				if (ok) {
					denyPermSet.remove(p)
					grantedPermSet.add(p)
				}
			}
			val c = permCallback
			permCallback = null
			c?.invoke(grantedPermSet)
		}
	}

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

	override fun onCreate(savedInstanceState: Bundle?) {
		this.setTheme(net.yet.R.style.yetTheme_NoActionBar)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		super.onCreate(savedInstanceState)
	}

	override fun onDestroy() {
		super.onDestroy()
		MsgCenter.remove(this)
		for (m in eventMerges) {
			m.clear()
		}
		eventMerges.clear()
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
			val app = App.get()
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
			val app = App.get()
			if (app != null && app is AppVisibleListener) {
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

		val topVisibleActivity: BaseActivity? get() {
			if (visiableActivityCount > 0) {
				return _topActivity
			}
			return null
		}

	}

}

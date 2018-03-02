package yet.wip

import android.app.Activity
import com.google.gson.JsonObject
import yet.database.MapTable
import yet.json.*
import yet.net.Http
import yet.ui.dialogs.ConfirmDialog
import yet.ui.dialogs.OKDialog
import yet.ui.page.BaseFragment
import yet.util.*
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2017-06-13.
 */

class YetVersion(val jo: JsonObject) {
	val versionCode: Int by jo
	val versionName: String by jo
	val msg: String by jo
	val resId: Int by jo


	fun great(): Boolean {
		return versionCode > App.versionCode
	}


	fun install() {
		installApk(resId)
	}

	companion object {
		var SERVER_CHECK = "http://app800.cn:8080/apps/check"
		var SERVER_DOWN = "http://app800.cn:8080/apps/res/download"

		val ignoreMap = MapTable("ver_ignore")

		fun isIgnored(verCode:Int): Boolean {
			return ignoreMap.get(verCode.toString()) != null
		}

		fun checkUI(page: BaseFragment) {
			back {
				val v = check()
				fore {
					if (page.activity != null) {
						confirmInstall(page.activity, v)
					}
				}
			}
		}

		private fun confirmInstall(act: Activity, v: YetVersion?) {
			if (v == null || !v.great()) {
				try {
					val dlg = OKDialog()
					dlg.show(act, "已经是最新版本")
				} catch (ex: Exception) {
				}
				return
			}
			val dlg = ConfirmDialog()
			dlg.ok("升级")
			dlg.cancel("取消")
			dlg.mid("忽略此版本")
			dlg.onOK = {
				v.install()
			}
			dlg.onMid = {
				ignoreMap.put(v.versionCode.toString(), v.versionName)
			}
			dlg.show(act, "检查升级", "发现新版本${v.versionName}")
		}

		fun installApk(resId: Int) {
			val url = Http(SERVER_DOWN).arg("id", resId).buildGetUrl()
			Util.installApk(App.app_name + ".apk", url)
		}


		fun check(): YetVersion? {
			val r = Http(SERVER_CHECK).arg("pkg", App.packageName).get()
			if (r.OK) {
				val jo = r.gsonObject() ?: return null
				if (jo.int("code") != 0) {
					return null
				}
				val jdata = jo.obj("data") ?: return null
				return YetVersion(jdata)
			}
			return null
		}


	}
}
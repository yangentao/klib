package yet.update

import android.app.Activity
import yet.database.MapTable
import yet.file.AppSD
import yet.net.Http
import yet.ui.dialogs.DialogX
import yet.ui.dialogs.alert
import yet.ui.page.BaseFragment
import yet.util.app.App
import yet.util.back
import yet.util.fore
import yet.yson.YsonObject

/**
 * Created by entaoyang@163.com on 2017-06-13.
 */

class YetVersion(val jo: YsonObject) {
	val versionCode: Int by jo
	val versionName: String by jo
	val msg: String by jo
	val resId: Int by jo


	fun great(): Boolean {
		return versionCode > App.versionCode
	}

	companion object {
		var SERVER_CHECK = "http://app800.cn/apps/check"
		var SERVER_DOWN = "http://app800.cn/apps/res/download"
		var CHECK_HOURS = 12 //最多每12小时检查一次

		val ignoreMap = MapTable("ver_ignore")

		var lastCheckUpdate: Long by MapTable.config

		private fun isIgnored(verCode: Int): Boolean {
			return ignoreMap.get(verCode.toString()) != null
		}

		fun checkByUser(page: BaseFragment) {
			back {
				val v = check()
				fore {
					if (v != null) {
						if (page.activity != null) {
							confirmInstall(page.activity, v, false)
						}
					} else {
						page.alert("已经是最新版本")
					}
				}
			}

		}

		fun checkAuto(page: BaseFragment) {
			val last = lastCheckUpdate
			val now = System.currentTimeMillis()
			if (now - last < CHECK_HOURS * 60 * 60 * 1000) {
				return
			}
			back {
				val v = check()
				fore {
					if (v != null) {
						lastCheckUpdate = now
						if (!isIgnored(v.versionCode)) {
							if (page.activity != null) {
								confirmInstall(page.activity, v, true)
							}
						}
					}

				}
			}
		}

		private fun confirmInstall(act: Activity, v: YetVersion, quiet: Boolean) {
			if (!v.great()) {
				try {
					if (!quiet) {
						act.alert("已经是最新版本")
					}
				} catch (ex: Exception) {
				}
				return
			}

			val dlg = DialogX(act)
			dlg.title("检查升级")
			dlg.bodyText("发现新版本${v.versionName}")
			dlg.buttons {
				ok("升级") {
					installApk(v.resId)
				}
				normal("忽略此版本") {
					ignoreMap.put(v.versionCode.toString(), v.versionName)
				}
				cancel("取消")
			}
			dlg.show()
		}

		fun installApk(resId: Int) {
			val url = Http(SERVER_DOWN).arg("id", resId).buildGetUrl()
			val fileName = App.app_name + ".apk"
			back {
				val f = AppSD.temp(fileName)
				val r = Http(url).download(f, null)
				if (r.OK) {
					fore {
						App.installApk(f)
					}
				}
			}
		}


		private fun check(): YetVersion? {
			val r = Http(SERVER_CHECK).arg("pkg", App.packageName).get()
			if (r.OK) {
				val jo = r.ysonObject() ?: return null
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
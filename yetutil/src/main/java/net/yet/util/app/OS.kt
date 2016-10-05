package net.yet.util.app

import android.os.Build
import android.os.Build.VERSION
import net.yet.util.Util
import java.util.*

class OS {


	var HUAWEI = isManufacturer("HUAWEI")

	companion object {
		val API = Build.VERSION.SDK_INT
		val V21 = 7
		val V22 = 8
		val V23 = 9
		val V40 = 14
		val V41 = 16
		val V42 = 17
		val V43 = 18
		val V44 = 19
		val V44W = 20
		val V50 = 21
		val V51 = 22
		val V60 = 23
		val V70 = 24

		val GE40 = API >= V40
		val GE44 = API >= V44
		val GE50 = API >= V50
		val GE60 = API >= V60
		val GE70 = API >= V70


		fun incremental(): String {
			return VERSION.INCREMENTAL
		}

		fun isManufacturer(s: String): Boolean {
			return hasNoCase(Build.MANUFACTURER, s)
		}

		fun isManufacturerEq(s: String): Boolean {
			return Util.equalNoCase(Build.MANUFACTURER, s)
		}

		fun isModel(s: String): Boolean {
			return hasNoCase(Build.MODEL, s)
		}

		fun isModels(vararg ms: String): Boolean {
			for (m in ms) {
				if (isModel(m)) {
					return true
				}
			}
			return false
		}

		/**
		 * model字符串包含m, 并且API >= api

		 * @param m
		 * *
		 * @param api
		 * *
		 * @return
		 */
		fun isModelApi(m: String, api: Int): Boolean {
			return isModel(m) && API >= api
		}

		fun isHardware(s: String): Boolean {
			return hasNoCase(Build.HARDWARE, s)
		}

		fun model(): String {
			return Build.MODEL
		}

		fun manufacturer(): String {
			return Build.MANUFACTURER
		}

		fun hardware(): String {
			return Build.HARDWARE
		}

		/**
		 * s1字符串是否包含s2, 都是null会返回true, 不区分大小写

		 * @param s1
		 * *
		 * @param s2
		 * *
		 * @return
		 */
		private fun hasNoCase(s1: String?, s2: String?): Boolean {
			if (s1 != null && s2 != null) {
				return s1.toLowerCase(Locale.getDefault()).contains(s2.toLowerCase(Locale.getDefault()))
			}
			return s1 === s2
		}

		/**
		 * s1字符串是否包含s2, 都是null会返回true

		 * @param s1
		 * *
		 * @param s2
		 * *
		 * @return
		 */
		private fun has(s1: String?, s2: String?): Boolean {
			if (s1 != null && s2 != null) {
				return s1.indexOf(s2) >= 0
			}
			return s1 === s2
		}
	}

}
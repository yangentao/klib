package yet.util.app

import yet.util.RefClassE

/**
 * Created by entaoyang@163.com on 2016-10-05.
 */

object SysProp {


	fun isValue(key: String, value: String): Boolean {
		return get(key) == value
	}

	operator fun get(key: String, defVal: String? = null): String? {
		try {
			val rc = RefClassE.from("android.os.SystemProperties")
			return rc.invoke("get", arrayOf<Class<*>>(String::class.java, String::class.java), key, defVal) as String
		} catch (t: Throwable) {
		}

		return defVal
	}
}

package yet.util.app;

import yet.util.RefClassE;
import yet.util.Util;

/**
 * Created by entaoyang@163.com on 2016-10-05.
 */

public class SysProp {


	public static boolean isValue(String key, String value) {
		String s = get(key);
		return Util.equalStr(s, value);
	}

	public static String get(String key) {
		return get(key, null);
	}

	public static String get(String key, String defVal) {
		try {
			RefClassE rc = RefClassE.from("android.os.SystemProperties");
			return (String) rc.invoke("get", new Class<?>[]{String.class, String.class}, key, defVal);
		} catch (Throwable t) {
		}
		return defVal;
	}
}

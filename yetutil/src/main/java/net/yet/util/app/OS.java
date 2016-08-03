package net.yet.util.app;

import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;

import net.yet.util.RefClassE;
import net.yet.util.RefUtil;
import net.yet.util.Util;

import java.util.Locale;

public class OS {
	public static final int API = Build.VERSION.SDK_INT;
	public static final int V21 = 7;
	public static final int V22 = 8;
	public static final int V23 = 9;
	public static final int V40 = 14;
	public static final int V41 = 16;
	public static final int V42 = 17;
	public static final int V43 = 18;
	public static final int V44 = 19;
	public static final int V44W = 20;
	public static final int V50 = 21;
	public static final int V51 = 22;
	public static final int V60 = 23;

	public static final boolean GE44 = API >= V44;
	public static final boolean GE50 = API >= V50;
	public static final boolean GE60 = API >= V60;


	public static boolean sysResBool(String name, boolean defVal) {
		Integer n = internalRBool(name);
		if (n != null) {
			return Resources.getSystem().getBoolean(n);
		}
		return defVal;
	}

	public static String sysResString(String name, String defVal) {
		Integer n = internalRString(name);
		if (n != null) {
			return Resources.getSystem().getString(n);
		}
		return defVal;
	}

	public static Integer internalRBool(String name) {
		return (Integer) RefUtil.getStatic("com.android.internal.R$bool", name);
	}

	public static int internalRBool(String name, int defVal) {
		Integer n = (Integer) RefUtil.getStatic("com.android.internal.R$bool", name);
		return n == null ? defVal : n;
	}

	public static Integer internalRString(String name) {
		return (Integer) RefUtil.getStatic("com.android.internal.R$string", name);
	}

	public static int internalRString(String name, int defVal) {
		Integer n = (Integer) RefUtil.getStatic("com.android.internal.R$string", name);
		return n == null ? defVal : n;
	}

	public static boolean sysPropertyEqual(String key, String value) {
		String s = sysProperty(key);
		return Util.equalStr(s, value);
	}

	public static String sysProperty(String key) {
		return sysProperty(key, null);
	}

	public static String sysProperty(String key, String defVal) {
		try {
			RefClassE rc = RefClassE.from("android.os.SystemProperties");
			return (String) rc.invoke("get", new Class<?>[]{String.class, String.class}, key, defVal);
		} catch (Throwable t) {
		}
		return defVal;
	}

	public static String incremental() {
		return VERSION.INCREMENTAL;
	}

	public static boolean isManufacturer(String s) {
		return hasNoCase(Build.MANUFACTURER, s);
	}

	public static boolean isManufacturerEq(String s) {
		return Util.equalNoCase(Build.MANUFACTURER, s);
	}

	public static boolean isModel(String s) {
		return hasNoCase(Build.MODEL, s);
	}

	public static boolean isModels(String... ms) {
		for (String m : ms) {
			if (isModel(m)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * model字符串包含m, 并且API >= api
	 *
	 * @param m
	 * @param api
	 * @return
	 */
	public static boolean isModelApi(String m, int api) {
		return isModel(m) && API >= api;
	}

	public static boolean isHardware(String s) {
		return hasNoCase(Build.HARDWARE, s);
	}

	public static String model() {
		return Build.MODEL;
	}

	public static String manufacturer() {
		return Build.MANUFACTURER;
	}

	public static String hardware() {
		return Build.HARDWARE;
	}

	/**
	 * s1字符串是否包含s2, 都是null会返回true, 不区分大小写
	 *
	 * @param s1
	 * @param s2
	 * @return
	 */
	private static boolean hasNoCase(String s1, String s2) {
		if (s1 != null && s2 != null) {
			return s1.toLowerCase(Locale.getDefault()).indexOf(s2.toLowerCase(Locale.getDefault())) >= 0;
		}
		return s1 == s2;
	}

	/**
	 * s1字符串是否包含s2, 都是null会返回true
	 *
	 * @param s1
	 * @param s2
	 * @return
	 */
	private static boolean has(String s1, String s2) {
		if (s1 != null && s2 != null) {
			return s1.indexOf(s2) >= 0;
		}
		return s1 == s2;
	}

}

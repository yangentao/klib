package yet.util;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import yet.util.app.App;
import yet.util.app.DownloadTask;
import yet.util.log.xlog;

public class Util {
	public static final String UTF8 = "UTF-8";
	public static Charset charsetUTF8 = Charset.forName(UTF8);
	public static final int BUFFER_4K = 4096;
	public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	public static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	public static final int K = 1024;
	public static final int M = K * K;
	public static final int G = K * M;

	public static Collator collatorChina = Collator.getInstance(Locale.CHINA);

	public static void Assert(boolean b) {
		if (!b && App.INSTANCE.getDebug()) {
			xlog.INSTANCE.fatal("assert failed!");
		}
	}


	/**
	 * @param max
	 * @return [0-max]
	 */
	public static int random(int max) {
		return new Random().nextInt(max + 1);
	}

	/**
	 * @param min
	 * @param max
	 * @return [min, max]
	 */
	public static int random(int min, int max) {
		int max2 = max - min;
		int ret = random(max2);
		return ret + min;
	}

	public static String utf8(byte[] data) {
		return new String(data, charsetUTF8);
	}

	public static void sleep(int millSeconds) {
		try {
			Thread.sleep(millSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static SharedPreferences getPref() {
		return PreferenceManager.getDefaultSharedPreferences(App.INSTANCE.getApp());
	}

	public static String bytesToHexString(byte[] bytes) {
		if (bytes == null)
			return null;
		StringBuilder ret = new StringBuilder(2 * bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			int b;
			b = 0x0f & (bytes[i] >> 4);
			ret.append("0123456789abcdef".charAt(b));
			b = 0x0f & bytes[i];
			ret.append("0123456789abcdef".charAt(b));
		}

		return ret.toString();
	}

	/**
	 * 如果value是null, 则返回null, 否则, 返回一个list
	 *
	 * @param count
	 * @param value
	 * @return
	 */
	public static <T> ArrayList<T> repeatX(int count, T value) {
		if (value == null) {
			return null;
		}
		ArrayList<T> ls = new ArrayList<T>(count >= 4 ? count : 4);
		for (int i = 0; i < count; ++i) {
			ls.add(value);
		}
		return ls;
	}

	public static <T> ArrayList<T> repeat(int count, T value) {
		ArrayList<T> ls = new ArrayList<T>(count >= 4 ? count : 4);
		for (int i = 0; i < count; ++i) {
			ls.add(value);
		}
		return ls;
	}

	public static ArrayList<Integer> repeat(int count, int value) {
		ArrayList<Integer> ls = new ArrayList<Integer>(count >= 4 ? count : 4);
		for (int i = 0; i < count; ++i) {
			ls.add(value);
		}
		return ls;
	}

	public static ArrayList<Long> repeat(int count, long value) {
		ArrayList<Long> ls = new ArrayList<Long>(count >= 4 ? count : 4);
		for (int i = 0; i < count; ++i) {
			ls.add(value);
		}
		return ls;
	}

	public static int max(int... values) {
		int m = Integer.MIN_VALUE;
		for (int n : values) {
			if (n > m) {
				m = n;
			}
		}
		return m;
	}

	/**
	 * 指定的服务列表, 是否都是clsName这个类
	 *
	 * @param clsName
	 * @param serviceNames
	 * @return
	 */
	public static boolean isServiceClass(String clsName, String... serviceNames) {
		for (String name : serviceNames) {
			Object obj = getService(name);
			if (!sameClass(obj, clsName)) {
				return false;
			}
		}
		return true;
	}

	public static boolean sameClass(Object obj, String className) {
		if (obj != null) {
			return obj.getClass().getName().equals(className);
		}
		return false;
	}

	public static <T> T cast(Object obj, T defVal) {
		return obj == null ? defVal : (T) obj;
	}

	public static int countNotNull(Object... objs) {
		int i = 0;
		for (Object obj : objs) {
			if (obj != null) {
				++i;
			}
		}
		return i;
	}

	public static boolean lowerEqual(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null || s2 == null) {
			return false;
		}
		s1 = s1.toLowerCase(Locale.getDefault());
		s2 = s2.toLowerCase(Locale.getDefault());
		return s1.compareTo(s2) == 0;
	}

	/**
	 * 用于log或调试输出
	 *
	 * @param obj
	 * @return
	 */
	public static String toLogString(Object obj) {
		if (obj == null) {
			return "null";
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		if (obj instanceof Long) {
			return obj.toString() + "L";
		}
		if (obj instanceof Float) {
			return obj.toString() + "F";
		}
		if (obj instanceof Throwable) {
			Throwable tr = (Throwable) obj;
			StringWriter sw = new StringWriter(256);
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			return sw.toString();
		}
		if (obj.getClass().isArray()) {
			int len = Array.getLength(obj);
			StrBuilder sb = new StrBuilder(100);
			sb.append("[");
			for (int i = 0; i < len; ++i) {
				if (i != 0) {
					sb.append(",");
				}
				Object ele = Array.get(obj, i);
				String s = toLogString(ele);
				sb.append(s);
			}
			sb.append("]");
			return sb.toString();
		}
		if (obj instanceof List<?>) {
			List<?> ls = (List<?>) obj;
			StrBuilder sb = new StrBuilder(256);
			sb.append("LIST[");
			for (int i = 0; i < ls.size(); ++i) {
				if (i != 0) {
					sb.append(",");
				}
				Object val = ls.get(i);
				String s = toLogString(val);
				sb.append(s);
			}
			sb.append("]");
			return sb.toString();
		}
		if (obj instanceof Map<?, ?>) {
			StrBuilder sb = new StrBuilder(256);
			sb.append("{");
			@SuppressWarnings("rawtypes") Map map = (Map) obj;
			boolean first = true;
			for (Object key : map.keySet()) {
				if (!first) {
					sb.append(",");
				}
				first = false;
				Object value = map.get(key);
				sb.append(toLogString(key), "=", toLogString(value));
			}
			sb.append("}");
			return sb.toString();
		}

		return obj.toString();
	}

	public static Map<String, Object> map(String key, Object value, Object... keyValues) {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		for (int i = 0; i < keyValues.length; i += 2) {
			map.put((String) keyValues[i], keyValues[i + 1]);
		}
		return map;
	}


	public static int trimLength(String s) {
		return s == null ? 0 : s.trim().length();
	}

	public static <T> String[] toStringArray(Collection<T> ls) {
		if (empty(ls)) {
			return new String[0];
		}
		String[] arr = new String[ls.size()];
		int index = 0;
		for (T val : ls) {
			arr[index++] = val == null ? null : val.toString();
		}
		return arr;

	}

	public static String sqlEscape(String s) {
		return s;
	}

	public static <T> String[] toSqlStringArray(Collection<T> ls) {
		if (empty(ls)) {
			return new String[0];
		}
		String[] arr = new String[ls.size()];
		int index = 0;
		for (T val : ls) {
			arr[index++] = val == null ? null : sqlEscape(val.toString());
		}
		return arr;

	}

	public static <T> String[] toSqlStringArray(String[] ls) {
		if (ls == null || ls.length == 0) {
			return new String[0];
		}
		String[] arr = new String[ls.length];
		int index = 0;
		for (String val : ls) {
			arr[index++] = val == null ? null : sqlEscape(val);
		}
		return arr;

	}

	public static <T> HashSet<T> asSet(T... values) {
		HashSet<T> set = new HashSet<T>();
		for (T val : values) {
			set.add(val);
		}
		return set;
	}

	public static <T> HashSet<T> asSet(Iterable<T> values) {
		HashSet<T> set = new HashSet<T>();
		for (T val : values) {
			set.add(val);
		}
		return set;
	}

	public static <T> ArrayList<T> asListByArray(T[] values) {
		ArrayList<T> ls = new ArrayList<T>();
		for (T val : values) {
			ls.add(val);
		}
		return ls;
	}

	public static ArrayList<Integer> asList(int... values) {
		ArrayList<Integer> ls = new ArrayList<Integer>();
		for (int val : values) {
			ls.add(val);
		}
		return ls;
	}

	public static <T> ArrayList<T> asList(T... values) {
		ArrayList<T> ls = new ArrayList<T>();
		for (T val : values) {
			ls.add(val);
		}
		return ls;
	}

	public static <T> ArrayList<T> asList(Collection<T> c) {
		ArrayList<T> ls = new ArrayList<>();
		if (c != null) {
			ls.addAll(c);
		}
		return ls;
	}

	public static <T> ArrayList<T> addToList(T value) {
		ArrayList<T> ls = new ArrayList<T>();
		ls.add(value);
		return ls;
	}

	/**
	 * 得到对象父类的泛型参数类型, ----得不到本类的, 只能是父类
	 *
	 * @param obj
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenericParam(Object obj, int index) {
		ParameterizedType pt = (ParameterizedType) obj.getClass().getGenericSuperclass();
		Type[] types = pt.getActualTypeArguments();
		Type t = types[index];
		return (Class<T>) t;
	}

	/**
	 * 限制value 在闭区间[min, max]以内
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static int limit(int value, int min, int max) {
		if (value > max) {
			value = max;
		}
		if (value < min) {
			value = min;
		}
		return value;
	}

	public static float limit(float value, float min, float max) {
		if (value > max) {
			value = max;
		}
		if (value < min) {
			value = min;
		}
		return value;
	}

	public static void openApk(Context context, String url) {
		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setDataAndType(Uri.parse(url), "application/vnd.android.package-archive");
			context.startActivity(i);
		} catch (Exception e) {
			openUrl(context, url);
		}
	}

	public static void openUrl(Context context, String url) {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}

	public static void openActivity(Context context, Class<? extends Activity> cls) {
		Intent it = new Intent(context, cls);
		context.startActivity(it);
	}

	public static void openActivity(Context context, Class<? extends Activity> cls, int flags) {
		Intent it = new Intent(context, cls);
		it.setFlags(flags);
		context.startActivity(it);
	}

	public static int parseInt(String s, int defVal) {
		if (s != null) {
			s = s.trim();
			try {
				return Integer.valueOf(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defVal;
	}

	public static long parseLong(String s, long defVal) {
		if (s != null) {
			s = s.trim();
			try {
				return Long.valueOf(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defVal;
	}

	public static TelephonyManager getTelManager() {
		return (TelephonyManager) getService(Context.TELEPHONY_SERVICE);
	}

	public static PowerManager getPowerManager() {
		return (PowerManager) getSystemService(Context.POWER_SERVICE);
	}

	public static KeyguardManager getKeyguardManager() {
		return (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	}

	/**
	 * r本次被执行了, 返回true; 本次没被执行, 返回false
	 * 只执行一次, 安装app后
	 *
	 * @param key
	 * @param r
	 */
	public static boolean runOnce(String key, Runnable r) {
		return runOnceFile("runonce", key, r);
	}

	/**
	 * r本次被执行了, 返回true; 本次没被执行, 返回false
	 *
	 * @param key
	 * @param r
	 * @return
	 */
	public static boolean runOnceVer(String key, Runnable r) {
		return runOnceFile("runonce" + App.INSTANCE.getVersionCode(), key, r);
	}

	/**
	 * r本次被执行了, 返回true; 本次没被执行, 返回false
	 *
	 * @param filename
	 * @param key
	 * @param r
	 * @return
	 */
	private static boolean runOnceFile(String filename, String key, Runnable r) {
		XMap map = XMap.load(filename);
		int n = map.getInt(key, 0);
		if (n != 0) {
			return false;
		}
		map.put(key, 1);
		map.save();
		if (r != null) {
			r.run();
		}
		return true;
	}

	/**
	 * 是否已经执行过该key
	 *
	 * @param key
	 * @return
	 */
	public static boolean hasRuned(String key) {
		XMap map = XMap.load("runonce");
		int n = map.getInt(key, 0);
		if (n != 0) {
			return true;
		}
		return false;
	}

	public static boolean hasRunedVer(String key) {
		XMap map = XMap.load("runonce" + App.INSTANCE.getVersionCode());
		int n = map.getInt(key, 0);
		if (n != 0) {
			return true;
		}
		return false;
	}

	public static void showInputMethod(View view) {
		InputMethodManager imm = (InputMethodManager) App.INSTANCE.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}


	public static void viewImage(Context context, Uri uri) {
		viewAction(context, uri, "image/*");
	}

	public static void viewAction(Context context, Uri uri, String dataType) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(uri, dataType);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	public static String getClipText() {
		ClipboardManager cm = (ClipboardManager) App.INSTANCE.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
		return cm.getText().toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T firstOfArray(Object arr, T defVal) {
		Object ret = firstOfArray(arr);
		if (ret != null) {
			return (T) ret;
		}
		return defVal;
	}

	public static Object firstOfArray(Object arr) {
		if (arr != null && arr.getClass().isArray() && Array.getLength(arr) > 0) {
			return Array.get(arr, 0);
		}
		return null;
	}

	public static <T> T first(T[] arr) {
		if (arr != null && arr.length > 0) {
			return arr[0];
		}
		return null;
	}

	public static <T> T first(T[] arr, T defVal) {
		if (arr != null && arr.length > 0) {
			return arr[0];
		}
		return defVal;
	}

	public static int first(int[] arr, int defVal) {
		if (arr != null && arr.length > 0) {
			return arr[0];
		}
		return defVal;
	}

	public static long first(long[] arr, long defVal) {
		if (arr != null && arr.length > 0) {
			return arr[0];
		}
		return defVal;
	}

	public static <T> T first(List<T> ls) {
		if (length(ls) > 0) {
			return ls.get(0);
		}
		return null;
	}

	public static <T> T first(Set<T> set) {
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		}
		return null;
	}

	public static <T> T last(List<T> ls) {
		if (length(ls) > 0) {
			return ls.get(ls.size() - 1);
		}
		return null;
	}

	public static void installApk(String title, String url) {
		DownloadTask.Companion.downloadAndInstall(url, title, ".apk", "");
	}

	public static void installApk(File apkFile) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		App.INSTANCE.getApp().startActivity(intent);
	}

	public static void installShortcut(String name, int imageRes, Class<?> cls, String exKey, String exValue) {
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.addCategory(Intent.CATEGORY_LAUNCHER);
		it.setClass(App.INSTANCE.getApp(), cls);
		it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (notEmpty(exKey)) {
			it.putExtra(exKey, exValue);
		}
		Util.installShortcut(name, imageRes, it);
	}

	public static void installShortcut(String name, int imageRes, Intent intent) {
		Intent addShortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
		// 不允许重复创建
		addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
		// 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
		// 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
		// 屏幕上没有空间时会提示
		// 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(App.INSTANCE.getApp(), imageRes));
		addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		App.INSTANCE.getApp().sendBroadcast(addShortcutIntent);
	}

	/**
	 * 过滤掉了空串, 并且trim
	 *
	 * @param s
	 * @param sep
	 * @return
	 */
	public static List<String> splitFilterEmptyTrim(String s, String sep) {
		String[] arr = s.split(sep);
		List<String> ls = new ArrayList<String>();
		for (String ss : arr) {
			ss = ss.trim();
			if (notEmpty(ss)) {
				ls.add(ss);
			}
		}
		return ls;
	}

	public static Class<?>[] classArray(Class<?>... values) {
		return values;
	}

	public static <T> T[] toArray(T... values) {
		return values;
	}

	public static List<String> toList(Collection<String> ss) {
		List<String> ls = new ArrayList<String>();
		ls.addAll(ss);
		return ls;
	}

	public static Set<String> toSet(Collection<String> ss) {
		Set<String> set = new HashSet<String>();
		set.addAll(ss);
		return set;
	}

	public static void hideInputMethod(TextView ed) {
		InputMethodManager imm = (InputMethodManager) ed.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && ed.isFocused()) {
			imm.hideSoftInputFromWindow(ed.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void hideInputMethod(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && activity.getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void copyToClipboard(String text) {
		ClipboardManager clip = (ClipboardManager) App.INSTANCE.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
		clip.setText(text);
		toast("内容已拷贝到剪贴板");
	}

	public TelephonyManager getTeleMgr() {
		return (TelephonyManager) App.INSTANCE.getApp().getSystemService(Context.TELEPHONY_SERVICE);
	}

	//	/**
	//	 * 开启wifi
	//	 */
	//	public static void openWifi() {
	//		if (!getWifiManager().isWifiEnabled()) {
	//			getWifiManager().setWifiEnabled(true);
	//		}
	//	}
	//
	//	/** 关闭wifi **/
	//	public static void closeWifi() {
	//		if (getWifiManager().isWifiEnabled()) {
	//			getWifiManager().setWifiEnabled(false);
	//		}
	//	}

	/**
	 * 当前是否正在使用wifi
	 */
	public static boolean isWifiActive() {
		ConnectivityManager cm = Util.getConnectivityManager();
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo == null) {
			return false;
		}
		return activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
	}


	public static ConnectivityManager getConnMgr() {
		return (ConnectivityManager) App.INSTANCE.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static ConnectivityManager getConnectivityManager() {
		return (ConnectivityManager) App.INSTANCE.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static Object getSystemService(String name) {
		return App.INSTANCE.getApp().getSystemService(name);
	}

	/**
	 * 找到一个类cls的方法, 以startWithMethodName开头(或相等), 并具有returyType的返回值类型和types的参数签名
	 *
	 * @param cls                 要查找的类
	 * @param startWithMethodName 要查找的方法名, 匹配以这个名字开始的方法
	 * @param types               参数签名
	 * @return 找到的方法, 或null
	 */
	public static Method findMethodStartWith(Class<?> cls, Class<?> returnType, String startWithMethodName, Class<?>... types) {
		for (Method m : cls.getMethods()) {
			if (!m.getName().startsWith(startWithMethodName)) {
				continue;
			}
			if (m.getReturnType() != returnType) {
				continue;
			}
			// 返回类型匹配, 并且名字匹配

			Class<?>[] paramTypes = m.getParameterTypes();
			if (paramTypes.length == 0 && types.length == 0) {
				return m;
			}
			if (paramTypes.length == 0 && types.length == 1 && types[0] == void.class) {
				return m;
			}
			if (types.length == 0 && paramTypes.length == 1 && paramTypes[0] == void.class) {
				return m;
			}

			if (paramTypes.length != types.length) {
				continue;
			}
			boolean eq = true;
			for (int i = 0; i < types.length; ++i) {
				if (paramTypes[i] != types[i]) {
					eq = false;
					break;
				}
			}
			if (eq) {
				return m;
			}
		}
		return null;
	}

	/**
	 * 是否纯字母a-z,A-Z
	 *
	 * @param s
	 * @return
	 */
	public static boolean isAlphas(String s) {
		char ch = 0;
		for (int i = 0; i < s.length(); ++i) {
			ch = s.charAt(i);
			if (!inRange11(ch, 'A', 'Z') && !inRange11(ch, 'a', 'z')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否纯数字
	 *
	 * @param s
	 * @return
	 */
	public static boolean isNumbers(String s) {
		char ch = 0;
		for (int i = 0; i < s.length(); ++i) {
			ch = s.charAt(i);
			if (ch < '0' || ch > '9') {
				return false;
			}
		}
		return true;
	}

	// 获取手机号, 或者返回null
	public static String getPhoneNumber1() {
		TelephonyManager tm = (TelephonyManager) App.INSTANCE.getApp().getSystemService(Context.TELEPHONY_SERVICE);
		if (tm != null) {
			return tm.getLine1Number();
		}
		return null;
	}

	public static <T> List<T> reverseCopy(List<T> ls) {
		if (ls != null) {
			ArrayList<T> arr = new ArrayList<T>(ls.size());
			for (int i = ls.size() - 1; i >= 0; --i) {
				arr.add(ls.get(i));
			}
			return arr;
		}
		return null;
	}

	public static <T> List<T> reverse(List<T> ls) {
		if (ls != null) {
			int size = ls.size();
			int mid = size / 2;
			for (int i = 0; i < mid; ++i) {
				T tmp = ls.get(i);
				ls.set(i, ls.get(size - 1 - i));
				ls.set(size - 1 - i, tmp);
			}
		}
		return ls;
	}

	// with path
	public static Uri uriPath(Uri uri, String pathSegment) {
		return Uri.withAppendedPath(uri, Uri.encode(pathSegment));
	}

	// with id
	public static Uri uriId(Uri uri, long id) {
		return ContentUris.withAppendedId(uri, id);
	}

	// with param
	public static Uri uriParam(Uri uri, String key, String value) {
		return uri.buildUpon().appendQueryParameter(key, value).build();
	}

	public static long uriId(Uri uri) {
		return ContentUris.parseId(uri);
	}

	public static <T> HashSet<T> copy(Set<T> s) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(s);
		return set;
	}

	public static <T> List<T> copy(List<T> ls) {
		List<T> newls = new ArrayList<T>(ls.size());
		newls.addAll(ls);
		return newls;
	}

	public static int compareLong(long left, long right) {
		long d = left - right;
		if (d > 0) {
			return 1;
		}
		if (d < 0) {
			return -1;
		}
		return 0;
	}

	public static List<String> validPhones(Collection<String> phones) {
		List<String> ls = new ArrayList<String>();
		for (String p : phones) {
			if (Util.isPhone(p)) {
				ls.add(Util.formatPhone(p));
			}
		}
		return ls;
	}

	// 去掉所有空格, 剩下全数字
	public static String formatPhone(String phone) {
		if (phone == null) {
			return null;
		}

		phone = phone.replaceAll("-", "");
		phone = phone.replace("+", "");
		phone = phone.replaceAll(" ", "");
		if (phone.length() >= 13) {
			if (phone.startsWith("86")) {
				phone = phone.substring(2);
			}
		}
		if (phone.length() == 12) {
			// 11位是手机号码, 区号+固话也可能是11位, 010 12345678, 所以 下面用01开头判断
			if (phone.startsWith("01")) {// 015110151971
				phone = phone.substring(1);// 15110151971
			}
		}
		return phone;
	}

	public static boolean isPhone(String phone) {
		if (length(phone) >= 3) {
			phone = formatPhone(phone);
			for (int i = 0; i < phone.length(); ++i) {
				char ch = phone.charAt(i);
				if (ch < '0' || ch > '9') {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	// 三个参数都非空
	// 失败返回null
	public static <T> String[] strField(List<T> ls, Class<T> cls, String fieldName) {
		try {
			Field f = cls.getField(fieldName);
			String[] arr = new String[ls.size()];
			for (int i = 0; i < arr.length; ++i) {
				arr[i] = (String) f.get(ls.get(i));
			}
			return arr;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return null;
	}


	public static void toast(final String... ss) {
		TaskUtil.fore(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(App.INSTANCE.getApp(), join("", ss), Toast.LENGTH_LONG).show();
			}
		});
	}

	public static void toastShort(final String... ss) {
		TaskUtil.fore(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(App.INSTANCE.getApp(), join("", ss), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static char firstChar(String s, char defChar) {
		if (s != null && s.length() > 0) {
			return s.charAt(0);
		}
		return defChar;
	}

	public static <T> T first(List<T> ls, T defaultValue) {
		if (ls == null || ls.size() == 0) {
			return defaultValue;
		}
		return ls.get(0);
	}

	public static String join(String j, String... arr) {
		StrBuilder sb = new StrBuilder(arr.length * 10 + 8, j);
		sb.appendS(arr);
		return sb.toString();
	}

	public static String join(String j, Collection<String> ls) {
		if (ls != null) {
			StrBuilder sb = new StrBuilder(ls.size() * 10 + 8, j);
			sb.appendAll(ls);
			return sb.toString();
		} else {
			return "";
		}

	}

	// public static void contactView(Context context, long contactId) {
	// Intent i = new Intent(Intent.ACTION_VIEW);
	// i.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
	// contactId));
	// context.startActivity(i);
	// }
	//
	// public static void contactEdit(Context context, long contactId) {
	// Intent i = new Intent(Intent.ACTION_EDIT);
	// i.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
	// contactId));
	// context.startActivity(i);
	// }

	// // phone可以是null
	// public static void contactInsert(Context context, String phone) {
	// Intent it = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(
	// Uri.parse("content://com.android.contacts"), "contacts"));
	// it.setType("vnd.android.cursor.dir/person");
	// if (notEmpty(phone)) {
	// it.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
	// }
	// context.startActivity(it);
	// }

	// //不好使
	// public static void contactInsertOrEdit(Context context, String phone) {
	// Intent it = new Intent(Intent.ACTION_INSERT_OR_EDIT);
	// it.setType("vnd.android.cursor.item/contact");
	// it.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
	// context.startActivity(it);
	// }

	// public static void smsSendSys(Context context, String body, String...
	// phones) {
	// String s = join(",", phones);
	// Uri uri = Uri.parse("smsto:" + s);
	// Intent i = new Intent(Intent.ACTION_SENDTO, uri);
	// i.putExtra("sms_body", body);
	// context.startActivity(i);
	// }
	//
	// public static void smsSendSys(Context context, String body, List<String>
	// phones) {
	// String s = join(",", phones);
	// Uri uri = Uri.parse("smsto:" + s);
	// Intent i = new Intent(Intent.ACTION_SENDTO, uri);
	// i.putExtra("sms_body", body);
	// context.startActivity(i);
	// }

	public static void callDefault(Context context, String phone) {
		if (Util.notEmpty(phone)) {
			Uri uri = Uri.fromParts("tel", phone, null);
			Intent i = new Intent(Intent.ACTION_CALL, uri);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// FLAG_RECEIVER_FOREGROUND
			try {
				context.startActivity(i);
			} catch (Throwable e) {
			}
		}
	}

	public static boolean lastCharIs(String s, int ch) {
		if (s != null && s.length() > 0) {
			return s.charAt(s.length() - 1) == ch;
		}
		return false;
	}

	public static boolean hasChar(String s, int ch) {
		if (s != null && s.length() > 0) {
			return s.lastIndexOf(ch) >= 0;
		}
		return false;
	}

	public static <T extends Comparable<T>> int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return o1.compareTo(o2);
	}

	public static int lengthOfArray(Object arr) {
		if (arr != null && arr.getClass().isArray()) {
			return Array.getLength(arr);
		}
		return 0;
	}

	public static int length(int[] arr) {
		return arr == null ? 0 : arr.length;
	}

	public static int length(long[] arr) {
		return arr == null ? 0 : arr.length;
	}

	public static int length(byte[] arr) {
		return arr == null ? 0 : arr.length;
	}

	public static <T> int length(T[] arr) {
		return arr == null ? 0 : arr.length;
	}

	public static int length(Collection<?> c) {
		return c == null ? 0 : c.size();
	}

	public static int length(String s) {
		return s == null ? 0 : s.length();
	}

	@SuppressWarnings("rawtypes")
	public static int length(Map m) {
		return m == null ? 0 : m.size();
	}

	public static void debugFail(boolean condition, String msg) {
		if (condition) {
			if (App.INSTANCE.getDebug()) {
				fail(msg);
			} else {
				xlog.INSTANCE.e(msg);
			}
		}
	}

	public static void failWhen(boolean condition, String info) {
		if (condition) {
			fail(info);
		}
	}

	public static void failIf(boolean condition, String info) {
		if (condition) {
			fail(info);
		}
	}

	public static void fail(String info) {
		throw new IllegalStateException("" + info);
	}

	public static void setViewWidth(View v, int width) {
		ViewGroup.LayoutParams lp = v.getLayoutParams();
		lp.width = width;
		v.setLayoutParams(lp);
	}

	public static void setViewSize(View v, int width, int height) {
		ViewGroup.LayoutParams lp = v.getLayoutParams();
		lp.width = width;
		lp.height = height;
		v.setLayoutParams(lp);
	}

	public static void fillTextView(View parentView, int textViewId, String text) {
		TextView textView = (TextView) parentView.findViewById(textViewId);
		textView.setText(text);
	}


	// /**
	// * for example : yyyy-MM-dd HH:mm:ss
	// *
	// * @param pattern
	// * @return
	// */
	// public static String getDateString(String pattern) {
	// SimpleDateFormat ff = new SimpleDateFormat(pattern, Locale.getDefault());
	// return ff.format(new Date());
	// }
	//
	// public static String getDateString(long date, String pattern) {
	// SimpleDateFormat ff = new SimpleDateFormat(pattern, Locale.getDefault());
	// return ff.format(new Date(date));
	// }


	public static boolean isHttp(String url) {
		if (isEmpty(url) || url.length() < 10) {
			return false;
		}
		return url.startsWith("http://");
	}

	public static void close(Closeable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
		}
	}


	private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String bytes2HexString(byte[] data) {
		StringBuffer sb = new StringBuffer(32);
		for (byte b : data) {
			char low = DIGITS[b & 0x0F];
			char high = DIGITS[(b & 0xF0) >>> 4];
			sb.append(high);
			sb.append(low);
		}
		return sb.toString();
	}

	public static String md5(String val) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());
			byte[] m = md5.digest();// 加密
			return bytes2HexString(m);
		} catch (Exception e) {
			e.printStackTrace();
			xlog.INSTANCE.e(e);
		}
		return null;
	}

	public static boolean inSetN(int target, int... values) {
		for (int v : values) {
			if (target == v) {
				return true;
			}
		}
		return false;
	}

	public static boolean inSet(String value, String... values) {
		for (String s : values) {
			if (value == s) {
				return true;
			}
			if (value != null && value.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean inSet(Object target, Object... values) {
		for (Object o : values) {
			if (equal(target, o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * (null, "") => true
	 *
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equalStr(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null) {
			s1 = "";
		}
		if (s2 == null) {
			s2 = "";
		}
		if (s1 == s2) {
			return true;
		}
		return s1.equals(s2);
	}

	public static boolean equalNoCase(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null || s2 == null) {
			return false;
		}
		return s1.toLowerCase(Locale.getDefault()).equals(s2.toLowerCase(Locale.getDefault()));
	}

	public static boolean equal(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null) {
			return false;
		}
		return o1.equals(o2);
	}

	public static boolean notEmpty(String s) {
		return !isEmpty(s);
	}

	public static boolean notEmpty(Collection<?> c) {
		return !isEmpty(c);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean empty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean emptyTrim(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean empty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean empty(Map<?, ?> map) {
		return map == null || map.size() == 0;
	}

	public static boolean notEmpty(Map<?, ?> map) {
		return !empty(map);
	}

	/**
	 * val 属于区间[from, to]
	 *
	 * @param val
	 * @param from
	 * @param to
	 * @return
	 * @Description:
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-18
	 */

	public static boolean inRange11(int val, int from, int to) {
		return val >= from && val <= to;
	}






	public static Object getService(String name) {
		return App.INSTANCE.getApp().getSystemService(name);
	}


}

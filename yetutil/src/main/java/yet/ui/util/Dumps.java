package yet.ui.util;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import yet.util.MyDate;
import yet.util.StrBuilder;
import yet.util.log.xlog;

public class Dumps {
	/**
	 * 打印一个类的所有方法
	 * 
	 * @param cls
	 */
	public static void classMethod(Class<?> cls) {
		ArrayList<String> all = new ArrayList<String>(64);
		for (Method m : cls.getMethods()) {
			StrBuilder sb = new StrBuilder(128);
			sb.append(m.getName());
			sb.append("(");
			boolean first = true;
			for (Class<?> p : m.getParameterTypes()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(p.getSimpleName());
			}
			sb.append(") -->　");
			sb.append(m.getReturnType().getSimpleName());

			if (Modifier.isStatic(m.getModifiers())) {
				sb.append("  [static]");
			}

			all.add(sb.toString());

		}
		Collections.sort(all);
		for (String m : all) {
			xlog.INSTANCE.d(m);
		}
	}

	public static void bundle(Bundle b) {
		dumpBundle("", b);
	}

	private static void dumpBundle(String prefix, Bundle b) {
		if (b != null) {
			for (String key : b.keySet()) {
				Object value = b.get(key);
				if (value instanceof Bundle) {
					xlog.INSTANCE.d(prefix, key);
					dumpBundle(prefix + "        ", (Bundle) value);
				} else {
					xlog.INSTANCE.d(prefix, key, value, value == null ? " null " : value.getClass().getSimpleName());
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void build() {
		xlog.INSTANCE.d("BOARD", Build.BOARD);
		xlog.INSTANCE.d("BOOTLOADER", Build.BOOTLOADER);
		xlog.INSTANCE.d("BRAND", Build.BRAND);
		xlog.INSTANCE.d("CPU_ABI", Build.CPU_ABI);
		xlog.INSTANCE.d("CPU_ABI2", Build.CPU_ABI2);
		xlog.INSTANCE.d("DEVICE", Build.DEVICE);
		xlog.INSTANCE.d("DISPLAY", Build.DISPLAY);
		xlog.INSTANCE.d("FINGERPRINT", Build.FINGERPRINT);
		xlog.INSTANCE.d("HARDWARE", Build.HARDWARE);
		xlog.INSTANCE.d("HOST", Build.HOST);
		xlog.INSTANCE.d("ID", Build.ID);
		xlog.INSTANCE.d("MANUFACTURER", Build.MANUFACTURER);
		xlog.INSTANCE.d("MODEL", Build.MODEL);
		xlog.INSTANCE.d("PRODUCT", Build.PRODUCT);
		xlog.INSTANCE.d("RADIO", Build.RADIO);
		xlog.INSTANCE.d("SERIAL", Build.SERIAL);
		xlog.INSTANCE.d("TAGS", Build.TAGS);
		xlog.INSTANCE.d("TIME", new MyDate(Build.TIME, Locale.getDefault()).formatDate());
		xlog.INSTANCE.d("TYPE", Build.TYPE);
		xlog.INSTANCE.d("UNKNOWN", Build.UNKNOWN);
		xlog.INSTANCE.d("USER", Build.USER);
		xlog.INSTANCE.d("getRadioVersion", Build.getRadioVersion());
	}

	public static void telMgr(TelephonyManager tm) {
		xlog.INSTANCE.d("Dump TelephonyManager");
		xlog.INSTANCE.d("line 1 number:", tm.getLine1Number());
		xlog.INSTANCE.d("country iso: ", tm.getNetworkCountryIso());
		xlog.INSTANCE.d("getNetworkOperator", tm.getNetworkOperator());
		xlog.INSTANCE.d("getNetworkOperatorName", tm.getNetworkOperatorName());
		xlog.INSTANCE.d("getNetworkType", tm.getNetworkType());
		xlog.INSTANCE.d("getPhoneType", tm.getPhoneType());
		xlog.INSTANCE.d("getSimCountryIso", tm.getSimCountryIso());
		xlog.INSTANCE.d("getSimOperator", tm.getSimOperator());
		xlog.INSTANCE.d("getSimOperatorName ", tm.getSimOperatorName());
		xlog.INSTANCE.d("getSimSerialNumber ", tm.getSimSerialNumber());
		xlog.INSTANCE.d("getSimState ", tm.getSimState());
		xlog.INSTANCE.d("getSubscriberId ", tm.getSubscriberId());
		xlog.INSTANCE.d("getDeviceId ", tm.getDeviceId());
		xlog.INSTANCE.d("getDeviceSoftwareVersion ", tm.getDeviceSoftwareVersion());
	}
}

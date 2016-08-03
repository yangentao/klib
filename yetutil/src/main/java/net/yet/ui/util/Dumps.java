package net.yet.ui.util;

import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import net.yet.util.DateUtil;
import net.yet.util.StrBuilder;
import net.yet.util.xlog;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

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
			xlog.d(m);
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
					xlog.d(prefix, key);
					dumpBundle(prefix + "        ", (Bundle) value);
				} else {
					xlog.d(prefix, key, value, value == null ? " null " : value.getClass().getSimpleName());
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void build() {
		xlog.d("BOARD", Build.BOARD);
		xlog.d("BOOTLOADER", Build.BOOTLOADER);
		xlog.d("BRAND", Build.BRAND);
		xlog.d("CPU_ABI", Build.CPU_ABI);
		xlog.d("CPU_ABI2", Build.CPU_ABI2);
		xlog.d("DEVICE", Build.DEVICE);
		xlog.d("DISPLAY", Build.DISPLAY);
		xlog.d("FINGERPRINT", Build.FINGERPRINT);
		xlog.d("HARDWARE", Build.HARDWARE);
		xlog.d("HOST", Build.HOST);
		xlog.d("ID", Build.ID);
		xlog.d("MANUFACTURER", Build.MANUFACTURER);
		xlog.d("MODEL", Build.MODEL);
		xlog.d("PRODUCT", Build.PRODUCT);
		xlog.d("RADIO", Build.RADIO);
		xlog.d("SERIAL", Build.SERIAL);
		xlog.d("TAGS", Build.TAGS);
		xlog.d("TIME", DateUtil.date(Build.TIME));
		xlog.d("TYPE", Build.TYPE);
		xlog.d("UNKNOWN", Build.UNKNOWN);
		xlog.d("USER", Build.USER);
		xlog.d("getRadioVersion", Build.getRadioVersion());
	}

	public static void telMgr(TelephonyManager tm) {
		xlog.d("Dump TelephonyManager");
		xlog.d("line 1 number:", tm.getLine1Number());
		xlog.d("country iso: ", tm.getNetworkCountryIso());
		xlog.d("getNetworkOperator", tm.getNetworkOperator());
		xlog.d("getNetworkOperatorName", tm.getNetworkOperatorName());
		xlog.d("getNetworkType", tm.getNetworkType());
		xlog.d("getPhoneType", tm.getPhoneType());
		xlog.d("getSimCountryIso", tm.getSimCountryIso());
		xlog.d("getSimOperator", tm.getSimOperator());
		xlog.d("getSimOperatorName ", tm.getSimOperatorName());
		xlog.d("getSimSerialNumber ", tm.getSimSerialNumber());
		xlog.d("getSimState ", tm.getSimState());
		xlog.d("getSubscriberId ", tm.getSubscriberId());
		xlog.d("getDeviceId ", tm.getDeviceId());
		xlog.d("getDeviceSoftwareVersion ", tm.getDeviceSoftwareVersion());
	}
}

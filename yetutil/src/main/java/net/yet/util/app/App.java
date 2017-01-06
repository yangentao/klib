package net.yet.util.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import net.yet.ui.activities.AnimConf;
import net.yet.util.MsgCenter;
import net.yet.util.StrBuilder;
import net.yet.util.Util;
import net.yet.util.database.Values;
import net.yet.util.log.xlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;

public class App {

	public static final String NETWORK_CONNECTED = "network.connected";

	private static WeakReference<Application> app = null;

	public static boolean debug = false;
	public static String app_name = null;
	public static int ic_launcher = 0;
	public static float density = 1;
	public static float scaledDensity = 1;
	public static String packageName = null;
	public static int versionCode = 1;
	public static String versionName = "1.0.0";

	public static AnimConf animConfDefault = new AnimConf();

	public static void init(Application application) {
		if (app != null && app.get() != null) {
			return;
		}
		app = new WeakReference<>(application);

		ic_launcher = application.getApplicationInfo().icon;
		app_name = application.getApplicationInfo().loadLabel(application.getPackageManager()).toString();
		packageName = application.getPackageName();
		density = application.getResources().getDisplayMetrics().density;
		scaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
		debug = 0 != (App.get().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);

		try {
			PackageInfo pi = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
			versionCode = pi.versionCode;
			versionName = pi.versionName;
		} catch (NameNotFoundException e) {
		}

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				StrBuilder sb = new StrBuilder(128);
				sb.append("uncaughtException:", thread.getId(), " ", thread.getName());
				ex.printStackTrace();
				xlog.INSTANCE.e(ex);
				xlog.INSTANCE.flush();
				System.exit(-1);
			}
		});

		BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					MsgCenter.INSTANCE.fire(ConnectivityManager.class);
				}
				if (isNetworkConnected()) {
					MsgCenter.INSTANCE.fire(App.NETWORK_CONNECTED);
				}

			}
		};
		App.get().registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

//		ImageLoaderExtKt.initImageLoader();
	}

	public static Context getContext() {
		return app.get();
	}

	public static Application app() {
		return app.get();
	}

	public static File sdAppRoot() {
		return app.get().getExternalFilesDir(null);
	}

	public static boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) app().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnected();
	}

	public static boolean debug() {
		return debug;
	}


	public static int getVersionCode() {
		return versionCode;
	}


	// 单位兆M
	private static int memLimit() {
		ActivityManager activityManager = (ActivityManager) app().getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getMemoryClass();
	}

	public static void exit() {
		System.exit(0);
	}


	public static Application get() {
		return app();
	}

	public static String imei() {
		try {
			TelephonyManager tm = (TelephonyManager) app().getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {

				return tm.getDeviceId();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return ai == null ? null : (ai.metaData == null ? null : ai.metaData.getString(metaKey));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			xlog.INSTANCE.e(e);
		}
		return null;
	}

	public static String metaValue(String metaKey) {
		return getMetaValue(app(), metaKey);
	}


	public static int sp2px(float spValue) {
		return (int) (spValue * scaledDensity + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(float dpValue) {
		if(dpValue > 0) {
			return (int) (dpValue * density + 0.5f);
		}else {
			return (int)dpValue;
		}
	}

	public static int dp2px(int dpValue) {
		if(dpValue > 0) {
			return (int) (dpValue * density + 0.5f);
		}else {
			return dpValue;
		}
	}



	public static Resources getResources() {
		return app().getResources();
	}

	public static ContentResolver getContentResolver() {
		return app().getContentResolver();
	}

	public static ContentResolver resolver() {
		return app().getContentResolver();
	}

	public static boolean isForeground() {
		ActivityManager am = (ActivityManager) app().getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName);
	}

	public static boolean isAppShowing() {
		return isForeground() && !Util.getKeyguardManager().inKeyguardRestrictedInputMode();
	}

	public static boolean isAppTop() {
		if (Util.getKeyguardManager().inKeyguardRestrictedInputMode()) {
			return false;
		}
		ActivityManager am = (ActivityManager) app().getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {

			if (info.processName.equals(packageName)) {
				xlog.INSTANCE.d(info.processName, info.importance);
				return info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
			}
		}
		return false;
	}

	public static void startService(Class<? extends Service> cls, Values values) {
		Intent it = new Intent(App.get(), cls);
		it.setPackage(packageName);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		App.get().startService(it);

	}

	/**
	 * App内广播
	 *
	 * @param action
	 * @param values
	 */
	public static void broadcastApp(String action, Values values) {
		Intent it = new Intent(action);
		it.setPackage(packageName);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		try {
			App.get().sendOrderedBroadcast(it, null);
		} catch (Exception e) {
			xlog.INSTANCE.e(e);
		}
	}


	public static InputStream openStream(Uri uri) throws FileNotFoundException {
		return getContentResolver().openInputStream(uri);
	}

}

package yet.util.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import yet.util.database.Values;
import yet.util.log.xlog;

/**
 * 明确带有Sys后缀的函数, 是面向Android系统的全局回调/调用
 * 不带Sys后缀的是面向App内的
 * <p/>
 * 带有Class参数的, 全部都是针对应用程序内的Class!!!---一般是Activity和Service
 * pendingBroadcast是回调本App组件!!!!----
 * pendingBroadcastSys回调会向Android系统广播
 * broadcast函数只向本App内广播
 * broadcastSys函数向整个Android系统广播
 *
 * @author yangentao@gmail.com
 */
public class IntentUtil {
	public static PendingIntent pendingActivity(Class<? extends Activity> cls, int flag, Values values) {
		Intent it = new Intent(App.INSTANCE.getApp(), cls);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		PendingIntent pi = PendingIntent.getActivity(App.INSTANCE.getApp(), 0, it, flag);
		return pi;
	}

	public static PendingIntent pendingService(Class<? extends Service> cls, int flag, Values values) {
		Intent it = new Intent(App.INSTANCE.getApp(), cls);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		PendingIntent pi = PendingIntent.getService(App.INSTANCE.getApp(), 0, it, flag);
		return pi;
	}

	public static PendingIntent pendingBroadcast(String action, int flag, Values values) {
		Intent it = new Intent(action);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		it.setPackage(App.INSTANCE.getPackageName());
		PendingIntent pi = PendingIntent.getBroadcast(App.INSTANCE.getApp(), 0, it, flag);
		return pi;
	}

	public static PendingIntent pendingBroadcastSys(String action, int flag, Values values) {
		Intent it = new Intent(action);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		PendingIntent pi = PendingIntent.getBroadcast(App.INSTANCE.getApp(), 0, it, flag);
		return pi;
	}

	public static void startService(Class<? extends Service> cls) {
		startService(cls, null, null, null);
	}

	public static void startService(Class<? extends Service> cls, String action) {
		startService(cls, action, null, null);
	}

	public static void startService(Class<? extends Service> cls, String action, Values values) {
		startService(cls, action, null, values);
	}

	public static void startService(Class<? extends Service> cls, Uri uri) {
		startService(cls, null, uri, null);
	}

	public static void startService(Class<? extends Service> cls, String action, Uri uri) {
		startService(cls, action, uri, null);
	}

	public static void startService(Class<? extends Service> cls, String action, Uri uri, Values values) {
		Intent it = new Intent(action, uri, App.INSTANCE.getApp(), cls);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		App.INSTANCE.getApp().startService(it);
	}

	public static void startService(Class<? extends Service> cls, Values values) {
		Intent it = new Intent(App.INSTANCE.getApp(), cls);
		if (values != null) {
			it.putExtras(values.bundle(false));
		}
		App.INSTANCE.getApp().startService(it);
	}

	public static void broadcast(String action) {
		broadcast(action, null, (Bundle) null);
	}

	public static void broadcast(String action, Uri uri) {
		broadcast(action, uri, (Bundle) null);
	}

	public static void broadcast(String action, Uri uri, Values values) {
		broadcast(action, uri, values == null ? null : values.bundle(false));
	}

	public static void broadcast(String action, Values values) {
		broadcast(action, null, values == null ? null : values.bundle(false));
	}

	public static void broadcast(String action, Uri uri, Bundle values) {
		Intent it = new Intent(action, uri);
		if (values != null) {
			it.putExtras(values);
		}
		it.setPackage(App.INSTANCE.getPackageName());
		App.INSTANCE.getApp().sendBroadcast(it);
	}

	public static void broadcastSys(String action) {
		broadcastSys(action, null);
	}

	public static void broadcastSys(String action, Uri uri) {
		broadcastSys(action, uri, (Bundle) null);
	}

	public static void broadcastSys(String action, Uri uri, Values values) {
		broadcastSys(action, uri, values == null ? null : values.bundle(false));
	}

	public static void broadcastSys(String action, Uri uri, Bundle values) {
		Intent it = new Intent(action, uri);
		if (values != null) {
			it.putExtras(values);
		}
		App.INSTANCE.getApp().sendBroadcast(it);
	}

	//
	// public static JsonObject getParams() {
	// return null;
	// }
	public static void dump(Intent it) {
		xlog.INSTANCE.d("Dump Intent");
		if (it == null) {
			xlog.INSTANCE.d("NULL");
			return;
		}
		xlog.INSTANCE.d("ACTION:", it.getAction());
		dump(it.getExtras());
		xlog.INSTANCE.d("Dump Intent  END");
	}

	public static void dump(Bundle b) {
		dump("", b);
	}

	private static void dump(String prefix, Bundle b) {
		if (b != null) {
			for (String key : b.keySet()) {
				Object value = b.get(key);
				if (value instanceof Bundle) {
					xlog.INSTANCE.d(prefix, key);
					dump(prefix + "        ", (Bundle) value);
				} else {
					xlog.INSTANCE.d(prefix, key, value, value == null ? " null " : value.getClass().getSimpleName());
				}

			}
		}
	}
}

package net.yet.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import net.yet.util.app.App;

public class AlarmUtil {

	public static void serviceAt(long time, boolean wakeup, String action, Class<? extends Service> cls) {
		serviceAt(time, wakeup, action, cls, null, null);
	}

	public static void serviceAt(long time, boolean wakeup, String action, Class<? extends Service> cls, Uri uri, Bundle bundle) {
		Intent it = new Intent(action, uri, App.getContext(), cls);
		if (bundle != null) {
			it.putExtras(bundle);
		}
		PendingIntent pi = PendingIntent.getService(App.getContext(), 0, it, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager am = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);
		am.set(wakeup ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC, time, pi);
	}

	public static void broadcastAt(long time, boolean wakeup, String action, Class<? extends BroadcastReceiver> cls) {
		broadcastAt(time, wakeup, action, cls, null, null);
	}

	public static void broadcastAt(long time, boolean wakeup, String action, Class<? extends BroadcastReceiver> cls, Uri uri, Bundle bundle) {
		Intent it = new Intent(action, uri, App.getContext(), cls);
		if (bundle != null) {
			it.putExtras(bundle);
		}
		PendingIntent pi = PendingIntent.getBroadcast(App.getContext(), 0, it, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager am = (AlarmManager) App.getContext().getSystemService(Context.ALARM_SERVICE);
		am.set(wakeup ? AlarmManager.RTC_WAKEUP : AlarmManager.RTC, time, pi);
	}
}

package net.yet.util;

import android.content.Context;
import android.widget.Toast;

import net.yet.util.app.App;

public class ToastUtil {
	public static void show(String msg) {
		showLong(msg);
	}

	public static void show(int res) {
		showLong(res);
	}

	public static void showLong(String msg) {
		show(App.getContext(), msg, Toast.LENGTH_LONG);
	}

	public static void showLong(int res) {
		show(App.getContext(), res, Toast.LENGTH_LONG);
	}

	public static void showLong(Context context, String msg) {
		show(context, msg, Toast.LENGTH_LONG);
	}

	public static void showLong(Context context, int res) {
		show(context, res, Toast.LENGTH_LONG);
	}

	public static void showShort(String msg) {
		show(App.getContext(), msg, Toast.LENGTH_SHORT);
	}

	public static void showShort(int res) {
		show(App.getContext(), res, Toast.LENGTH_SHORT);
	}

	public static void showShort(Context context, String msg) {
		show(context, msg, Toast.LENGTH_SHORT);
	}

	public static void showShort(Context context, int res) {
		show(context, res, Toast.LENGTH_SHORT);
	}

	public static void show(Context context, int msg, int duration) {
		show(context, App.resString(msg), duration);
	}

	public static void show(final Context context, final String msg, final int duration) {
		TaskUtil.fore(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, msg, duration).show();
			}
		});
	}
}

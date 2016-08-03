package net.yet.util;

import android.util.Log;

import net.yet.util.xlog.XPrinter;

public class LogcatPrinter implements XPrinter {

	@Override
	public void println(int priority, String tag, String msg) {
		 Log.println(priority, tag, "" +msg);
	}

	@Override
	public void flush() {
	}

}
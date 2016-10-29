package net.yet.util.log;

import android.util.Log;

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

public class LogLevel {
	public static final String TAG = "xlog";

	public static final int DISABLE = 0;
	public static final int ENABLE_ALL = 1;
	public static final int VERBOSE = Log.VERBOSE;//2
	public static final int DEBUG = Log.DEBUG;//3
	public static final int INFO = Log.INFO;// 4
	public static final int WARN = Log.WARN;//5
	public static final int ERROR = Log.ERROR;//6

	// public static final int ASSERT =7;// Log.ASSERT;

	public static String getText(int level) {
		if (level >= 0 && level < levelText.length) {
			return levelText[level];
		}
		return "Level" + level;
	}

	private static final String[] levelText = new String[]{"DISABLE", "ENABLE_ALL", "VERBOSE", "DEBUG", "INFO", "WARN", "ERROR", "ASSERT",};
}

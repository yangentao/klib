package net.yet.util;

import net.yet.util.event.ActionMerger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 可以使用setLogLevel来设置log输出级别<br/>
 * 默认,在debug版本输出全部log, 在release版本输出info以上级别log(verbos和debug级别的不会被输出)
 *
 * @author yangentao 日志 Logcat日志级别: 0:禁用日志, 1:启用(所有日志), 2:verbose, 3: debug,
 *         4:info, 5:warn, 6:error,
 */
public class xlog {
	public interface XPrinter {
		void flush();

		void println(int priority, String tag, String msg);
	}

	/**
	 * 值与android.util.Log的级别兼容
	 */
	public static class Level {
		public static final int DISABLE = 0;
		public static final int ENABLE_ALL = 1;
		public static final int VERBOSE = 2;// Log.VERBOSE;
		public static final int DEBUG = 4;//3;// Log.DEBUG;
		public static final int INFO = 4;// Log.INFO;
		public static final int WARN = 5;// Log.WARN;
		public static final int ERROR = 6;// Log.ERROR;

		// public static final int ASSERT =7;// Log.ASSERT;

		public static String getText(int level) {
			if (level >= 0 && level < levelText.length) {
				return levelText[level];
			}
			return "Level" + level;
		}

		private static final String[] levelText = new String[]{"DISABLE", "ENABLE_ALL", "VERBOSE", "DEBUG", "INFO", "WARN", "ERROR", "ASSERT",};
	}

	private static String TAG = "xlog";
	private static int logLevel = Level.ENABLE_ALL;
	private static String sep = " ";

	private static XPrinter printer = new LogcatPrinter();

	static {
		if (Util.debug()) {
			logLevel = Level.ENABLE_ALL;
		} else {
			logLevel = Level.INFO;
		}
		logLevel = Level.ENABLE_ALL;
	}

	public static void flush() {
		if (printer != null) {
			printer.flush();
		}
	}

	public static void setSeprator(String s) {
		sep = s;
	}

	public static String getSeprator() {
		return sep;
	}

	public static void setPrinters(XPrinter... ps) {
		printer = new TreePrinter(ps);
	}

	public static void setTag(String tag) {
		TAG = tag;
	}

	/**
	 * @see Level
	 */
	public static void setLogLevel(int level) {
		logLevel = level;
	}

	private static boolean isEnable(int level) {
		return logLevel > 0 && level >= logLevel;
	}

	public static void dIf(boolean condition, Object... args) {
		if (condition) {
			d(args);
		}
	}

	public static void wIf(boolean condition, Object... args) {
		if (condition) {
			w(args);
		}
	}

	public static void eIf(boolean condition, Object... args) {
		if (condition) {
			e(args);
		}
	}

	public static void d(Object... args) {
		println(Level.DEBUG, TAG, args);
	}

	public static void w(Object... args) {
		println(Level.WARN, TAG, args);
	}

	public static void e(Object... args) {
		println(Level.ERROR, TAG, args);
	}

	public static void i(Object... args) {
		println(Level.INFO, TAG, args);
	}

	public static void v(Object... args) {
		println(Level.VERBOSE, TAG, args);
	}

	public static void dTag(String tag, Object... args) {
		println(Level.DEBUG, tag, args);
	}

	public static void wTag(String tag, Object... args) {
		println(Level.WARN, tag, args);
	}

	public static void eTag(String tag, Object... args) {
		println(Level.ERROR, tag, args);
	}

	public static void iTag(String tag, Object... args) {
		println(Level.INFO, tag, args);
	}

	public static void vTag(String tag, Object... args) {
		println(Level.VERBOSE, tag, args);
	}

	private static String getString(Object obj) {
		return Util.toLogString(obj);
	}

	/**
	 * @param priority 见Log.WARN/ERROR等
	 * @param tag
	 * @param args
	 */
	public static void println(int priority, String tag, Object... args) {
		if (printer == null) {
			return;
		}
		if (!isEnable(priority)) {
			return;
		}
		String msg = null;
		if (args.length == 0) {
			msg = "";
		} else {
			StringBuffer sb = new StringBuffer(args.length * 8 + 8);
			boolean first = true;
			for (Object obj : args) {
				if (first) {
					first = false;
				} else {
					if (sep != null) {
						sb.append(sep);
					}
				}
				sb.append(getString(obj));
			}
			msg = sb.toString();
		}

		printer.println(priority, tag, msg);
		autoFlushTrigger.trigger();
	}

	// 2秒延时刷新
	private static ActionMerger autoFlushTrigger = new ActionMerger(2000, new Runnable() {

		@Override
		public void run() {
			flush();
		}
	});

	public static class TreePrinter implements XPrinter {
		private XPrinter[] ps;

		public TreePrinter(XPrinter... ps) {
			this.ps = ps;
		}

		@Override
		public void println(int priority, String tag, String msg) {
			if (ps != null) {
				for (XPrinter p : ps) {
					p.println(priority, tag, msg);
				}
			}
		}

		@Override
		public void flush() {
			if (ps != null) {
				for (XPrinter p : ps) {
					p.flush();
				}
			}

		}
	}

	public static String formatMsg(int priority, String tag, String msg) {
		StringBuilder sb = new StringBuilder(msg.length() + tag.length() + 8);
		String date = getDateString("yyyy-MM-dd HH:mm:ss.SSS ");
		sb.append(date);
		sb.append(String.format(Locale.getDefault(), "%6d ", Thread.currentThread().getId()));
		sb.append(Level.getText(priority));
		sb.append(" [");
		sb.append(tag);
		sb.append("] ");
		sb.append(msg);
		return sb.toString();
	}

	public static class ConsolePrinter implements XPrinter {

		@Override
		public void println(int priority, String tag, String msg) {
			PrintStream ps = System.out;
			if (priority >= Level.ERROR) {
				ps = System.err;
			}
			String s = formatMsg(priority, tag, msg);
			ps.println(s);
		}

		@Override
		public void flush() {
			System.out.flush();
			System.err.flush();

		}
	}

	;

	public static class StreamPrinter implements XPrinter {
		private PrintStream ps;

		public StreamPrinter() {
			this(null);
		}

		public StreamPrinter(PrintStream ps) {
			this.ps = ps;
		}

		public void setStream(PrintStream ps) {
			this.ps = ps;
		}

		@Override
		public void println(int priority, String tag, String msg) {
			if (ps != null) {
				String s = formatMsg(priority, tag, msg);
				ps.println(s);
			}
		}

		@Override
		public void flush() {
			if (ps != null) {
				ps.flush();
			}

		}
	}

	;

	public static class FilePrinter implements XPrinter {
		private BufferedWriter writer = null;

		public FilePrinter(File f, int cacheSize, int limit) {
			try {
				if (f.exists()) {
					FileInputStream is = new FileInputStream(f);
					if (is.available() > limit) {
						f.delete();
					}
					is.close();
					is = null;
				}
				writer = new BufferedWriter(new FileWriter(f, true), cacheSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void flush() {
			try {
				if (writer != null) {
					writer.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void println(int priority, String tag, String msg) {
			if (writer != null) {
				String s = formatMsg(priority, tag, msg);
				try {
					writer.write(s);
					writer.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	;

	private static String getDateString(String pattern) {
		SimpleDateFormat ff = new SimpleDateFormat(pattern, Locale.getDefault());
		return ff.format(new Date());
	}

	// 打印失败信息, 并抛出运行时异常, 终止程序
	public static void fatal(Object... args) {
		e(args);
		throw new RuntimeException("fatal error!");
	}

	public static void debugFail(boolean b, Object... args) {
		if (b) {
			if (Util.debug()) {
				fatal(args);
			} else {
				e(args);
			}
		}
	}
}

package net.yet.util;

import net.yet.util.log.xlog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_TIME_X = "HH:mm:ss.SSS";
	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATE_TIME_NO_SEC = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_DATE_TIME_X = "yyyy-MM-dd HH:mm:ss.SSS";

//	public static String nowDigits() {
//		SimpleDateFormat ff = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault());
//		return ff.format(new Date());
//	}

	private static long tmpFileN = 0;

	synchronized public static String tmpFile() {
		SimpleDateFormat ff = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.getDefault());
		String s = ff.format(new Date());
		++tmpFileN;
		s += "_" + Long.toString(tmpFileN);
		return s;
	}

	public static Date parse(String format, String dateStr) {
		SimpleDateFormat ff = new SimpleDateFormat(format, Locale.getDefault());
		try {
			return ff.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
			xlog.INSTANCE.e(ex);
		}
		return null;
	}

	/**
	 * for example : yyyy-MM-dd HH:mm:ss
	 *
	 * @param pattern
	 * @return
	 */
	public static String now(String pattern) {
		SimpleDateFormat ff = new SimpleDateFormat(pattern, Locale.getDefault());
		return ff.format(new Date());
	}

	public static String now() {
		return now(FORMAT_DATE_TIME);
	}

	public static String nowX() {
		return now(FORMAT_DATE_TIME_X);
	}

	public static String dateTimeX(long date) {
		return format(date, FORMAT_DATE_TIME_X);
	}

	public static String dateTime(long date) {
		return format(date, FORMAT_DATE_TIME);
	}

	public static String dateTimeNoSec(long date) {
		return format(date, FORMAT_DATE_TIME_NO_SEC);
	}

	public static String dateTime() {
		return now(FORMAT_DATE_TIME);
	}

	public static String date(long date) {
		return format(date, FORMAT_DATE);
	}

	public static String date() {
		return now(FORMAT_DATE);
	}

	public static String time(long date) {
		return format(date, FORMAT_TIME);
	}

	public static String time() {
		return now(FORMAT_TIME);
	}

	public static String timeX(long date) {
		return format(date, FORMAT_TIME_X);
	}

	public static String timeX() {
		return now(FORMAT_TIME_X);
	}

	public static String format(long date, String pattern) {
		SimpleDateFormat ff = new SimpleDateFormat(pattern, Locale.getDefault());
		return ff.format(new Date(date));
	}

	// 今天的返回时间 16:30,
	// 今年的返回日期 12-30
	// 往年的返回: 2014-12-30
	public static String shortString(long date) {
		Calendar cNow = Calendar.getInstance();
		Calendar cDate = Calendar.getInstance();
		cDate.setTimeInMillis(date);

		if (cNow.get(Calendar.YEAR) != cDate.get(Calendar.YEAR)) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			return df.format(new Date(date));
		}
		if (cNow.get(Calendar.DAY_OF_YEAR) != cDate.get(Calendar.DAY_OF_YEAR)) {
			SimpleDateFormat df = new SimpleDateFormat("M-d", Locale.getDefault());
			return df.format(new Date(date));
		}
		SimpleDateFormat df = new SimpleDateFormat("H:mm", Locale.getDefault());
		return df.format(new Date(date));
	}

	/**
	 * 将12345秒==> 12小时30分40秒
	 *
	 * @param total
	 * @return
	 */
	public static String durationString(long total) {
		if (total < 60) {
			return StrBuilder.build(total, "秒");
		}
		if (total < 60 * 60) {
			return StrBuilder.build(total / 60, "分", total % 60, "秒");
		}
		return StrBuilder.build(total / 3600, "时", (total % 3600) / 60, "分", total % 60, "秒");
	}
}

package net.yet.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

/**
 * 字符串构造, 类似StringBuilder <br/>
 *
 * @author yangentao
 * @important 这个类的目的不是为性能, 而是为易用, 尽管内部使用StringBuffer做代理; 对性能要求高时建议使用StringBuffer
 */
public class StrBuilder {
	private static final String NULL_STR = "";
	private StringBuilder sb;

	private String sep = null;
	private String nullStr = NULL_STR;

	public static String build(Object... args) {
		StrBuilder builder = new StrBuilder();
		builder.append(args);
		return builder.toString();
	}

	public static String buildS(String... args) {
		StrBuilder builder = new StrBuilder();
		builder.appendS(args);
		return builder.toString();
	}

	public StrBuilder() {
		this(128, null, NULL_STR);
	}

	public StrBuilder(int capcity) {
		this(capcity, null, NULL_STR);
	}

	public StrBuilder(int capcity, String sep) {
		this(capcity, sep, NULL_STR);
	}

	public StrBuilder(int capcity, String sep, String nullStr) {
		sb = new StringBuilder(capcity);
		this.sep = sep;
		this.nullStr = nullStr;
	}

	public void setLength(int len) {
		sb.setLength(len);
	}

	public void clear() {
		setLength(0);
	}

	public void popOne() {
		pop(1);
	}

	public void pop(int n) {
		int len = length();
		len = len - n;
		if (len < 0) {
			len = 0;
		}
		setLength(len);
	}

	public StringBuilder getStringBuilder() {
		return sb;
	}

	public void setNullStr(String s) {
		if (s == null) {
			s = "";
		}
		this.nullStr = s;
	}

	public void setSeperator(String sep) {
		this.sep = sep;
	}

	public int length() {
		return sb.length();
	}

	public boolean isEmpty() {
		return length() == 0;
	}

	/**
	 * 注意 appendChar不会使用seprator和nullStr!!!!!
	 *
	 * @param chs
	 * @return
	 */
	public StrBuilder appendChar(char... chs) {
		for (char ch : chs) {
			sb.append(ch);
		}
		return this;
	}

	public StrBuilder appendAll(Collection<String> args) {
		for (String s : args) {
			appendStr(s);
		}
		return this;
	}

	public StrBuilder appendS(String... args) {
		for (String s : args) {
			appendStr(s);
		}
		return this;
	}

	public StrBuilder appendLN() {
		return appendStr("\r\n");
	}

	public StrBuilder ln() {
		return appendStr("\r\n");
	}

	public StrBuilder append(String... args) {
		return appendS(args);
	}

	public StrBuilder append(Object... args) {
		for (Object obj : args) {
			appendStr(getString(obj));
		}
		return this;
	}

	private StrBuilder appendStr(String s) {
		if (sep != null && sep.length() > 0 && sb.length() > 0) {
			sb.append(sep);
		}
		if (s == null) {
			s = nullStr;
		}
		sb.append(s);
		return this;
	}

	private String getString(Object obj) {
		if (obj == null) {
			return nullStr;
		} else if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Number) {
			return obj.toString();
		} else if (obj instanceof Throwable) {
			Throwable tr = (Throwable) obj;
			StringWriter sw = new StringWriter(128);
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			return sw.toString();
		}

		// else
		return obj.toString();
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}

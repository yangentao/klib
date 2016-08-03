package net.yet.util;

/**
 * Created by entaoyang@163.com on 16/4/26.
 */
public class Hex {
	private static final String DICT = "0123456789abcdef";

	public static String encode(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		StringBuilder ret = new StringBuilder(2 * bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			int b;
			b = 0x0f & (bytes[i] >> 4);
			ret.append(DICT.charAt(b));
			b = 0x0f & bytes[i];
			ret.append(DICT.charAt(b));
		}
		return ret.toString();
	}

	public static byte[] decode(String hexString) {
		if (hexString == null) {
			return null;
		}
		int strLen = hexString.length();
		if (strLen == 0) {
			return new byte[0];
		}
		if (strLen % 2 != 0) {
			throw new IllegalArgumentException("字符串长度必须是2的倍数");
		}
		String s = hexString.toLowerCase();

		byte[] bytes = new byte[strLen / 2];
		for (int i = 0; i < strLen; i += 2) {
			int hi = toByte(s.charAt(i));
			int lo = toByte(s.charAt(i + 1));
			int n = ((hi << 4) & 0xf0) | (lo & 0x0f);
			byte b = (byte) n;
			bytes[i / 2] = b;
		}
		return bytes;
	}

	private static int toByte(char ch) {
		if (ch >= '0' && ch <= '9') {
			return (ch - '0');
		}
		if (ch >= 'a' && ch <= 'f') {
			return (ch - 'a');
		}
		throw new IllegalArgumentException("不合法的字符" + ch);
	}
}

package yet.util;

/**
 * Created by entaoyang@163.com on 2016-07-27.
 */


public class Bit {
	public static boolean has(int value, int flag) {
		return (value & flag) != 0;
	}

	public static int remove(int value, int flag) {
		return value & ~flag;
	}
}

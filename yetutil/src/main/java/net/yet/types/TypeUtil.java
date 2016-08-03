package net.yet.types;

/**
 * Created by entaoyang@163.com on 2016-07-29.
 */

public class TypeUtil {

	public static Enum<?> makeEnum(Class<?> enumClass, String value) {
		if (enumClass.isEnum()) {
			return Enum.valueOf((Class<? extends Enum>) enumClass, value);
		}
		return null;
	}
}

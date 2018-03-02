package yet.ref;

/**
 * Created by entaoyang@163.com on 2017-03-13.
 */

public class EnumUtil {
	public static Enum<?> valueOf(Class<?> enumClass, String value) {
		if (enumClass.isEnum()) {
			return Enum.valueOf((Class<? extends Enum>) enumClass, value);
		}
		return null;
	}
}

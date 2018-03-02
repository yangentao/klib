package yet.util;

import java.util.Collection;
import java.util.Map;

import kotlin.Unit;

public class Pred {
	/**
	 * 0, false, "", null 时, 返回true;
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isFalse(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof Boolean) {
			Boolean b = (Boolean) obj;
			return !b.booleanValue();
		}
		if (obj instanceof String) {
			return ((String) obj).length() == 0;
		}
		if (obj instanceof Number) {
			Number n = (Number) obj;
			return n.longValue() == 0;
		}
		return false;
	}

	/**
	 * 0, false, "", null, 空的集合, 返回true
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isFalseX(Object obj) {
		if (obj == null) {
			return true;
		}
		if(obj == Unit.INSTANCE) {
			return true ;
		}
		if (obj instanceof Boolean) {
			Boolean b = (Boolean) obj;
			return !b.booleanValue();
		}
		if (obj instanceof String) {
			return ((String) obj).length() == 0;
		}
		if (obj instanceof Number) {
			Number n = (Number) obj;
			return n.longValue() == 0;
		}
		if (obj instanceof Collection) {
			Collection<?> c = (Collection<?>) obj;
			return c.isEmpty();
		}
		if (obj instanceof Map) {
			Map<?, ?> m = (Map<?, ?>) obj;
			return m.isEmpty();
		}
		return false;
	}

	public static boolean isTrue(Object obj) {
		return !isFalse(obj);
	}

	public static boolean isTrueX(Object obj) {
		return !isFalseX(obj);
	}

	/**
	 * 返回第一个是"true"的值. 或null
	 * 0, false, "", null被判定为false
	 * 
	 * @param objs
	 * @return
	 */
	public static Object OR(Object... objs) {
		for (Object obj : objs) {
			if (isTrue(obj)) {
				return obj;
			}
		}
		return null;
	}

	public static Object ORX(Object... objs) {
		for (Object obj : objs) {
			if (isTrueX(obj)) {
				return obj;
			}
		}
		return null;
	}

	public static String ORS(String... ss) {
		String last = null;
		for (String s : ss) {
			last = s;
			if (s != null && s.length() > 0) {
				return s;
			}
		}
		return last;
	}

	/**
	 * 如果有一个不是空(null, 0, false, ""), 则返回true.
	 * 
	 * @param objs
	 * @return
	 */
	public static boolean or(Object... objs) {
		for (Object obj : objs) {
			if (isTrue(obj)) {
				return true;
			}
		}
		return false;
	}

	public static boolean orX(Object... objs) {
		for (Object obj : objs) {
			if (isTrueX(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 都不是 0, "", false, null 则返回true
	 * 
	 * @param objs
	 * @return
	 */
	public static boolean and(Object... objs) {
		for (Object obj : objs) {
			if (isFalse(obj)) {
				return false;
			}
		}
		return objs.length > 0;
	}

	public static boolean andX(Object... objs) {
		for (Object obj : objs) {
			if (isFalseX(obj)) {
				return false;
			}
		}
		return objs.length > 0;
	}

}

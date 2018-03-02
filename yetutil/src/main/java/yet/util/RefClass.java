package yet.util;

/**
 * 先判断hasClass, 再执行其他方法
 * 如果给定的cls是null , get和invoke方法会返回null, set方法会返回false
 * 
 * @author yangentao@gmail.com
 *
 */
public class RefClass {
	private Class<?> cls = null;

	/**
	 * 不会返回null, 但是cls成员可能是null
	 * 
	 * @param clsName
	 * @return
	 */
	public static RefClass from(String clsName) {
		Class<?> cls = null;
		try {
			cls = Class.forName(clsName);
		} catch (ClassNotFoundException e) {
		}
		return new RefClass(cls);
	}

	public static RefClass from(Class<?> cls) {
		return new RefClass(cls);
	}

	public RefClass(Class<?> cls) {
		this.cls = cls;
	}

	public Class<?> getCls() {
		return cls;
	}

	public boolean hasClass() {
		return cls != null;
	}

	public boolean hasMethod(String name, Class<?>... argTypes) {
		if (cls != null) {
			return RefUtil.hasMethod(cls, name, argTypes);
		}
		return false;
	}

	public boolean hasField(String name) {
		if (cls != null) {
			return RefUtil.hasField(cls, name);
		}
		return false;
	}

	public Object get(String name) {
		return cls == null ? null : get(name, null);
	}

	public Object get(String name, Object defVal) {
		try {
			return cls == null ? null : RefUtil.getStaticE(this.cls, name);
		} catch (Exception e) {
		}
		return defVal;
	}

	public boolean set(String name, Object value) {
		try {
			if (cls != null) {
				RefUtil.setStaticE(this.cls, name, value);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public Object invoke1(String name, Class<?> argType, Object arg) {
		return invoke(name, new Class<?>[] { argType }, arg);
	}

	public Object invoke(String name, Class<?>[] argTypes, Object... args) {
		try {
			return cls == null ? null : RefUtil.invokeStaticE(cls, name, argTypes, args);
		} catch (Exception e) {
		}
		return null;
	}

	public Object invoke(String name, Object... args) {
		try {
			return cls == null ? null : RefUtil.invokeStaticE(cls, name, args);
		} catch (Exception e) {
		}
		return null;
	}

}

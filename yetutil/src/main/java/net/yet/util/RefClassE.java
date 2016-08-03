package net.yet.util;

public class RefClassE {
	private Class<?> cls = null;

	public RefClassE(Class<?> cls) {
		this.cls = cls;
	}

	/**
	 * 没找到类会返回null
	 * 
	 * @param clsName
	 * @return
	 */
	public static RefClassE from(String clsName) {
		Class<?> cls = null;
		try {
			cls = Class.forName(clsName);
		} catch (ClassNotFoundException e) {
		}
		return new RefClassE(cls);
	}

	public Class<?> getCls() {
		return cls;
	}

	public boolean hasClass() {
		return cls != null;
	}

	public Object getE(String name) throws Exception {
		return RefUtil.getStaticE(this.cls, name);
	}

	public void set(String name, Object value) throws Exception {
		RefUtil.setStaticE(this.cls, name, value);
	}

	public Object invoke(String name, Class<?>[] argTypes, Object... args) throws Exception {
		return RefUtil.invokeStaticE(cls, name, argTypes, args);
	}

	public Object invoke(String name, Object... args) throws Exception {
		return RefUtil.invokeStaticE(cls, name, args);
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
}

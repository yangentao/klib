package yet.util;

import yet.util.app.App;

/**
 * 如果给定的obj是null , get和invoke方法会返回null, set方法会返回false
 * 
 * @author yangentao@gmail.com
 *
 */
public class RefObject {
	private Object obj = null;

	/**
	 * 没有找打service 也不会返回null, 通过hasObject判断
	 * 
	 * @param name
	 * @return
	 */
	public static RefObject fromService(String name) {
		Object obj = App.INSTANCE.getApp().getSystemService(name) ;
		return new RefObject(obj);
	}

	public static RefObject from(Object obj) {
		return new RefObject(obj);
	}

	public RefObject(Object obj) {
		this.obj = obj;
	}

	public Object getObject() {
		return obj;
	}

	public boolean hasObject() {
		return obj != null;
	}

	public boolean hasMethod(String name, Class<?>... argTypes) {
		if (obj != null) {
			return RefUtil.hasMethod(obj.getClass(), name, argTypes);
		}
		return false;
	}

	public boolean hasField(String name) {
		if (obj != null) {
			return RefUtil.hasField(obj.getClass(), name);
		}
		return false;
	}

	public Object get(String name) {
		try {
			return obj == null ? null : RefUtil.getE(this.obj, name);
		} catch (Throwable e) {
		}
		return null;
	}

	public Object get(String name, Object defVal) {
		try {
			return RefUtil.getE(this.obj, name);
		} catch (Throwable e) {
		}
		return defVal;
	}

	public boolean set(String name, Object value) {
		try {
			if (obj != null) {
				RefUtil.setE(this.obj, name, value);
				return true;
			}
		} catch (Throwable e) {
		}
		return false;
	}

	public Object invoke1(String name, Class<?> argType, Object arg) {
		return invoke(name, new Class<?>[] { argType }, arg);
	}

	public Object invoke(String name, Class<?>[] argTypes, Object... args) {
		try {
			return obj == null ? null : RefUtil.invokeObjectE(obj, name, argTypes, args);
		} catch (Exception e) {
		}
		return null;
	}

	public Object invoke(String name, Object... args) {
		try {
			return obj == null ? null : RefUtil.invokeObjectE(obj, name, args);
		} catch (Exception e) {
		}
		return null;
	}

}

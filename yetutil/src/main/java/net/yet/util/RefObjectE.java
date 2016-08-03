package net.yet.util;

/**
 * 先判断hasObject是否返回true, 再调用其他方法
 * 
 * @author yangentao@gmail.com
 *
 */
public class RefObjectE {
	private Object obj = null;

	/**
	 * 没有找打service 也不会返回null
	 * 
	 * @param name
	 * @return
	 */
	public static RefObjectE fromService(String name) {
		Object obj = Util.getService(name);
		return new RefObjectE(obj);
	}

	public RefObjectE(Object obj) {
		this.obj = obj;
	}

	public Object getObject() {
		return obj;
	}

	public boolean hasObject() {
		return obj != null;
	}

	public Object get(String name) throws Exception {
		return RefUtil.getE(this.obj, name);
	}

	public void set(String name, Object value) throws Exception {
		RefUtil.setE(this.obj, name, value);
	}

	public Object invoke(String name, Class<?>[] argTypes, Object... args) throws Exception {
		return RefUtil.invokeObjectE(obj, name, argTypes, args);
	}

	public Object invoke(String name, Object... args) throws Exception {
		return RefUtil.invokeObjectE(obj, name, args);
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
}

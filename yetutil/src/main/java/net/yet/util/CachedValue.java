package net.yet.util;

public abstract class CachedValue<T> {
	protected T value = null;
	protected boolean inited = false;

	abstract protected T onInit();

	public T get() {
		if (!inited) {
			synchronized (this) {
				if (!inited) {
					value = onInit();
					inited = true;
				}
			}
		}
		return this.value;
	}

	synchronized public void clear() {
		value = null;
		inited = false;
	}

	synchronized public void clearAndInit() {
		clear();
		get();
	}

	synchronized public void set(T value) {
		this.value = value;
		inited = true;
	}

	/**
	 * @return 是否执行过初始化方法, 或被set; ---有可能被set一个null值, 但是仍然返回true
	 */
	public boolean hasValue() {
		return inited;
	}

	/**
	 * 等价与 get() == null
	 * 值是否==null; 如果没有初始化过,会进行初始化,
	 * 
	 * @return 值是否==null
	 */
	public boolean isNull() {
		return get() == null;
	}

	/**
	 * @return get() != null;
	 */
	public boolean notNull() {
		return get() != null;
	}

	public void backInit() {
		TaskUtil.backFore(new BackFore() {
			T newValue;

			@Override
			public void onFore() {
				set(newValue);
			}

			@Override
			public void onBack() {
				newValue = onInit();
			}
		});
	}
}

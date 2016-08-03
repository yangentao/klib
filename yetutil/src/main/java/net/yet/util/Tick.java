package net.yet.util;

public class Tick {
	private static final String TAG = "tick";

	private long start_time = 0;
	private long end_time = 0;

	public Tick() {
		start();
	}

	/**
	 * 开始计时
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-6
	 */

	public void start() {
		start_time = System.currentTimeMillis();
	}

	/**
	 * 停止计时
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-5
	 */

	private void end() {
		end_time = System.currentTimeMillis();
	}

	/**
	 * 结束计时
	 * 
	 * @Description:
	 * @param prefix
	 *            打印信息
	 * @return
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-6
	 */
	public void end(String prefix) {
		end(0, prefix);
	}

	/**
	 * @param warnLevel
	 *            计时大于这个值(单位毫秒)时, 用警告输出
	 * @param prefix
	 */
	public void end(long warnLevel, String prefix) {
		end();
		long tick = getTick();
		if (null == prefix) {
			prefix = "";
		}
		if (warnLevel > 0 && tick > warnLevel) {
			xlog.wTag(TAG, "[TimeTick]", prefix, ":", tick, "(expect<", warnLevel, ")");
		} else {
			xlog.dTag(TAG, "[TimeTick]", prefix, ":", tick);
		}
		start();
	}

	/**
	 * 返回开始和结束的时间差
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-6
	 */

	public long getTick() {
		return end_time - start_time;
	}

	private static Tick t = new Tick();

	/**
	 * 开始计时
	 * 
	 * @Description:
	 * @return 计时器
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-6
	 */
	public static Tick global() {
		return t;
	}
}

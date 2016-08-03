package net.yet.util;

/**
 * 通用 的回调处理
 * 
 * @author yangentao@gmail.com
 *
 */
public interface OnCallback {

	/**
	 * @param success
	 *            是否成功
	 * @param code
	 *            一般0表示成功, 具体情况根据使用场景定义
	 * @param msg
	 *            错误信息
	 */
	public void onCallback(boolean success, int code, String msg);

}

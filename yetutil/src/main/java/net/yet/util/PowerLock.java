package net.yet.util;

import android.content.Context;
import android.os.PowerManager;

public class PowerLock {
	private PowerManager.WakeLock lock;

	public PowerLock() {
		PowerManager pm = (PowerManager) Util.getService(Context.POWER_SERVICE);
		lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PowerLock");
		lock.setReferenceCounted(false);
	}

	public static PowerLock need(int millSeconds) {
		PowerLock lk = new PowerLock();
		lk.acquire(millSeconds);
		return lk;
	}

	/**
	 * 超时自动释放
	 * 
	 * @param millSeconds
	 */
	public void acquire(int millSeconds) {
		lock.acquire(millSeconds);
	}

	public void acquire() {
		lock.acquire();
	}

	public void release() {
		if (lock.isHeld()) {
			lock.release();
		}
	}

	public boolean isHeld() {
		return lock.isHeld();
	}

}

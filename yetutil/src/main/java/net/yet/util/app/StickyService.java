package net.yet.util.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import net.yet.util.Util;

/**
 * onStartCommand返回 START_STICKY, 这个值可以改写
 * onStartCommand中解析参数, 回调onAction
 * 
 * @author yangentao@gmail.com
 *
 */
public abstract class StickyService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
		onHandleIntent(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		PowerManager pm = Util.getPowerManager();
		WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StickyService.lock");
		lock.setReferenceCounted(false);
		lock.acquire(5000);
		onStart(intent, startId);
		lock.release();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 在主线程运行, 如果需要长时间任务, 应该自己申请WakeLock, 并在子线程中完成
	 * 
	 * @param intent
	 */
	protected abstract void onHandleIntent(Intent intent);
}

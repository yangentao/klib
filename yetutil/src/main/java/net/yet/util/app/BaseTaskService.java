package net.yet.util.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import net.yet.util.IdGen;
import net.yet.util.PowerLock;
import net.yet.util.RunTask;
import net.yet.util.TaskQueue;
import net.yet.util.xlog;

/**
 * IntentService有个问题, 如果后面的Intent提前处理完, 调用了stopSelf(int startId), 则, 整个service被关闭了.
 * 启动一个IntentService 4次, startId分别是: 1, 2, 3, 4
 * 如果 4提前完成并调用了stopSelf(4), 则, 前面的任务1,2,3都被关闭了.
 * <p/>
 * 因此, 才有了这个类BaseTaskService,  TaskService, SeqTaskService
 */
public abstract class BaseTaskService extends Service {
    protected final String GROUP_TASK = getClass().getSimpleName() + ".task.group." + IdGen.gen();
    private PowerLock lock;
    private TaskQueue taskHandler;
    private boolean redely = false;

    @Override
    public void onCreate() {
        super.onCreate();
        taskHandler = new TaskQueue("basetaskservice");
        this.redely = redelivery();
        if (needPowerLock()) {
            lock = new PowerLock();
            lock.acquire();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RunTask.cancel(GROUP_TASK);
        taskHandler.quit();
        if (lock != null) {
            lock.release();
        }
        xlog.d("Service Destroy");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO 子类实现
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            mayStopSelf();
            return redely ? START_REDELIVER_INTENT : START_NOT_STICKY;
        }
        if (!needPocess(intent, startId)) {
            mayStopSelf();
            return redely ? START_REDELIVER_INTENT : START_NOT_STICKY;
        }
        onStart(intent, startId);
        return redely ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean redelivery() {
        return false;
    }

    protected boolean needPowerLock() {
        return true;
    }

    public TaskQueue getHandler() {
        return taskHandler;
    }

    protected boolean needPocess(Intent intent, int startId) {
        return true;
    }

    protected abstract void mayStopSelf();

    public abstract void finish(final int startId);

    /**
     * 子线程调用, 处理完成后, 调用finish(startId)来结束任务
     *
     * @param intent
     * @param startId
     */
    protected abstract void onProcess(Intent intent, int startId);
}

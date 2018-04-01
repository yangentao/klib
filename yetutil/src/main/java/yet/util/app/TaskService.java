package yet.util.app;

import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

import yet.util.RunTask;
import yet.util.TaskUtil;

/**
 * 调用onProcess来处理任务, 调用finish任务来结束任务.
 * onProcess是有序被回调的, 但是finish方法可以无序.
 *
 * @author yangentao@gmail.com
 * @see BaseTaskService
 */
public abstract class TaskService extends BaseTaskService {
    private Set<Integer> set = new HashSet<Integer>();

    final public void onStart(final Intent intent, final int startId) {
        // 添加任务, 并排队执行
        synchronized (set) {
            set.add(startId);
        }
        getHandler().back(new RunTask() {

            @Override
            protected void onRun() throws Exception {
                onProcess(intent, startId);
            }
        }).addGroup(getGROUP_TASK());
    }

    ;

    @Override
    final protected void mayStopSelf() {
        TaskUtil.foreDelay(getStopDelay(), new Runnable() {

            @Override
            public void run() {
                if (set.isEmpty()) {
                    stopSelf();
                }
            }
        });
    }

    /**
     * 没有任务后, 多长时间关闭服务,   在关闭服务前, 如果有新任务进来, 会撤销关闭服务的计划
     *
     * @return 毫秒
     */
    protected int getStopDelay() {
        return 1000;
    }

    /**
     * 结束一个任务, 可以多次调用.
     *
     * @param startId
     */
    @Override
    final public void finish(final int startId) {
        synchronized (set) {
            set.remove(startId);
        }
        mayStopSelf();
    }
}

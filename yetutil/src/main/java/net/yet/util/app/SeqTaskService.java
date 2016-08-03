package net.yet.util.app;

import android.content.Intent;

import net.yet.util.RunTask;
import net.yet.util.TaskUtil;
import net.yet.util.Util;
import net.yet.util.xlog;

import java.util.LinkedList;

/**
 * 直到一个任务处理完, 才进行下一个任务的处理.
 * 调用finish方法来结束一个任务
 *
 * @see BaseTaskService
 * @author yangentao@gmail.com
 *
 */
public abstract class SeqTaskService extends BaseTaskService {
	static class TaskInfo {
		final public int startId;
		final public Intent intent;

		public TaskInfo(Intent intent, int startId) {
			this.startId = startId;
			this.intent = intent;
		}

		@Override
		public int hashCode() {
			return startId;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof TaskInfo) {
				return startId == ((TaskInfo) o).startId;
			}
			return false;
		}
	}

	private final LinkedList<TaskInfo> taskQueue = new LinkedList<TaskInfo>();
	private TaskInfo lastRunningTask = null;

	@Override
	public void onStart(final Intent intent, final int startId) {
		// 进队列
		TaskInfo task = new TaskInfo(intent, startId);
		synchronized (taskQueue) {
			taskQueue.offerLast(task);
		}
		dispatchTask();
	}

	private TaskInfo pollTask() {
		synchronized (taskQueue) {
			return taskQueue.pollFirst();
		}
	}

	private void dispatchTask() {
		if (lastRunningTask != null) {
			// xlog.d("已经有任务在运行");
			return;
		}
		final TaskInfo task = pollTask();
		if (task != null) {
			lastRunningTask = task;
			TaskUtil.back(new RunTask() {

				@Override
				protected void onRun() throws Exception {
					onProcess(task.intent, task.startId);
				}
			}).addGroup(GROUP_TASK);
		} else {
			mayStopSelf();
		}
	}

	@Override
	final public void finish(int startId) {
		if (lastRunningTask == null) {
			xlog.e("没有正在执行的任务");
			return;
		}
		Util.debugFail(lastRunningTask.startId != startId, "要结束的task和正在运行的不一样!");

		lastRunningTask = null;
		dispatchTask();
	}

	@Override
	final protected void mayStopSelf() {
		TaskUtil.foreDelay(1000, new Runnable() {

			@Override
			public void run() {
				if (lastRunningTask == null && taskQueue.isEmpty()) {
					stopSelf();
				}
			}
		});
	}

}

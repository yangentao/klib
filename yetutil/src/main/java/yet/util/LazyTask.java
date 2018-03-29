package yet.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import yet.util.log.xlog;

/**
 * 延时执行回调, 在回调执行前, 每一次schedule或refresh都会重新计算回调的执行时间.
 * 时间到的时候, 回调被执行, 然后从任务中删除
 * 
 * 比如, 写数据库, 当没有写操作N毫秒后关闭数据库
 * 
 * @author yangentao@gmail.com
 *
 */
public class LazyTask {
	private static class Task {
		public long delay;
		public long scheduleTime;
		public Runnable task;
	}

	private static Map<String, Task> map = new ConcurrentHashMap<String, Task>();

	synchronized public static void schedule(String taskname, int millSec, Runnable taskRun) {
		Task task = map.get(taskname);
		if (task == null) {
			task = new Task();
			task.delay = millSec;
			task.task = taskRun;
			task.scheduleTime = System.currentTimeMillis();
			map.put(taskname, task);
			TaskRunnable tr = new TaskRunnable(taskname);
			tr.run();
		} else {
			task.delay = millSec;
			task.task = taskRun;
			task.scheduleTime = System.currentTimeMillis();
		}
	}

	static class TaskRunnable implements Runnable {
		private String taskname;

		public TaskRunnable(String name) {
			this.taskname = name;
		}

		@Override
		public void run() {
			Task t = map.get(taskname);
			if (t != null) {
				long delta = t.scheduleTime + t.delay - System.currentTimeMillis();
				if (delta <= 0) {
					xlog.INSTANCE.d("lazy task callback!");
					map.remove(taskname);
					t.task.run();
				} else {
					TaskUtil.foreDelay(delta + 50, this);
				}
			}
		}
	}

	public static void schedule(Class<?> cls, int millSec, Runnable taskRun) {
		schedule(cls.getName(), millSec, taskRun);
	}

	public static void refresh(Class<?> cls) {
		refresh(cls.getName());
	}

	synchronized public static void refresh(final String taskname) {
		Task task = map.get(taskname);
		if (task != null) {
			task.scheduleTime = System.currentTimeMillis();
		}

	}
}

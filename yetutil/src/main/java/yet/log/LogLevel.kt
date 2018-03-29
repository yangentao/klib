package yet.util.log;

import android.util.Log

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

enum class LogLevel2(val n: Int) {
	DISABLE(0), ENABLE_ALL(1), VERBOSE(Log.VERBOSE), DEBUG(Log.DEBUG), INFO(Log.INFO), WARN(Log.WARN), ERROR(Log.ERROR);

	fun ge(level: LogLevel2): Boolean {
		return this.ordinal >= level.ordinal
	}
}

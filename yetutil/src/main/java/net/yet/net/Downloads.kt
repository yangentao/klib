package net.yet.net

import net.yet.util.Msg
import net.yet.util.Progress
import net.yet.util.TaskQueue
import java.io.File
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-09-04.
 */

object Downloads {
	val queue = TaskQueue("downloads")
	val set = HashSet<String>()
	fun addTask(msg: String, url: String, saveTo: File, progress: Progress? = null) {
		if (!set.add(url)) {
			return
		}
		queue.back {
			val r = Http(url).download(saveTo, progress)
			set.remove(url)
			Msg(msg).argB(r.OK).argS(url, saveTo.absolutePath).fire()

		}
	}
}
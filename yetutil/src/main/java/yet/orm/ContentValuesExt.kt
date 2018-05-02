package yet.orm

import android.content.ContentValues
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import yet.ext.customName
import yet.yson.YsonArray
import yet.yson.YsonObject
import java.io.File
import java.util.*
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-10.
 */

fun buildContentValues(args: Collection<Pair<KMutableProperty<*>, Any?>>): ContentValues {
	val cv = ContentValues(args.size)
	for ((p, v) in args) {
		cv.putAny(p.customName, v)
	}
	return cv
}

fun buildContentValues(vararg args: Pair<KMutableProperty<*>, Any?>): ContentValues {
	val cv = ContentValues(args.size)
	for ((p, v) in args) {
		cv.putAny(p.customName, v)
	}
	return cv
}

fun ContentValues.putAny(name: String, value: Any?) {
	if (value == null) {
		this.putNull(name)
	} else {
		when (value) {
			is Boolean -> this.put(name, if (value) 1 else 0)
			is Byte -> this.put(name, value.toLong())
			is Short -> this.put(name, value.toLong())
			is Int -> this.put(name, value.toLong())
			is Long -> this.put(name, value.toLong())
			is Float -> this.put(name, value.toDouble())
			is Double -> this.put(name, value.toDouble())
			is ByteArray -> this.put(name, value)
			is Char -> this.put(name, value.toString())
			is String -> this.put(name, value )
			is YsonArray -> this.put(name, value.toString())
			is YsonObject -> this.put(name, value.toString())
			is JSONArray -> this.put(name, value.toString())
			is JSONObject -> this.put(name, value.toString())
			is File -> this.put(name, value.absolutePath)
			is Uri -> this.put(name, value.toString())
			is UUID -> this.put(name, value.toString())
			is Enum<*> -> this.put(name, value.toString())
			else -> this.put(name, value.toString())
		}
	}
}

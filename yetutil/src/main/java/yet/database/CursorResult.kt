package yet.util.database

import android.database.Cursor
import org.json.JSONArray
import org.json.JSONObject
import yet.json.putNull
import yet.util.log.xlog
import yet.yson.YsonArray
import yet.yson.YsonObject
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/13.
 */

//所有属性和方法, 都会关闭cursor
class CursorResult(c: Cursor?) {
	val cursor: Cursor = c ?: SafeCursor(null)

	inline fun each(block: (Cursor) -> Unit) {
		try {
			while (cursor.moveToNext()) {
				block(cursor)
			}
		} catch(ex: Throwable) {
			ex.printStackTrace()
		} finally {
			close()
		}
	}


	/**
	 * 是否存在符合条件的记录

	 * @return
	 */
	val exist: Boolean get () {
		val n = cursor.count
		close()
		return n > 0
	}

	val columnNames: Set<String> get() {
		val set = HashSet<String>(32)
		try {
			set += cursor.columnNames
		} catch (e: Throwable) {
			e.printStackTrace()
		}
		close()
		return set
	}

	fun close() {
		cursor.close()
	}

	fun countAndClose(): Int {
		val n = cursor.count
		close()
		return n
	}


	/**
	 * @return 不会返回null
	 * *
	 * @formatter:off Cursor.
	 * * FIELD_TYPE_NULL = 0
	 * * FIELD_TYPE_INTEGER = 1
	 * * FIELD_TYPE_FLOAT = 2
	 * * FIELD_TYPE_STRING = 3
	 * * FIELD_TYPE_BLOB = 4
	 * *
	 * @formatter:on
	 */
	fun columnTypes(): Map<String, Int> {
		val map = HashMap<String, Int>(32)
		try {
			if (cursor.moveToNext()) {
				for (i in 0..cursor.columnCount - 1) {
					val name = cursor.getColumnName(i)
					val type = cursor.getType(i)
					map.put(name!!, type)
				}
			}

		} catch(e: Throwable) {
			e.printStackTrace()
		}
		close()
		return map
	}


	/**
	 * 查找第一条记录, 没找到返回null

	 * @return
	 */
	fun jsonObject(): YsonObject? {
		var jo: YsonObject? = null
		try {
			if (cursor.moveToNext()) {
				jo = mapOne(cursor)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		}
		close()
		return jo
	}
	fun jsonObject2(): JSONObject? {
		var jo: JSONObject? = null
		try {
			if (cursor.moveToNext()) {
				jo = mapOne2(cursor)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		}
		close()
		return jo
	}

	/**
	 * 将所有的记录映射成JsonObject的数组JsonArray
	 * 数据量大, 不要调用此函数, 性能低, 小数据可以, 或调试用

	 * @return
	 */
	fun jsonArray(): YsonArray {
		val arr = YsonArray()
		try {
			while (cursor.moveToNext()) {
				val jo = mapOne(cursor)
				arr.add(jo)
			}
		} catch (t: Throwable) {
			t.printStackTrace()
		}
		close()
		return arr
	}
	fun jsonArray2(): JSONArray {
		val arr = JSONArray()
		try {
			while (cursor.moveToNext()) {
				val jo = mapOne2(cursor)
				arr.put(jo)
			}
		} catch (t: Throwable) {
			t.printStackTrace()
		}
		close()
		return arr
	}

	/**
	 * 将第一行,第0列的数据转化成数字返回.
	 * 如果是null, 或blob 或 [String类型转化到int出错], 都会返回failVal

	 * @param failVal
	 * *
	 * @return
	 */
	fun intValue(): Int? {
		return longValue()?.toInt()
	}

	fun intValue(failVal: Int): Int {
		return longValue()?.toInt() ?: failVal
	}

	/**
	 * 忽略null, blob
	 * 将第一行,第0列的数据转化成数字返回.
	 * 如果是null, 或blob 或 [String类型转化到number出错], 都会返回failVal

	 * @param failVal
	 * *
	 * @return
	 */
	fun longValue(): Long? {
		try {
			if (cursor.moveToNext()) {
				val type = cursor.getType(0)
				when (type) {
					Cursor.FIELD_TYPE_INTEGER -> return cursor.getLong(0)
					Cursor.FIELD_TYPE_FLOAT -> return cursor.getDouble(0).toLong()
					Cursor.FIELD_TYPE_STRING -> return cursor.getString(0)?.toLong()
					else -> {
					}
				}
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		} finally {
			close()
		}
		return null
	}

	fun longValue(failVal: Long): Long {
		return longValue() ?: failVal
	}

	// 失败返回nul;
	// 取第一行, 第一列的值
	fun strValue(): String? {
		try {
			if (cursor.moveToNext()) {
				return cursor.getString(0)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		} finally {
			close()
		}
		return null
	}

	/**
	 * 忽略null, blob
	 * 只处理第一列数据, 后面的列被忽略
	 * 返回的结果集中没有null值
	 * 结果集类型是String, Long, Double类型之一
	 *
	 *
	 * 模拟的distinct, 尽量使用where减小数据集!
	 * 慎用!

	 * @return
	 */
	fun firstColumnValueSet(): Set<Any> {
		val set = HashSet<Any>(32)
		firstColumnValueSetWithNull().forEach {
			if (it != null) {
				set.add(it)
			}
		}
		return set
	}

	fun firstColumnValueSetWithNull(): Set<Any?> {
		val set = HashSet<Any?>(32)
		try {
			while (cursor.moveToNext()) {
				val type = cursor.getType(0)
				when (type) {
					Cursor.FIELD_TYPE_NULL -> set.add(null)
					Cursor.FIELD_TYPE_INTEGER -> set.add(cursor.getLong(0))
					Cursor.FIELD_TYPE_FLOAT -> set.add(cursor.getDouble(0))
					Cursor.FIELD_TYPE_STRING -> set.add(cursor.getString(0))
					else -> {
					}
				}
			}
		} catch(e: Throwable) {
			e.printStackTrace()
		} finally {
			close()
		}
		return set
	}

	/**
	 * 忽略null, integer和float 转换成字符串, blob类型被忽略

	 * @return
	 */
	fun stringSet(): Set<String> {
		val set = HashSet<String>(32)
		stringSetWithNull().forEach {
			if (it != null) {
				set.add(it)
			}
		}
		return set
	}

	fun stringSetWithNull(): Set<String?> {
		val set = HashSet<String?>(32)
		try {
			while (cursor.moveToNext()) {
				val type = cursor.getType(0)
				when (type) {
					Cursor.FIELD_TYPE_NULL -> set.add(null)
					Cursor.FIELD_TYPE_INTEGER -> set.add(cursor.getLong(0).toString())
					Cursor.FIELD_TYPE_FLOAT -> set.add(cursor.getDouble(0).toString())
					Cursor.FIELD_TYPE_STRING -> set.add(cursor.getString(0))
					else -> {
					}
				}
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		} finally {
			close()
		}
		return set
	}

	/**
	 * 忽略null和blob, string和float被转换成int

	 * @param nullable true:如果结果集中有null则会添加到返回的set中; false:忽略null值
	 * *
	 * @return
	 */
	fun intSet(): Set<Int> {
		val set = HashSet<Int>()
		longSetWithNull().forEach {
			if (it != null) {
				set.add(it.toInt())
			}
		}
		return set
	}

	fun intSetWithNull(): Set<Int?> {
		val set = HashSet<Int?>()
		longSetWithNull().forEach {
			if (it == null) {
				set.add(null)
			} else {
				set.add(it.toInt())
			}
		}
		return set
	}

	/**
	 * 忽略blob, string和float被转换成long

	 * @param nullable true:如果结果集中有null则会添加到返回的set中; false:忽略null值
	 * *
	 * @return
	 */
	fun longSet(): Set<Long> {
		val set = HashSet<Long>(32)
		longSetWithNull().forEach {
			if (it != null) {
				set.add(it)
			}
		}
		return set
	}

	fun longSetWithNull(): Set<Long?> {
		val set = HashSet<Long?>(32)
		try {
			while (cursor.moveToNext()) {
				val type = Cursor.FIELD_TYPE_INTEGER
				when (type) {
					Cursor.FIELD_TYPE_NULL -> set.add(null)
					Cursor.FIELD_TYPE_INTEGER -> set.add(cursor.getLong(0))
					Cursor.FIELD_TYPE_FLOAT -> set.add(cursor.getDouble(0).toLong())
					Cursor.FIELD_TYPE_STRING -> {
						try {
							set.add(java.lang.Long.valueOf(cursor.getString(0)))
						} catch (e: Exception) {
							set.add(null)
						}
					}
					else -> {
					}
				}
			}
		} catch (e: Throwable) {
			e.printStackTrace()
		} finally {
			close()
		}
		return set
	}

	/**
	 * 忽略blob
	 * mapOne不会关闭cursor, 不会执行cursor.moveToXXX方法
	 * 返回的JsonObject可能包含number, string, 或null

	 * @param c
	 * *
	 * @return 不会返回null
	 */
	fun mapOne(c: Cursor): YsonObject {
		val jo = YsonObject()
		for (i in 0 until c.columnCount) {
			val key = c.getColumnName(i)
			val type = c.getType(i)
			when (type) {
				Cursor.FIELD_TYPE_INTEGER -> jo.any(key, c.getLong(i))
				Cursor.FIELD_TYPE_FLOAT -> jo.any(key, c.getDouble(i))
				Cursor.FIELD_TYPE_STRING -> jo.any(key, c.getString(i))
				Cursor.FIELD_TYPE_NULL -> jo.any(key, null )
				Cursor.FIELD_TYPE_BLOB -> {
				}
				else -> {
				}
			}
		}
		return jo
	}
	fun mapOne2(c: Cursor): JSONObject {
		val jo = JSONObject()
		for (i in 0 until c.columnCount) {
			val key = c.getColumnName(i)
			val type = c.getType(i)
			when (type) {
				Cursor.FIELD_TYPE_INTEGER -> jo.put(key, c.getLong(i))
				Cursor.FIELD_TYPE_FLOAT -> jo.put(key, c.getDouble(i))
				Cursor.FIELD_TYPE_STRING -> jo.put(key, c.getString(i))
				Cursor.FIELD_TYPE_NULL -> jo.putNull(key)
				Cursor.FIELD_TYPE_BLOB -> {
				}
				else -> {
				}
			}
		}
		return jo
	}

	fun dump() {
		val arr = jsonArray()
		xlog.d(arr)
	}


}

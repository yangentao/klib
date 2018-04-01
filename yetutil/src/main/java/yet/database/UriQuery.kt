package yet.util.database

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import yet.util.app.App
import yet.util.log.logd
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/13.
 */

class UriQuery(val uri: Uri) {
	val ID = "_id"

	private var columns: Array<String> = emptyArray()

	private var orderStr: String? = null
	private var limitStr: String? = null
	private var whereStr: String? = null
	private var argList = ArrayList<Any>()

	constructor(uri: String) : this(Uri.parse(uri)) {
	}

	constructor(uri: Uri, id: Long, vararg cols: String) : this(ContentUris.withAppendedId(uri, id)) {
		colunms(*cols)
	}

	fun colunms(vararg cols: String): UriQuery {
		columns = Array<String>(cols.size) {
			cols[it]
		}
		return this
	}

	fun where(w: WhereNode): UriQuery {
		this.whereStr = w.toString()
		this.argList.addAll(w.args)
		return this
	}
	fun where(s:String):UriQuery {
		this.whereStr = s
		return this
	}


	fun whereId(_id: Long): UriQuery {
		return where("_id=$_id")
	}

	fun whereEq(col: String, value: String): UriQuery {
		return where(col EQ value)
	}


	fun whereEq(col: String, value: Long): UriQuery {
		return where("$col=$value")
	}


	fun limit(limit: Int): UriQuery {
		if (limit > 0) {
			limitStr = " LIMIT $limit "
		}
		return this
	}

	fun limit(limit: Int, offset: Int): UriQuery {
		if (limit > 0 && offset >= 0) {
			limitStr = " LIMIT $limit OFFSET $offset "
		}
		return this
	}

	fun orderBy(sortOrder: String): UriQuery {
		this.orderStr = sortOrder
		return this
	}

	fun orderBy(col: String, asc: Boolean): UriQuery {
		this.orderStr = col + if (asc) " ASC " else " DESC "
		return this
	}

	fun asc(col: String): UriQuery {
		return orderBy(col, true)
	}

	fun desc(col: String): UriQuery {
		return orderBy(col, false)
	}

	fun query(): Cursor {
		var order: String? = orderStr
		if (limitStr != null) {
			if (orderStr != null) {
				order = orderStr + limitStr
			} else {
				order = "_id ASC " + limitStr!!
			}
		}
		try {
			logd("UriQuery: $uri, $columns, $whereStr", argList.map { it.toString() })
			var c = App.contentResolver.query(uri, columns, whereStr, argList.map { it.toString() }.toTypedArray(), order)
			if (c != null) {
				return c
			}
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
		return SafeCursor(null)
	}

	fun queryOne(): Cursor {
		limit(1)
		return query()
	}

	fun result(): CursorResult {
		return CursorResult(query())
	}

	fun resultOne(): CursorResult {
		return CursorResult(queryOne())
	}

	fun resultJsonObject(): JsonObject? {
		return resultOne().jsonObject()
	}

	fun resultJsonArray(): JsonArray {
		return result().jsonArray()
	}

	fun resultString(): String? {
		return resultOne().strValue()
	}

	fun resultInt(): Int? {
		return resultOne().intValue()
	}

	fun resultInt(failVal: Int): Int {
		return resultOne().intValue() ?: failVal
	}

	fun resultLong(): Long? {
		return resultOne().longValue()
	}

	fun resultLong(failVal: Long): Long {
		return resultOne().longValue() ?: failVal
	}

	fun resultCount(): Int {
		return result().countAndClose()
	}

	companion object {
		fun select(uri: Uri, vararg cols: String): UriQuery {
			return UriQuery(uri).colunms(*cols)
		}
	}

}
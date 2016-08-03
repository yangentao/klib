package net.yet.util.database

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.yet.util.Util
import net.yet.util.app.App
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


	fun whereId(_id: Long): UriQuery {
		return where(ID EQ _id)
	}

	fun whereEq(col: String, value: String): UriQuery {
		return where(col EQ value)
	}


	fun whereEq(col: String, value: Long): UriQuery {
		return where(col EQ value)
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
			var c = App.getContentResolver().query(uri, columns, whereStr, Util.toStringArray(argList), order)
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

	fun resultInt(fallback: Int = 0): Int {
		return resultOne().intValue(fallback)
	}

	fun resultLong(fallback: Long = 0): Long {
		return resultOne().longValue(fallback)
	}

}
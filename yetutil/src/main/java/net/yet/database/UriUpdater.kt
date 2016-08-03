package net.yet.util.database

import android.content.ContentValues
import android.net.Uri
import net.yet.util.app.App

object UriUpdater {
	val _ID = "_id"

	fun insert(uri: Uri, values: ContentValues): Uri {
		return App.getContentResolver().insert(uri, values)
	}

	fun insert(uri: Uri, vararg keyValues: Pair<String, Any>): Uri {
		return insert(uri, Values(*keyValues))
	}

	fun insert(uri: Uri, values: Values): Uri {
		return insert(uri, values.contentValues())
	}

	fun update(uri: Uri, values: ContentValues, w: WhereNode? = null): Int {
		return App.getContentResolver().update(uri, values, w?.toString(), w?.argsArray)
	}

	fun update(uri: Uri, values: Values, w: WhereNode? = null): Int {
		return update(uri, values.contentValues(), w)
	}

	fun update(uri: Uri, values: Map<String, Any>, w: WhereNode? = null): Int {
		return update(uri, Values(values), w)
	}

	fun update(uri: Uri, key: String, value: Any, w: WhereNode? = null): Int {
		return update(uri, Values(key to value), w)
	}

	fun update(uri: Uri, id: Long, vararg keyValues: Pair<String, Any>): Int {
		return update(uri, Values(*keyValues), _ID EQ id)
	}

	fun update(uri: Uri, id: Long, values: Values): Int {
		return update(uri, values, _ID EQ id)
	}

	fun delete(uri: Uri): Int {
		return App.getContentResolver().delete(uri, null, null)
	}

	fun delete(uri: Uri, w: WhereNode): Int {
		return App.getContentResolver().delete(uri, w.toString(), w.argsArray)
	}

	fun delete(uri: Uri, id: Long): Int {
		return delete(uri, _ID EQ id)
	}

	fun delete(uri: Uri, key: String, value: Long): Int {
		return delete(uri, key EQ value)
	}

	fun delete(uri: Uri, key: String, value: String): Int {
		return delete(uri, key EQ value)
	}

}

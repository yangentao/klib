package yet.util.database

import android.content.ContentValues
import android.net.Uri
import yet.util.app.App

object UriUpdater {
	const val _ID = "_id"

	@JvmStatic fun insert(uri: Uri, values: ContentValues): Uri {
		return App.contentResolver.insert(uri, values)
	}

	@JvmStatic fun insert(uri: Uri, vararg keyValues: Pair<String, Any>): Uri {
		return insert(uri, Values(*keyValues))
	}

	@JvmStatic fun insert(uri: Uri, values: Values): Uri {
		return insert(uri, values.contentValues())
	}

	@JvmStatic fun insert(uri: Uri, key: String, value: String): Uri {
		return insert(uri, key to value)
	}

	@JvmStatic fun insert(uri: Uri, key: String, value: Long): Uri {
		return insert(uri, key to value)
	}

	@JvmStatic fun insert(uri: Uri, key: String, value: String, key2: String, value2: String): Uri {
		return insert(uri, key to value, key2 to value2)
	}

	@JvmStatic fun insert(uri: Uri, key: String, value: String, key2: String, value2: Long): Uri {
		return insert(uri, key to value, key2 to value2)
	}

	@JvmStatic fun update(uri: Uri, values: ContentValues): Int {
		return update(uri, values, null)
	}

	@JvmStatic fun update(uri: Uri, values: ContentValues, w: WhereNode?): Int {
		return App.contentResolver.update(uri, values, w?.toString(), w?.toArgArray)
	}

	@JvmStatic fun update(uri: Uri, values: Values): Int {
		return update(uri, values, null)
	}

	@JvmStatic fun update(uri: Uri, values: Values, w: WhereNode?): Int {
		return update(uri, values.contentValues(), w)
	}

	@JvmStatic fun update(uri: Uri, values: Map<String, Any>): Int {
		return update(uri, values, null)
	}

	@JvmStatic fun update(uri: Uri, values: Map<String, Any>, w: WhereNode?): Int {
		return update(uri, Values(values), w)
	}

	@JvmStatic fun update(uri: Uri, key: String, value: Any): Int {
		return update(uri, key, value, null)
	}

	@JvmStatic fun update(uri: Uri, key: String, value: Any, w: WhereNode?): Int {
		return update(uri, Values(key to value), w)
	}

	@JvmStatic fun update(uri: Uri, id: Long, vararg keyValues: Pair<String, Any>): Int {
		return update(uri, Values(*keyValues), _ID EQ id)
	}

	@JvmStatic fun update(uri: Uri, id: Long, name: String, value: Long): Int {
		return update(uri, id, name to value)
	}

	@JvmStatic fun update(uri: Uri, id: Long, name: String, value: String): Int {
		return update(uri, id, name to value)
	}

	@JvmStatic fun update(uri: Uri, id: Long, values: Values): Int {
		return update(uri, values, _ID EQ id)
	}

	@JvmStatic fun delete(uri: Uri): Int {
		return App.contentResolver.delete(uri, null, null)
	}

	@JvmStatic fun delete(uri: Uri, w: WhereNode): Int {
		return App.contentResolver.delete(uri, w.toString(), w.toArgArray)
	}

	@JvmStatic fun delete(uri: Uri, id: Long): Int {
		return delete(uri, _ID EQ id)
	}

	@JvmStatic fun delete(uri: Uri, key: String, value: Long): Int {
		return delete(uri, key EQ value)
	}

	@JvmStatic fun delete(uri: Uri, key: String, value: String): Int {
		return delete(uri, key EQ value)
	}
	fun delete(uri: Uri, keyValue: Pair<String,String>): Int {
		return delete(uri, keyValue.first EQ keyValue.second)
	}

}

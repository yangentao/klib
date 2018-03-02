package yet.util.database

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentResolver
import android.database.CharArrayBuffer
import android.database.ContentObserver
import android.database.Cursor
import android.database.DataSetObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import yet.util.app.OS

/**
 * Created by entaoyang@163.com on 16/5/13.
 */
class SafeCursor(val wrappedCursor: Cursor?) : Cursor {


	fun isNull(): Boolean {
		return wrappedCursor == null
	}

	override fun close() {
		wrappedCursor?.close()
	}

	override fun isClosed(): Boolean {
		return wrappedCursor?.isClosed ?: true
	}

	override fun getCount(): Int {
		return wrappedCursor?.count ?: 0
	}

	override fun deactivate() {
		wrappedCursor?.deactivate()
	}

	override fun moveToFirst(): Boolean {
		return wrappedCursor?.moveToFirst() ?: false
	}

	override fun getColumnCount(): Int {
		return wrappedCursor?.columnCount ?: 0
	}

	override fun getColumnIndex(columnName: String): Int {
		return wrappedCursor?.getColumnIndex(columnName) ?: -1
	}

	@Throws(IllegalArgumentException::class)
	override fun getColumnIndexOrThrow(columnName: String): Int {
		return wrappedCursor?.getColumnIndexOrThrow(columnName) ?: throw IllegalArgumentException("NULL cursor!")
	}

	override fun getColumnName(columnIndex: Int): String? {
		return wrappedCursor?.getColumnName(columnIndex) ?: null
	}

	override fun getColumnNames(): Array<String> {
		return wrappedCursor?.columnNames ?: emptyArray<String>()
	}

	override fun getDouble(columnIndex: Int): Double {
		return wrappedCursor?.getDouble(columnIndex) ?: 0.0
	}

	override fun getExtras(): Bundle {
		return wrappedCursor?.extras ?: Bundle()
	}

	override fun getFloat(columnIndex: Int): Float {
		return wrappedCursor?.getFloat(columnIndex) ?: 0f
	}

	override fun getInt(columnIndex: Int): Int {
		return wrappedCursor?.getInt(columnIndex) ?: 0
	}

	override fun getLong(columnIndex: Int): Long {
		return wrappedCursor?.getLong(columnIndex) ?: 0
	}

	override fun getShort(columnIndex: Int): Short {
		return wrappedCursor?.getShort(columnIndex) ?: 0
	}

	override fun getString(columnIndex: Int): String? {
		return wrappedCursor?.getString(columnIndex)
	}

	override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer) {
		wrappedCursor?.copyStringToBuffer(columnIndex, buffer)
	}

	override fun getBlob(columnIndex: Int): ByteArray? {
		return wrappedCursor?.getBlob(columnIndex)
	}

	override fun getWantsAllOnMoveCalls(): Boolean {
		return wrappedCursor?.wantsAllOnMoveCalls ?: false
	}

	@TargetApi(Build.VERSION_CODES.M)
	override fun setExtras(extras: Bundle) {
		if (OS.API > OS.V60) {
			wrappedCursor?.extras = extras
		}
	}


	override fun isAfterLast(): Boolean {
		return wrappedCursor?.isAfterLast ?: false
	}

	override fun isBeforeFirst(): Boolean {
		return wrappedCursor?.isBeforeFirst ?: false
	}

	override fun isFirst(): Boolean {
		return wrappedCursor?.isFirst ?: false
	}

	override fun isLast(): Boolean {
		return wrappedCursor?.isLast ?: false
	}

	override fun getType(columnIndex: Int): Int {
		return wrappedCursor?.getType(columnIndex) ?: Cursor.FIELD_TYPE_NULL
	}

	override fun isNull(columnIndex: Int): Boolean {
		return wrappedCursor?.isNull(columnIndex) ?: true
	}

	override fun moveToLast(): Boolean {
		return wrappedCursor?.moveToLast() ?: false
	}

	override fun move(offset: Int): Boolean {
		return wrappedCursor?.move(offset) ?: false
	}

	override fun moveToPosition(position: Int): Boolean {
		return wrappedCursor?.moveToPosition(position) ?: false
	}

	override fun moveToNext(): Boolean {
		return wrappedCursor?.moveToNext() ?: false
	}

	override fun getPosition(): Int {
		return wrappedCursor?.position ?: -1
	}

	override fun moveToPrevious(): Boolean {
		return wrappedCursor?.moveToPrevious() ?: false
	}

	override fun registerContentObserver(observer: ContentObserver) {
		wrappedCursor?.registerContentObserver(observer)
	}

	override fun registerDataSetObserver(observer: DataSetObserver) {
		wrappedCursor?.registerDataSetObserver(observer)
	}

	override fun requery(): Boolean {
		return wrappedCursor?.requery() ?: false
	}

	override fun respond(extras: Bundle): Bundle? {
		return wrappedCursor?.respond(extras)
	}

	override fun setNotificationUri(cr: ContentResolver, uri: Uri) {
		wrappedCursor?.setNotificationUri(cr, uri)
	}

	@SuppressLint("NewApi")
	override fun getNotificationUri(): Uri? {
		return wrappedCursor?.notificationUri
	}

	override fun unregisterContentObserver(observer: ContentObserver) {
		wrappedCursor?.unregisterContentObserver(observer)
	}

	override fun unregisterDataSetObserver(observer: DataSetObserver) {
		wrappedCursor?.unregisterDataSetObserver(observer)
	}
}

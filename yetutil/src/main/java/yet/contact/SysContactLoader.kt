package yet.contact

import android.provider.ContactsContract
import yet.ext.notEmpty
import yet.util.database.EQ
import yet.util.database.UriQuery
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */
private val CONTENT_URI = ContactsContract.Data.CONTENT_URI
private val MIME = ContactsContract.Data.MIMETYPE
private val MimePhone = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE

private val COLS = arrayOf(
		ContactsContract.Contacts.DISPLAY_NAME,
		ContactsContract.CommonDataKinds.Phone.NUMBER
)

fun loadSysContacts(): ArrayList<SysContact> {
	val cursor = UriQuery.select(CONTENT_URI, *COLS).where(MIME EQ MimePhone).query()
	val ls = ArrayList<SysContact>(512)
	while (cursor.moveToNext()) {
		val name = cursor.getString(0)
		val phone = cursor.getString(1)
		val p = formatPhone(phone)
		if (p.notEmpty()) {
			ls.add(SysContact(p!!, name))
		}
	}
	cursor.close()
	return ls
}

private fun formatPhone(phoneNumber: String?): String? {
	var phone: String = phoneNumber ?: return null
	phone = phone.replace("-", "")
	phone = phone.replace(" ", "")
	if (phone.startsWith("+86")) {
		phone = phone.substring(3)
	}
	return phone
}

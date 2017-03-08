package net.yet.util.app

import android.widget.TextView
import com.google.gson.JsonArray
import net.yet.json.eachObject
import net.yet.ext.optString
import net.yet.util.*
import net.yet.util.database.GE
import net.yet.util.database.UriQuery
import java.util.regex.Pattern

/**
 * Created by yangentao on 16/3/1.
 */
object SmsCodeFill {
	private val INBOX_SMS_URI = MyTelephony.Sms.Inbox.CONTENT_URI

	fun getSmsSince(time: Long): JsonArray {
		return UriQuery(INBOX_SMS_URI).where("date" GE time).desc("date").resultJsonArray();
	}

	fun searchSmsCode(textView: TextView, sinceTime: Long = System.currentTimeMillis()) {
		TaskUtil.repeatFore(30, 2000, object : RepeatCallback() {

			override fun onRepeat(index: Int, value: Long): Boolean {
				val ja = getSmsSince(sinceTime)
				ja.eachObject {
					val code = matchCode(it.optString("body"))
					val n = Util.length(code)
					if (n >= 4 && n <= 6) {
						textView.text = code
						return false
					}
				}
				return true
			}
		})

	}

	private fun matchCode(body: String?): String? {
		if (body != null) {
			val p = Pattern.compile("(\\d{4,6})")
			val m = p.matcher(body)
			if (m.find() && m.groupCount() >= 1) {
				return m.group()
			}
		}
		return null
	}

}

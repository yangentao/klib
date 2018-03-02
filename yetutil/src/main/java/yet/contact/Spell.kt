package yet.contact

import yet.util.HanziToPinyin
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */


object Spell {
	val map = HashMap<String, String>(512)

	fun get(ss: String?): String? {
		val s = ss ?: return null
		if (s.length == 0) {
			return s
		}
		var spell: String? = map[s]
		if (spell == null) {
			spell = HanziToPinyin.getPinyin(s)
			map.put(s, spell)
		}
		return spell
	}

}

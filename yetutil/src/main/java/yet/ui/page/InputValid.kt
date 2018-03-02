package yet.ui.page

/**
 * Created by entaoyang@163.com on 2017-06-05.
 */

class InputValid {
	var label: String? = null

	var trimInput: Boolean = false
	var allowEmpty: Boolean = true
	var minLength: Int = -1
	var maxLength: Int = -1
	var fixLength: Int = -1

	var email: Boolean = false
	var phone11: Boolean = false
	var numberOnly: Boolean = false

	var allowEmptyTip: String? = null
	var minLenTip: String? = null
	var maxLenTip: String? = null
	var fixLenTip: String? = null
	var emailTip: String? = null
	var phone11Tip: String? = null
	var numTip: String? = null

	fun label(s: String?) {
		if (s == null) {
			return
		}
		var lb = s
		if (lb.endsWith('*')) {
			require()
			lb = lb.substring(0, lb.length - 1)
		}
		if (lb.startsWith("请输入")) {
			lb = lb.substringAfter("请输入")
		}
		label = lb
	}


	fun numbers(tip: String? = null) {
		numberOnly = true
		numTip = tip
		trimText()
	}

	fun email(tip: String? = null) {
		email = true
		emailTip = tip
		trimText()
	}

	fun phone11(tip: String? = null) {
		phone11 = true
		phone11Tip = tip
		trimText()
	}

	fun notEmpty(tip: String? = null) {
		allowEmpty = false
		allowEmptyTip = tip
	}

	fun require(tip: String? = null) {
		notEmpty(tip)
	}

	fun trimText() {
		trimInput = true
	}

	fun minLen(n: Int, tip: String? = null) {
		minLength = n
		minLenTip = tip
	}

	fun maxLen(n: Int, tip: String? = null) {
		maxLength = n
		maxLenTip = tip
	}

	fun fixLen(n: Int, tip: String? = null) {
		fixLength = n
		fixLenTip = tip
	}
}
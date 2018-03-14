package yet.ui.page

import android.content.Context
import android.widget.*
import yet.theme.Colors
import yet.theme.InputSize
import yet.ui.ext.*
import yet.ui.res.CheckBoxDrawable
import yet.ui.util.TimeDown
import yet.ui.viewcreator.*
import yet.ui.widget.listview.itemview.TextDetailView
import yet.util.*
import yet.util.app.SmsCodeFill
import yet.util.log.logd
import java.util.regex.Pattern

/**
 * Created by entaoyang@163.com on 2016-10-20.
 */

abstract class InputPage : TitledPage() {

	lateinit var inputLayout: LinearLayout

	var INPUT_HEIGHT = InputSize.EditHeight
	var inputMarginTop = 10
	var buttonMarginTop = 30

	private val editList = ArrayList<Pair<String, EditText>>()
	private val checkMap = HashMap<String, CheckBox>()
	private val dateMap = HashMap<String, TextDetailView>()
	private val dateFormatMap = HashMap<String, String>().withDefault { MyDate.FORMAT_DATE }
	private val selectMap = HashMap<String, TextDetailView>()
	private val validMap = HashMap<String, InputValid>()
	private var codeEdit: EditText? = null
	private var codeButton: Button? = null
	private var timeDownKey: String? = null
	private var codeClickTime: Long = 0


	fun valid(): Boolean {
		for ((k, v) in validMap.entries) {
			val ed = editList.find { it.first == k }!!.second
			if (!valid(k, ed, v)) {
				return false
			}
		}
		return true
	}

	open fun onValidError(key: String, edit: EditText, msg: String) {
		alert(msg)
	}

	fun valid(key: String, edit: EditText, v: InputValid): Boolean {
		var label = v.label ?: ""
		label = label.trim().trim('*')

		logd("Valid: ", key, label)

		var tx = edit.text.toString()
		if (v.trimInput) {
			val t2 = tx.trim()
			if (t2 != tx) {
				edit.setText(t2)
				tx = t2
			}
		}
		if (tx.isEmpty()) {
			if (!v.allowEmpty) {
				onValidError(key, edit, v.allowEmptyTip ?: label + "不能为空")
				return false
			}
		} else {
			if (v.fixLength > 0) {
				if (tx.length != v.fixLength) {
					onValidError(key, edit, v.fixLenTip ?: label + "长度必须是${v.fixLength}")
					return false
				}
			}
			if (v.minLength > 0) {
				if (tx.length < v.minLength) {
					onValidError(key, edit, v.minLenTip ?: label + "长度必须大于${v.minLength}")
					return false
				}
			}
			if (v.maxLength > 0) {
				if (tx.length < v.maxLength) {
					onValidError(key, edit, v.maxLenTip ?: label + "长度必须小于${v.maxLength}")
					return false
				}
			}
			if (v.email) {
				if (!isEmailFormat(tx)) {
					onValidError(key, edit, v.emailTip ?: label + "必须是email格式")
					return false
				}
			}
			if (v.phone11) {
				if (!isPhoneFormatCN11(tx)) {
					onValidError(key, edit, v.phone11Tip ?: label + "必须是11位手机号格式")
					return false
				}
			}
			if (v.numberOnly) {
				if (null != tx.find { (it !in '0'..'9') && it != '.' }) {
					onValidError(key, edit, v.numTip ?: label + "必须是数字")
					return false
				}
			}
		}
		return true
	}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		inputLayout = contentView.linearVer(lParam().widthFill().heightWrap()) {
			padding(30, 25, 30, 20)
		}
	}
	override fun onContentCreated() {
		super.onContentCreated()
		val sz = editList.size
		editList.forEachIndexed { n, p ->
			if (n < sz - 1) {
				p.second.imeNext {
					editList[n + 1].second.requestFocus()
				}
			} else {
				p.second.imeDone {
					hideInputMethod()
				}
			}
		}
	}

	fun isEmailFormat(s: String?): Boolean {
		val ss = s ?: return false
		val regex = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?"
		val p = Pattern.compile(regex)
		val m = p.matcher(ss)
		return m.find()
	}

	//中国的11位手机号码格式, 连续11位数字,1开头
	fun isPhoneFormatCN11(s: String?): Boolean {
		val ss = s ?: return false
		val regex = "1[0-9]{10}"
		val p = Pattern.compile(regex)
		val m = p.matcher(ss)
		return m.find()
	}

	fun isCheck(key: String): Boolean {
		return checkMap[key]?.isChecked ?: false
	}

	fun setCheck(key: String, check: Boolean) {
		checkMap[key]?.isChecked = check
	}

	val code: String
		get() {
			return codeEdit?.text?.toString() ?: ""
		}

	fun getText(key: String): String {
		val s = editList.find { it.first == key }?.second?.text?.toString() ?: ""
		val v = validMap[key]
		if (v != null && v.trimInput) {
			return s.trim()
		}
		return s
	}

	fun setText(key: String, text: String) {
		editList.find { it.first == key }?.second?.setText(text)
	}

	fun disableEdit(key: String) {
		editList.find { it.first == key }?.second?.isEnabled = false
	}

	fun getDate(key: String): Long {
		val s = dateMap[key]!!.detailView!!.text!!.toString()
		if (s.isNotEmpty()) {
			return MyDate.parse(dateFormatMap[key] ?: MyDate.FORMAT_DATE, s)!!.time
		}
		return 0
	}

	fun setDate(key: String, date: Long) {
		dateMap[key]?.setDetail(MyDate(date).format(dateFormatMap[key] ?: MyDate.FORMAT_DATE))
	}

	fun addDate(key: String, title: String, format: String = MyDate.FORMAT_DATE, marginTop: Int = inputMarginTop): TextDetailView {
		val v = TextDetailView(inputLayout.context)
		v.padding(0, 0, 0, 0)
		v.detailView.textSizeB().gravityCenter().padding(10, 5, 10, 5)
		v.detailView.miniWidthDp(100)
		v.detailView.miniHeightDp(InputSize.ButtonHeightSmall - 10)
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		v.setText(title)
		inputLayout.addView(v, lParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0))
		dateMap[key] = v
		dateFormatMap[key] = format
		setDate(key, 0L)
		v.onClick {
			pickDate(getDate(key)) {
				setDate(key, it)
			}
		}
		return v
	}

	fun addTextDetail(key: String, title: String, marginTop: Int = inputMarginTop): TextDetailView {
		val v = TextDetailView(inputLayout.context)
		v.padding(0, 0, 0, 0)
		v.detailView.textSizeB().gravityCenter().padding(10, 5, 10, 5)
		v.detailView.miniWidthDp(100)
		v.detailView.miniHeightDp(InputSize.ButtonHeightSmall - 10)
		v.setText(title)
		inputLayout.addView(v, lParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0))
		return v
	}
	fun getSelectValue(key: String): String {
		return selectMap[key]?.tag as? String ?: ""
	}

	fun addSelect(key: String, title: String, value: String, values: List<Pair<String, String>>) {
		val v = addTextDetail(key, title)
		selectMap[key] = v
		val item = values.find { it.first == value }
		v.setDetail(item?.second)
		v.tag = item?.first
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		v.onClick {
			itemSelectN(values.map { it.second }) {
				v.tag = values[it].first
				v.setDetail(values[it].second)
			}

		}
	}


	fun addStatic(label: String, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0)) {
			text = label
		}
	}

	fun addLabel(label: String, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().heightWrap().margins(0, marginTop, 0, 0)) {
			text = label
		}
	}

	private fun makeEdit(hint: String, marginTop: Int): EditText {
		val ed = createEditX().hint(hint)
		ed.padding(5, 2, 5, 2)
		linearParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0).set(ed)
		return ed
	}

	fun addEdit(key: String, hint: String, marginTop: Int = inputMarginTop, block: InputValid.() -> Unit = {}): EditText {
		val ed = makeEdit(hint, marginTop)
		inputLayout.addView(ed)
		editList.add(key to ed)
		val iv = InputValid()
		iv.label(hint)
		iv.block()
		validMap[key] = iv
		return ed
	}

	fun addEditPhone(key: String, hint: String = "请输入手机号", marginTop: Int = inputMarginTop, block: InputValid.() -> Unit = {}): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePhone()
		inputLayout.addView(ed)
		editList.add(key to ed)
		val iv = InputValid()
		iv.label(hint)
		iv.phone11()
		iv.block()
		validMap[key] = iv
		return ed
	}

	fun addEditEmail(key: String, hint: String = "请输入邮箱", marginTop: Int = inputMarginTop, block: InputValid.() -> Unit = {}): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypeEmail()
		inputLayout.addView(ed)
		editList.add(key to ed)
		val iv = InputValid()
		iv.label(hint)
		iv.email()
		iv.block()
		validMap[key] = iv
		return ed
	}

	fun addNumber(key: String, hint: String, marginTop: Int = inputMarginTop, block: InputValid.() -> Unit = {}): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypeNumber()
		inputLayout.addView(ed)
		editList.add(key to ed)
		val iv = InputValid()
		iv.label(hint)
		iv.numbers()
		iv.block()
		validMap[key] = iv
		return ed
	}

	fun addPasswordAgain(key: String, hint: String = "请再次输入密码"): EditText {
		return addPassword(key, hint, inputMarginTop)
	}

	fun addPassword(key: String, hint: String = "请输入密码", marginTop: Int = inputMarginTop, block: InputValid.() -> Unit = {}): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePassword()
		inputLayout.addView(ed)
		editList.add(key to ed)
		val iv = InputValid()
		iv.label(hint)
		iv.notEmpty()
		iv.block()
		validMap[key] = iv
		return ed
	}

	fun addPasswordNumber(key: String, hint: String = "请输入数字密码", marginTop: Int = inputMarginTop, block: InputValid.() -> Unit = {}): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePasswordNumber()
		inputLayout.addView(ed)
		editList.add(key to ed)
		val iv = InputValid()
		iv.label(hint)
		iv.numbers()
		iv.block()
		validMap[key] = iv
		return ed
	}


	fun searchSmsCode() {
		if (this.codeEdit != null && codeClickTime != 0L) {
			SmsCodeFill.searchSmsCode(this.codeEdit as TextView, codeClickTime)
		}
	}

	fun startTimeDown(seconds: Int) {
		fore {
			TimeDown.startClick(timeDownKey!!, seconds, codeButton!!)
		}
	}

	fun addVerifyCode(timeDownKey: String, phoneEditKey: String, block: (String) -> Unit) {
		addVerifyCode(timeDownKey, phoneEditKey, inputMarginTop, block)
	}

	fun addVerifyCode(timeDownKey: String, phoneEditKey: String, marginTop: Int, block: (String) -> Unit) {
		val verifyLayout = activity.createLinearHorizontal()
		codeEdit = createEdit().hint("输入验证码")
		codeEdit?.inputTypeNumber()
		verifyLayout.addView(codeEdit, linearParam().width(0).weight(1f).height(InputSize.EditHeight))
		codeButton = createButton("获取验证码").themeWhite()
		verifyLayout.addView(codeButton, linearParam().widthWrap().heightWrap().margins(3, 0, 0, 0))
		inputLayout.addView(verifyLayout, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))

		this.timeDownKey = timeDownKey
		if (Util.notEmpty(timeDownKey)) {
			TimeDown.updateView(this.timeDownKey!!, codeButton!!)
		}
		codeButton!!.setOnClickListener({
			codeClickTime = System.currentTimeMillis()
			val phone = getText(phoneEditKey)
			if (!isPhoneFormatCN11(phone)) {
				ToastUtil.show("手机号格式错误")
			} else {
				startTimeDown(60)
				back {
					block(phone)
				}
			}
		})
	}

	fun addCheckbox(key: String, title: String, marginTop: Int = inputMarginTop) {
		val cb = activity.createCheckBox()
		cb.text = title
		val d = CheckBoxDrawable()
		cb.buttonDrawable = d
		cb.padding(20, 5, 5, 5)
		inputLayout.addView(cb, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		checkMap.put(key, cb)
	}

	fun addSafeButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
		val b = createButton(title).themeGreen()
		inputLayout.addView(b, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		b.setOnClickListener { _onButtonClick(key) }
	}

	fun addRedButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
		val b = createButton(title).themeRed()
		inputLayout.addView(b, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		b.setOnClickListener { _onButtonClick(key) }
	}

	fun addWhiteButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
		val b = createButton(title).themeWhite()
		inputLayout.addView(b, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		b.setOnClickListener { _onButtonClick(key) }
	}

	private fun _onButtonClick(key: String) {
		hideInputMethod()
		onButtonClick(key)
	}

	abstract fun onButtonClick(key: String)
}
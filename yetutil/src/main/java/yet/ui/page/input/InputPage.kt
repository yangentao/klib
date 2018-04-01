package yet.ui.page.input

import android.content.Context
import android.view.ViewGroup
import android.widget.*
import yet.theme.Colors
import yet.theme.InputSize
import yet.ui.ext.*
import yet.ui.page.TitledPage
import yet.ui.util.TimeDown
import yet.ui.viewcreator.*
import yet.ui.widget.listview.itemview.TextDetailView
import yet.ui.widget.listview.itemview.textDetail
import yet.util.MyDate
import yet.util.ToastUtil
import yet.util.app.SmsCodeFill
import yet.util.back
import yet.util.fore
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
	private val validMap = LinkedHashMap<String, InputValid>()
	private var codeEdit: EditText? = null
	private var codeButton: Button? = null
	private var timeDownKey: String? = null
	private var codeClickTime: Long = 0


	fun edit(block: InputOption.() -> Unit = {}): EditText {
		val io = InputOption()
		io.height = InputSize.EditHeight
		io.block()
		if (io.inputValid.label.isEmpty()) {
			io.inputValid.label(io.hint)
		}
		val ed = inputLayout.editX(LParam.WidthFill.height(io.height).margins(io.marginLeft, io.marginTop, io.marginRight, io.marginBottom)) {
			padding(5, 2, 5, 2)
			hint = io.hint
			setText(io.value)
		}
		editList.add(io.key to ed)
		validMap[io.key] = io.inputValid
		return ed
	}

	fun phone(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入手机号"
			valid {
				phone11()
			}
			this.block()
		}
		ed.inputTypePhone()
		return ed
	}

	fun email(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入邮箱"
			valid {
				email()
			}
			this.block()
		}
		ed.inputTypeEmail()
		return ed
	}

	fun number(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			valid {
				numbers()
			}
			this.block()
		}
		ed.inputTypeNumber()
		return ed
	}

	fun password(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入密码"
			valid {
				notEmpty()
			}
			this.block()
		}
		ed.inputTypePassword()
		return ed
	}

	fun passwordAgain(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请再次输入密码"
			valid {
				notEmpty()
			}
			this.block()
		}
		ed.inputTypePassword()
		return ed
	}

	fun passwordNumber(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入数字密码"
			valid {
				notEmpty()
				numbers()
			}
			this.block()
		}
		ed.inputTypePasswordNumber()
		return ed
	}

	fun passwordNumberAgain(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请再次输入数字密码"
			valid {
				notEmpty()
				numbers()
			}
			this.block()
		}
		ed.inputTypePasswordNumber()
		return ed
	}

	fun checkbox(block: InputOption.() -> Unit = {}): CheckBox {
		val io = InputOption()
		io.height = ViewGroup.LayoutParams.WRAP_CONTENT
		io.block()
		if (io.inputValid.label.isEmpty()) {
			io.inputValid.label(io.hint)
		}
		io.inputValid.label(io.hint)
		val cb = inputLayout.checkBox(LParam.WidthFill.height(io.height).margins(io.marginLeft, io.marginTop, io.marginRight, io.marginBottom)) {
			padding(10, 5, 5, 5)
			this.hint = io.hint
			if (io.value.isEmpty()) {
				this.text = io.hint
			} else {
				this.text = io.value
			}
		}
		checkMap[io.key] = cb
		validMap[io.key] = io.inputValid
		return cb
	}

	fun static(label: String, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0)) {
			text = label
		}
	}

	fun label(label: String, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().heightWrap().margins(0, marginTop, 0, 0)) {
			text = label
		}
	}

	fun button(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		val b = inputLayout.button(LParam.WidthFill.HeightWrap.margins(0, marginTop, 0, 0)) {
			setOnClickListener { _onButtonClick(key) }
			text = title
		}
		return b
	}

	fun buttonSafe(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		return button(key, title, marginTop).themeGreen()
	}

	fun buttonRed(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		return button(key, title, marginTop).themeRed()
	}

	fun buttonWhite(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		return button(key, title, marginTop).themeWhite()
	}

	fun textDetail(title: String, marginTop: Int = inputMarginTop): TextDetailView {
		return inputLayout.textDetail(LParam.WidthFill.height(INPUT_HEIGHT).margins(0, marginTop, 0, 0)) {
			padding(0, 0, 0, 0)
			detailView.textSizeB().gravityCenter().padding(10, 5, 10, 5)
			detailView.miniWidthDp(100)
			detailView.miniHeightDp(InputSize.ButtonHeightSmall - 10)
			textView.text = title
		}
	}

	fun date(key: String, title: String, format: String = MyDate.FORMAT_DATE, marginTop: Int = inputMarginTop): TextDetailView {
		val v = textDetail(title, marginTop)
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
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


	fun select(key: String, title: String, value: String, values: List<Pair<String, String>>) {
		val v = textDetail(title)
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		selectMap[key] = v
		val item = values.find { it.first == value }
		v.setDetail(item?.second)
		v.tag = item?.first
		v.onClick {
			itemSelectN(values.map { it.second }) {
				v.tag = values[it].first
				v.setDetail(values[it].second)
			}

		}
	}

	fun valid(): Boolean {
		for ((k, v) in validMap.entries) {
			val ed = editList.find { it.first == k }!!.second
			val info = v.checkAll(ed)
			if (info != null) {
				alert(info)
				return false
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
		if (v != null && v.trimText) {
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


	fun getSelectValue(key: String): String {
		return selectMap[key]?.tag as? String ?: ""
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
		if (timeDownKey.isNotEmpty()) {
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


	private fun _onButtonClick(key: String) {
		hideInputMethod()
		onButtonClick(key)
	}

	abstract fun onButtonClick(key: String)
}
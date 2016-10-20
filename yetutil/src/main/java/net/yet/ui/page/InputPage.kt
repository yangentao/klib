package net.yet.ui.page

import android.content.Context
import android.widget.*
import net.yet.theme.InputSize
import net.yet.ui.ext.*
import net.yet.ui.res.Img
import net.yet.ui.util.TimeDown
import net.yet.util.ToastUtil
import net.yet.util.Util
import net.yet.util.app.SmsCodeFill
import net.yet.util.back
import net.yet.util.fore
import java.util.*
import java.util.regex.Pattern

/**
 * Created by entaoyang@163.com on 2016-10-20.
 */

abstract class InputPage : TitledPage() {

	lateinit var inputLayout: LinearLayout

	var INPUT_HEIGHT = InputSize.EditHeight
	var inputMarginTop = 10
	var buttonMarginTop = 30

	private val editMap = HashMap<String, EditText>()
	private val checkMap = HashMap<String, CheckBox>()
	private var codeEdit: EditText? = null
	private var codeButton: Button? = null
	private var timeDownKey: String? = null
	private var codeClickTime: Long = 0


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		inputLayout = context.createLinearVertical().padding(40, 25, 40, 5)
		contentView.addViewParam(inputLayout) {
			widthFill().heightWrap()
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
		return editMap[key]?.text?.toString() ?: ""
	}

	fun setText(key: String, text: String) {
		editMap[key]?.setText(text)
	}

	fun disableEdit(key: String) {
		editMap[key]?.isEnabled = false
	}


	private fun makeEdit(hint: String, marginTop: Int): EditText {
		val ed = createEditText().hint(hint)
		ed.padding(5, 2, 5, 2)
		linearParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0).set(ed)
		return ed
	}

	fun addEdit(key: String, hint: String, marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		inputLayout.addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addPhone(key: String, hint: String = "请输入手机号", marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePhone()
		inputLayout.addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addEmail(key: String, hint: String = "请输入邮箱", marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypeEmail()
		inputLayout.addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addNumber(key: String, hint: String, marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypeNumber()
		inputLayout.addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addPasswordAgain(key: String): EditText {
		return addPassword(key, "请再次输入密码", inputMarginTop)
	}

	fun addPassword(key: String, hint: String = "请输入密码", marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePassword()
		inputLayout.addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addPasswordNumber(key: String, hint: String = "请输入数字密码", marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePasswordNumber()
		inputLayout.addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun searchSmsCode() {
		if (this.codeEdit != null && codeClickTime != 0L) {
			SmsCodeFill.searchSmsCode(this.codeEdit as TextView, codeClickTime)
		}
	}

	fun startTimeDown(seconds: Int) {
		fore {
			TimeDown.startClick(timeDownKey, seconds, codeButton)
		}
	}

	fun addVerifyCode(timeDownKey: String, phoneEditKey: String, block: (String) -> Unit) {
		addVerifyCode(timeDownKey, phoneEditKey, inputMarginTop, block)
	}

	fun addVerifyCode(timeDownKey: String, phoneEditKey: String, marginTop: Int, block: (String) -> Unit) {
		val verifyLayout = activity.createLinearHorizontal()
		codeEdit = createEditText().hint("输入验证码")
		codeEdit?.inputTypeNumber()
		verifyLayout.addView(codeEdit, linearParam().width(0).weight(1f).height(InputSize.EditHeight))
		codeButton = createButton("获取验证码").themeWhite()
		verifyLayout.addView(codeButton, linearParam().widthWrap().heightWrap().margins(3, 0, 0, 0))
		inputLayout.addView(verifyLayout, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))

		this.timeDownKey = timeDownKey
		if (Util.notEmpty(timeDownKey)) {
			TimeDown.updateView(this.timeDownKey, codeButton)
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
		val cb = activity.createCheckbox()
		cb.text = title
		val d = Img.namedStates("checkbox", true)
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
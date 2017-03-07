package net.yet.ui.widget

import android.content.Context
import android.view.View
import android.widget.*
import net.yet.R
import net.yet.theme.Colors
import net.yet.theme.InputSize
import net.yet.ui.ext.*
import net.yet.ui.page.BaseFragment
import net.yet.ui.res.ImageStated
import net.yet.ui.res.ResConst
import net.yet.ui.res.ResStr
import net.yet.ui.util.RectDrawable
import net.yet.ui.util.TimeDown
import net.yet.util.ToastUtil
import net.yet.util.Util
import net.yet.util.app.SmsCodeFill
import net.yet.util.back
import net.yet.util.fore
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */

class InputPanel constructor(context: Context, private val fragment: BaseFragment? = null) : LinearLayout(context) {
	var INPUT_HEIGHT = 45
	private val editMap = HashMap<String, EditText>()
	private val checkMap = HashMap<String, CheckBox>()
	private val buttonMap = HashMap<String, Button>()
	private var codeEdit: EditText? = null
	private var codeButton: Button? = null
	private var timeDownKey: String? = null
	private var codeClickTime: Long = 0


	var inputMarginTop = 10
	var buttonMarginTop = 30

	var onButtonClick: (String) -> Unit = {
	}
	var onRequestCodeClick: (InputPanel, String) -> Unit = { p, phone ->

	}

	init {
		this.orientationVertical().padding(40, 25, 40, 5)
	}


	fun isCheck(key: String): Boolean {
		return checkMap[key]?.isChecked ?: false
	}

	fun setCheck(key: String, check: Boolean) {
		checkMap[key]?.isChecked = check
	}

	val code: String?
		get() {
			return codeEdit?.text.toString()
		}

	fun getText(key: String): String {
		return editMap[key]?.text.toString()
	}

	fun setText(key: String, text: String) {
		editMap[key]?.setText(text)
	}

	fun disableEdit(key: String) {
		editMap[key]?.isEnabled = false
	}

	fun enableButton(key: String, enable: Boolean) {
		buttonMap[key]?.isEnabled = enable
	}

	fun setButtonText(key: String, text: String) {
		buttonMap[key]?.text = text
	}

	fun getButtonText(key: String): String? {
		return buttonMap[key]?.text?.toString()
	}

	fun button(key: String): Button {
		return buttonMap[key]!!
	}

	fun edit(key: String): EditText {
		return editMap[key]!!
	}


	private fun makeEdit(hint: String, marginTop: Int): EditText {
		val ed = context.createEditText().hint(hint)
		ed.padding(5, 2, 5, 2)
		linearParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0).set(ed)
		return ed
	}

	fun addEdit(key: String, hint: String, marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addPhone(key: String, hint: String = ResStr(R.string.yet_phone_input), marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePhone()
		addView(ed)
		editMap.put(key, ed)
		return ed
	}

	@JvmOverloads fun addNumber(key: String, hint: String, marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypeNumber()
		addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addPasswordAgain(key: String): EditText {
		return addPassword(key, ResStr(R.string.yet_pwd_again), inputMarginTop)
	}

	fun addPassword(key: String, hint: String = ResStr(R.string.yet_pwd_input), marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePassword()
		addView(ed)
		editMap.put(key, ed)
		return ed
	}

	fun addPasswordNumber(key: String, hint: String = ResStr(R.string.yet_pwd_input), marginTop: Int = inputMarginTop): EditText {
		val ed = makeEdit(hint, marginTop)
		ed.inputTypePasswordNumber()
		addView(ed)
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
		val llDraw = RectDrawable(Colors.TRANS).corner(InputSize.EditCorner).stroke(1, Colors.EditFocus).value
		val editDraw = RectDrawable(Colors.WHITE).corners(InputSize.EditCorner, 0, 0, InputSize.EditCorner).value
		val btnNormalDraw = RectDrawable(Colors.Theme).corners(0, InputSize.EditCorner, InputSize.EditCorner, 0).value
		val btnPressDraw = RectDrawable(Colors.Fade).corners(0, InputSize.EditCorner, InputSize.EditCorner, 0).value
		val btnDisableDraw = RectDrawable(Colors.Disabled).corners(0, InputSize.EditCorner, InputSize.EditCorner, 0).value
		val btnDraw = ImageStated(btnNormalDraw).pressed(btnPressDraw).enabled(btnDisableDraw, false).value

		val verifyLayout = context.createLinearHorizontal().backDrawable(llDraw).padding(1)
		codeEdit = context.createEditText().text("输入验证码").inputTypeNumber().backDrawable(editDraw).padding(15, 0, 15, 0)
		verifyLayout.addViewParam(codeEdit!!) { width(0).weight(1f).heightFill() }
		codeButton = context.createButton("获取验证码").backDrawable(btnDraw).textColorWhite()
		verifyLayout.addViewParam(codeButton!!) { widthWrap().heightFill() }
		addViewParam(verifyLayout) { widthFill().height(InputSize.EditHeight).margins(0, marginTop, 0, 0) }

		this.timeDownKey = timeDownKey
		if (Util.notEmpty(timeDownKey)) {
			TimeDown.updateView(this.timeDownKey, codeButton)
		}
		codeButton!!.setOnClickListener(View.OnClickListener {
			codeClickTime = System.currentTimeMillis()
			val phone = getText(phoneEditKey)
			if (Util.length(phone) < 11) {
				ToastUtil.show("请输入正确的手机号")
			} else {
				startTimeDown(60)
				back {
					block(phone)
				}
			}
		})
	}

	fun addCheckbox(key: String, title: String, marginTop: Int = inputMarginTop) {
		val cb = context.createCheckbox()
		cb.text = title
		val d = ResConst.checkbox()
		cb.buttonDrawable = d
		cb.padding(20, 5, 5, 5)
		this.addView(cb, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		checkMap.put(key, cb)
	}

	fun addSafeButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
		val b = context.createButton(title).themeGreen()
		this.addView(b, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		b.setOnClickListener { _onButtonClick(key) }
		buttonMap[key] = b
	}

	fun addRedButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
		val b = context.createButton(title).themeRed()
		this.addView(b, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		b.setOnClickListener { _onButtonClick(key) }
		buttonMap[key] = b
	}

	fun addWhiteButton(key: String, title: String, marginTop: Int = buttonMarginTop) {
		val b = context.createButton(title).themeWhite()
		this.addView(b, linearParam().widthFill().heightWrap().margins(0, marginTop, 0, 0))
		b.setOnClickListener { _onButtonClick(key) }
		buttonMap[key] = b
	}

	fun _onButtonClick(key: String) {
		fragment?.hideInputMethod()
		onButtonClick(key)
	}

}

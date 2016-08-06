package net.yet.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import net.yet.ui.ext.genId
import net.yet.ui.res.ResConst
import net.yet.util.Util
import net.yet.util.fore

class EditTextX(context: Context) : EditText(context) {
	private var x: Drawable = ResConst.editClear()

	init {
		genId()
		this.setOnTouchListener(View.OnTouchListener { v, event ->
			if (this@EditTextX.compoundDrawables[2] == null) {
				return@OnTouchListener false
			}
			if (event.action != MotionEvent.ACTION_UP) {
				return@OnTouchListener false
			}
			if (event.x > this@EditTextX.width - this@EditTextX.paddingRight
					- IMAGE_WIDTH) {
				this@EditTextX.setText("")
				this@EditTextX.setCompoundDrawables(null, null, null, null)
				fore{
					Util.hideInputMethod(this@EditTextX)
				}
			}
			false
		})
		this.addTextChangedListener(object : TextWatcher {

			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
				this@EditTextX.setCompoundDrawables(null, null, if (this@EditTextX.text.toString() == "")
					null
				else
					x, null)
			}

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

			}

			override fun afterTextChanged(s: Editable) {

			}
		})
	}

	companion object {
		val IMAGE_WIDTH = 25
	}
}

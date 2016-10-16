package net.yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import net.yet.R
import net.yet.ui.ext.createEditText
import net.yet.ui.ext.dp
import net.yet.util.app.Res

abstract class InputDialog {
	private var inputType = InputType.TYPE_CLASS_TEXT
	var isOK = false
		private set

	fun inputTypeText() {
		this.inputType = InputType.TYPE_CLASS_TEXT
	}

	fun inputTypePhone() {
		this.inputType = InputType.TYPE_CLASS_PHONE
	}

	fun inputTypeNumber() {
		this.inputType = InputType.TYPE_CLASS_NUMBER
	}

	fun inputTypePassword() {
		this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
	}
	fun inputTypePasswordNumber() {
		this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
	}

	fun inputTypeEmail() {
		this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
	}

	fun show(context: Context, title: String) {
		show(context, title, null)
	}
	fun show(context: Context, title: String, defalutValue: String?) {
		create(context, title, defalutValue).show()
	}

	fun create(context: Context, title: String, defalutValue: String? = null): AlertDialog {
		val builder = AlertDialog.Builder(context)
		builder.setTitle(title)
		//		final EditText edit = XView.id(new EditText(context));
		val edit = context.createEditText()
		edit.minimumHeight = dp(45)
		edit.inputType = inputType
		edit.setText(defalutValue)
		builder.setView(edit)
		builder.setPositiveButton(Res.str(R.string.ok), DialogInterface.OnClickListener { dlg, arg1 ->
			isOK = true
			dlg.dismiss()
			onOK(edit.text.toString())
		})
		builder.setNegativeButton(Res.str(R.string.cancel), DialogInterface.OnClickListener { dlg, arg1 -> dlg.dismiss() })
		val dlg = builder.create()
		dlg.setCanceledOnTouchOutside(true)
		dlg.setCancelable(true)
		dlg.setOnDismissListener { this@InputDialog.onDismiss() }
		return dlg
	}

	open fun onDismiss() {

	}

	abstract fun onOK(text: String?)
}

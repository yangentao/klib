package net.yet.yetlibdemo

import android.content.Context
import android.widget.LinearLayout
import yet.theme.InputSize
import yet.ui.ext.backDrawable
import yet.ui.page.input.InputPage
import yet.ui.res.D

/**
 * Created by entaoyang@163.com on 2018-03-30.
 */

class LoginPage : InputPage() {

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			title = "登录"
			showBack()
		}
		edit {
			key = "user"
			hint = "用户名"
			valid {
				trimText()
				minLength(2)
				require()
			}
		}
		password {
			key = "pwd"
			hint = "密码"
		}
		checkbox {
			key = "check"
			value = "自动登录"
		}
		buttonSafe("login", "登录").backDrawable(D.buttonGreen(InputSize.ButtonHeight / 2))


	}

	override fun onButtonClick(key: String) {
		valid()

	}

}
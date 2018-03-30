package yet.ui.page.input

import android.view.ViewGroup

/**
 * Created by entaoyang@163.com on 2018-03-30.
 */
class InputOption {
	var key:String = ""
	var marginTop:Int = 10
	var marginLeft:Int = 0
	var marginRight:Int = 0
	var marginBottom:Int = 0
	var hint:String = ""
	var value:String = ""
	var height:Int = ViewGroup.LayoutParams.WRAP_CONTENT
	var inputValid = InputValid()

	fun valid(block: InputValid.()->Unit) {
		this.inputValid.block()
	}

}
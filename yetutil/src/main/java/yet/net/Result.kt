package yet.net

import yet.yson.YsonArray
import yet.yson.YsonObject

/**
 * Created by entaoyang@163.com on 2018-04-07.
 */

class Result(val httpResult: HttpResult) {
	var CodeOKValue = Result.CODE_OK
	var CodeName = Result.CODE
	var DataName = Result.DATA
	val MsgName = Result.MSG

	val jo: YsonObject = httpResult.ysonObject() ?: YsonObject()
	val OK: Boolean
		get() {
			return httpResult.OK && code == CodeOKValue
		}

	val code: Int
		get() {
			if (httpResult.OK) {
				return jo.int(CodeName) ?: -1
			} else {
				return httpResult.responseCode
			}
		}

	val msg: String
		get() {
			if (httpResult.OK) {
				return jo.str(MsgName) ?: ""
			} else {
				return httpResult.errorMsg ?: "未知错误"
			}
		}

	val data: YsonObject?
		get() {
			return jo.obj(DataName)
		}

	val dataArray: YsonArray?
		get() {
			return jo.arr(DataName)
		}
	val dataInt: Int?
		get() {
			return jo.int(DataName)
		}
	val dataLong: Long?
		get() {
			return jo.long(DataName)
		}
	val dataDouble: Double?
		get() {
			return jo.real(DataName)
		}
	val dataFloat: Float?
		get() {
			return jo.real(DataName)?.toFloat()
		}
	val dataString: String?
		get() {
			return jo.str(DataName)
		}

	override fun toString(): String {
		return jo.toString()
	}

	companion object {
		var CODE_OK = 0
		var CODE = "code"
		var DATA = "data"
		val MSG = "msg"

		val MSG_OK = "操作成功"
		val MSG_FAILED = "操作失败"

	}
}
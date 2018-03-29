package yet.yson

class YsonString(val data: String) : YsonValue() {

	constructor(v: Char) : this(String(charArrayOf(v)))
	constructor(v: StringBuffer) : this(v.toString())
	constructor(v: StringBuilder) : this(v.toString())

	override fun yson(buf: StringBuilder) {
		buf.append("\"")
		buf.append(escapeJson(data))
		buf.append("\"")
	}

	override fun equals(other: Any?): Boolean {
		if (other is YsonString) {
			return other.data == data
		}
		return false
	}

	override fun hashCode(): Int {
		return data.hashCode()
	}

	override fun preferBufferSize(): Int {
		return data.length + 8
	}

}
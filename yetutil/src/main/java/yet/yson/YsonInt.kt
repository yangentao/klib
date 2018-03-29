package yet.yson

import java.math.BigInteger

class YsonInt(val data: Long) : YsonValue() {


	override fun yson(buf: StringBuilder) {
		buf.append(data.toString())
	}

	override fun equals(other: Any?): Boolean {
		if (other is YsonInt) {
			return other.data == data
		}
		return false
	}

	override fun hashCode(): Int {
		return data.hashCode()
	}

	override fun preferBufferSize(): Int {
		return 12
	}

	constructor(v: Byte) : this(v.toLong())
	constructor(v: Short) : this(v.toLong())
	constructor(v: Int) : this(v.toLong())
	constructor(v: BigInteger) : this(v.toLong())

}
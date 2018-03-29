package yet.yson

import java.math.BigDecimal

class YsonReal(val data: Double) : YsonValue() {

	constructor(v: Float) : this(v.toDouble())
	constructor(v: BigDecimal) : this(v.toDouble())

	override fun yson(buf: StringBuilder) {
		buf.append(data.toString())
	}

	override fun equals(other: Any?): Boolean {
		if (other is YsonReal) {
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

}
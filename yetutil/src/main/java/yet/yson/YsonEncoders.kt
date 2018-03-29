package yet.yson

import java.math.BigDecimal
import java.math.BigInteger

interface IToYson {
	fun toYsonValue(v: Any): YsonValue
}

object BoolToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonBool(v as Boolean)
	}
}

object ByteToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt(v as Byte)
	}
}

object ShortToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt(v as Short)
	}
}

object IntToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt(v as Int)
	}
}

object LongToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt(v as Long)
	}
}

object BigIntegerToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt(v as BigInteger)
	}
}

object FloatToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonReal(v as Float)
	}
}

object DoubleToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonReal(v as Double)
	}
}

object BigDecimalToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonReal(v as BigDecimal)
	}
}

object CharToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonString(v as Char)
	}
}

object StringToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonString(v as String)
	}
}

object StringBufferToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonString(v as StringBuffer)
	}
}

object StringBuilderToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonString(v as StringBuilder)
	}
}

object ByteArrayToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonBlob(v as ByteArray)
	}
}

object BlobToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonBlob(v as java.sql.Blob)
	}
}

object DateToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt((v as java.util.Date).time)
	}
}

object SQLTimestampToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonInt((v as java.sql.Timestamp).time)
	}
}

object SQLTimeToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonString((v as java.sql.Time).toString())
	}
}

object SQLDateToYson : IToYson {
	override fun toYsonValue(v: Any): YsonValue {
		return YsonString((v as java.sql.Date).toString())
	}
}


package yet.ext

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import yet.json.GSON
import yet.util.MyDate
import yet.yson.YsonArray
import yet.yson.YsonObject
import java.sql.Time
import kotlin.reflect.KClassifier

/**
 * Created by entaoyang@163.com on 2018/4/4.
 */

interface ITextConvert {
	fun fromText(text: String): Any?
	fun toText(value: Any): String {
		return value.toString()
	}

	val defaultValue: Any
}

val TextConverts = mapOf<KClassifier, ITextConvert>(
		String::class to StringText,
		Boolean::class to BoolText,
		java.lang.Boolean::class to BoolText,
		Byte::class to ByteText,
		java.lang.Byte::class to ByteText,
		Short::class to ShortText,
		java.lang.Short::class to ShortText,
		Char::class to CharText,
		java.lang.Character::class to CharText,
		Int::class to IntText,
		java.lang.Integer::class to IntText,
		Long::class to LongText,
		java.lang.Long::class to LongText,
		Float::class to FloatText,
		java.lang.Float::class to FloatText,
		Double::class to DoubleText,
		java.lang.Double::class to DoubleText,
		YsonObject::class to YsonObjectText,
		YsonArray::class to YsonArrayText,
		java.sql.Date::class to SQLDateText,
		java.sql.Time::class to SQLTimeText,
		java.util.Date::class to UtilDateText,
		JsonObject::class to JsonObjectText,
		JsonArray::class to JsonArrayText

)

object StringText : ITextConvert {
	override val defaultValue: Any = ""
	override fun fromText(text: String): Any? {
		return text
	}
}

object BoolText : ITextConvert {
	override val defaultValue: Any = false
	override fun fromText(text: String): Any? {
		return text == "true"
	}
}

object ByteText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.toByteOrNull()
	}
}

object ShortText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.toShortOrNull()
	}
}

object CharText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.firstOrNull()
	}
}

object IntText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.toIntOrNull()
	}
}

object LongText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.toLongOrNull()
	}
}

object FloatText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.toFloatOrNull()
	}
}

object DoubleText : ITextConvert {
	override val defaultValue: Any = 0
	override fun fromText(text: String): Any? {
		return text.toDoubleOrNull()
	}
}

object YsonObjectText : ITextConvert {
	override val defaultValue: Any = YsonObject()
	override fun fromText(text: String): Any? {
		return YsonObject(text)
	}
}

object YsonArrayText : ITextConvert {
	override val defaultValue: Any = YsonArray()
	override fun fromText(text: String): Any? {
		return YsonArray(text)
	}
}

object JsonObjectText : ITextConvert {
	override val defaultValue: Any = JsonObject()
	override fun fromText(text: String): Any? {
		return GSON.parseObject(text)
	}
}

object JsonArrayText : ITextConvert {
	override val defaultValue: Any = YsonArray()
	override fun fromText(text: String): Any? {
		return GSON.parseArray(text)
	}
}

//yyy-MM-dd
object SQLDateText : ITextConvert {
	override val defaultValue: Any = java.sql.Date(0)
	override fun fromText(text: String): Any? {
		return MyDate.parseDate(text)?.sqlDate
	}

	override fun toText(value: Any): String {
		return MyDate((value as java.sql.Date).time).formatDate()
	}
}

//"HH:mm:ss"
object SQLTimeText : ITextConvert {
	override val defaultValue: Any = java.sql.Time(0)
	override fun fromText(text: String): Any? {
		return MyDate.parseTime(text)?.sqlTime
	}

	override fun toText(value: Any): String {
		return MyDate((value as java.sql.Time).time).formatTime()
	}
}

//"yyyy-MM-dd HH:mm:ss.SSS"
object UtilDateText : ITextConvert {


	override val defaultValue: Any = java.util.Date(0)
	override fun fromText(text: String): Any? {
		return MyDate.parseDateTimeX(text)?.utilDate
	}

	override fun toText(value: Any): String {
		return MyDate((value as java.util.Date).time).formatDateTimeX()
	}
}




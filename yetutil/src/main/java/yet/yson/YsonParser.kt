package yet.yson

private val CR: Char = '\r'
private val LF: Char = '\n'
private val SP: Char = ' '
private val TAB: Char = '\t'
private val DOT: Char = '.'
private val QT: Char = '\"'
private val SQT: Char = '\''
private val WHITE: Set<Char> = hashSetOf(CR, LF, SP, TAB)
private val NUM_START: Set<Char> = hashSetOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-')
private val NUMS: Set<Char> = hashSetOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', DOT, 'e', 'E', '+', '-')
private val ESCAP: Set<Char> = hashSetOf(SQT, '\\', '/', 'b', 'f', 'n', 'r', 't', 'u')

private val Char.isWhite: Boolean get() = this in WHITE

class YsonParser(val text: String) {
	private val data: CharArray = text.toCharArray()
	private var current: Int = 0

	private val end: Boolean get() = current >= data.size
	private val currentChar: Char get() = data[current]

	fun parse(): YsonValue {
		skipWhite()
		if (end) {
			throw YsonError("空的字符串")
		}
		val ch = currentChar
		val v = when (ch) {
			'{' -> parseObject()
			'[' -> parseArray()
			QT -> parseString()
			't' -> parseTrue()
			'f' -> parseFalse()
			'n' -> parseNull()
			in NUM_START -> parseNumber()
			else -> throw YsonError("")
		}
		if (!shouldEnd()) {
			throw YsonError("应该结束")
		}
		return v
	}

	fun parseArray(): YsonArray {
		skipWhite()
		tokenc('[')
		val ya = YsonArray()
		while (!end) {
			skipWhite()
			if (currentChar == ']') {
				break
			}
			if (currentChar == ',') {
				next()
				continue
			}
			val yv = parse()
			ya.data.add(yv)
		}
		tokenc(']')
		return ya
	}

	fun parseObject(): YsonObject {
		skipWhite()
		tokenc('{')
		val yo = YsonObject()
		while (!end) {
			skipWhite()
			if (currentChar == '}') {
				break
			}
			if (currentChar == ',') {
				next()
				continue
			}
			val key = parseString()
			tokenc(':')
			val yv = parse()
			yo.data[key.data] = yv
		}
		tokenc('}')
		return yo
	}

	fun parseString(): YsonString {
		skipWhite()
		tokenc(QT)//字符串以双引号开始
		val buf = StringBuilder(64)
		var escing = false
		while (!end) {
			val ch = currentChar
			if (!escing) {
				if (ch == QT) {//字符串结束,双引号
					break
				}
				next()
				if (ch == '\\') {//开始转义
					escing = true
					continue
				}
				buf.append(ch)//正常字符
			} else {
				escing = false
				next()
				when (ch) {
					QT, SQT, '\\', '/' -> buf.append(ch)
					'n' -> buf.append(LF)
					'r' -> buf.append(CR)
					't' -> buf.append(TAB)
					'u' -> {
						if (current + 4 < text.length) {
							val sb = StringBuilder(4)
							sb.append(text[current + 0])
							sb.append(text[current + 1])
							sb.append(text[current + 2])
							sb.append(text[current + 3])
							current += 4
							val n = sb.toString().toInt(16)
							buf.append(n.toChar())
						} else {
							throw YsonError("期望是unicode字符")
						}
					}
					else -> {
						buf.append(ch)
					}

				}
			}
		}
		if (escing) {
			throw  YsonError("解析错误,转义,")
		}
		tokenc(QT)
		return YsonString(buf.toString())
	}

	fun parseNumber(): YsonValue {
		skipWhite()
		val buf = StringBuilder(32)
		while (!end) {
			val c = currentChar
			if (c !in NUMS) {
				break
			}
			buf.append(c)
			next()
		}
		val s = buf.toString()
		if (s.isEmpty()) {
			throw YsonError("非数字")
		}
		if ('.' in s) {
			val d = s.toDouble()
			return YsonReal(d)
		}
		val n = s.toLong()
		return YsonInt(n)
	}

	fun parseTrue(): YsonBool {
		skipWhite()
		tokens("true")
		return YsonBool.True
	}

	fun parseFalse(): YsonBool {
		skipWhite()
		tokens("false")
		return YsonBool.False
	}

	fun parseNull(): YsonNull {
		skipWhite()
		tokens("null")
		return YsonNull.inst
	}

	private fun next() {
		current += 1
	}

	private fun shouldEnd(): Boolean {
		this.skipWhite()
		return this.end
	}

	private fun skipWhite() {
		while (!end) {
			if (currentChar.isWhite) {
				next()
			} else {
				return
			}
		}
	}

	private fun isChar(c: Char): Boolean {
		return currentChar == c
	}

	private fun tokenc(c: Char) {
		skipWhite()
		if (currentChar != c) {
			throw YsonError("期望是字符$c", text, current)
		}
		next()
		skipWhite()
	}

	private fun tokens(s: String) {
		skipWhite()
		for (c in s) {
			if (currentChar != c) {
				throw YsonError("期望是字符串$s", text, current)
			}
			next()
		}
		skipWhite()
	}

}
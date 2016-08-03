package net.yet.ext

/**
 * Created by entaoyang@163.com on 16/6/17.
 */


fun Short.lowByte(): Byte {
	return (this.toInt() and 0xFF).toByte()
}

fun Short.highByte(): Byte {
	return (this.toInt() shr 8).toByte()
}

fun makeShort(hi: Byte, lo: Byte): Short {
	return ((hi.toInt() shl 8) + (lo.toInt() and 0xff)).toShort()
}

fun Int.hasBits(bitsFlag: Int): Boolean {
	return (this and bitsFlag) != 0
}

fun Int.removeBits(bitsFlag: Int): Int {
	return this and bitsFlag.inv()
}
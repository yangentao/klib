package net.yet.util.database

import java.util.*


class WhereNode(val s: String) {
	val args = ArrayList<String>()
	val argsArray: Array<String> get() = Array<String>(args.size) { args[it] }
	override fun toString(): String {
		return s
	}
}

infix fun String.EQ(value: String?): WhereNode {
	if (value != null) {
		val n = WhereNode("$this=?")
		n.args.add(value)
		return n
	} else {
		return IsNull(this)
	}
}

infix fun String.EQ(value: Number): WhereNode {
	return this.EQ(value.toString())
}


infix fun String.NE(value: String?): WhereNode {
	if (value != null) {
		val n = WhereNode("$this!=?")
		n.args.add(value)
		return n
	} else {
		return NotNull(this)
	}
}

infix fun String.NE(value: Number): WhereNode {
	return this.NE(value.toString())
}

infix fun String.GE(value: String): WhereNode {
	val n = WhereNode("$this>=?")
	n.args.add(value)
	return n
}

infix fun String.GE(value: Number): WhereNode {
	return this.GE(value.toString())
}


infix fun String.GT(value: String): WhereNode {
	val n = WhereNode("$this>?")
	n.args.add(value)
	return n
}

infix fun String.GT(value: Number): WhereNode {
	return this.GT(value.toString())
}


infix fun String.LE(value: String): WhereNode {
	val n = WhereNode("$this<=?")
	n.args.add(value)
	return n
}

infix fun String.LE(value: Number): WhereNode {
	return this.LE(value.toString())
}

infix fun String.LT(value: String): WhereNode {
	val n = WhereNode("$this<?")
	n.args.add(value)
	return n
}

infix fun String.LT(value: Number): WhereNode {
	return this.LT(value.toString())
}

fun IsNull(column: String): WhereNode {
	return WhereNode("$column IS NULL")
}

fun NotNull(column: String): WhereNode {
	return WhereNode("$column IS NOT NULL")
}

infix fun WhereNode.AND(item: WhereNode): WhereNode {
	val n = WhereNode("($s) AND (${item.s})")
	n.args.addAll(this.args)
	n.args.addAll(item.args)
	return n
}

infix fun WhereNode.OR(item: WhereNode): WhereNode {
	val n = WhereNode("($s) OR (${item.s})")
	n.args.addAll(this.args)
	n.args.addAll(item.args)
	return n
}

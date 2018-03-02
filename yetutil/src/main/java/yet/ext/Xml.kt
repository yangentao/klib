package yet.ext

import org.w3c.dom.Document
import org.w3c.dom.Element
import yet.util.MyDate
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Created by entaoyang@163.com on 2016-09-04.
 */

fun XmlParse(s: String): Document? {
	val fac = DocumentBuilderFactory.newInstance()
	val db = fac.newDocumentBuilder()
	val stream = ByteArrayInputStream(s.toByteArray())
	return db.parse(stream)
}

fun Element.toXml(): String {
	val t = TransformerFactory.newInstance().newTransformer()
	val w = StringWriter(512)
	t.transform(DOMSource(this), StreamResult(w))
	return w.toString()
}

fun Element.toXmlDoc(): String {
	val t = TransformerFactory.newInstance().newTransformer()
	val w = StringWriter(512)
	t.transform(DOMSource(this.ownerDocument), StreamResult(w))
	return w.toString()
}

fun XmlRoot(name: String): Element {
	val fac = DocumentBuilderFactory.newInstance()
	val db = fac.newDocumentBuilder()
	val doc = db.newDocument()
	val e = doc.createElement(name)
	doc.appendChild(e)
	return e
}


fun Element.attr(name: String): String? {
	return this.getAttribute(name)
}

fun Element.attr(name: String, value: String): Element {
	this.setAttribute(name, value)
	return this
}

fun Element.text(): String? {
	return this.textContent?.trim()
}

val Element.text: String? get() {
	return this.textContent?.trim()
}
val Element.textDateTime: Long get() {
	return this.textDateTime()
}


//yyyy-MM-dd HH:mm:ss
fun Element.textDateTime(): Long {
	return MyDate.parseDateTime(text())?.time ?: 0L
}

fun Element.addText(text: String): Element {
	val node = this.ownerDocument.createTextNode(text)
	this.appendChild(node)
	return this
}

fun Element.element(name: String): Element? {
	val ls = this.getElementsByTagName(name)
	if (ls != null && ls.length > 0) {
		return ls.item(0) as? Element
	}
	return null
}

fun Element.e(name: String): Element? {
	return this.element(name)
}

fun Element.addElement(name: String): Element {
	val e = this.ownerDocument.createElement(name)
	appendChild(e)
	return e
}

fun Element.addElement(name: String, block: Element.() -> Unit): Element {
	val e = this.ownerDocument.createElement(name)
	appendChild(e)
	e.block()
	return e
}


fun Element.elements(name: String): List<Element> {
	val es = ArrayList<Element>(16)
	val ls = this.getElementsByTagName(name)
	if (ls != null) {
		for (i in 0..ls.length - 1) {
			val e = ls.item(i) as Element
			es.add(e)
		}
	}
	return es
}

fun Element.elist(name: String): List<Element> {
	return this.elements(name)
}


fun escapeXML(s: String): String {
	return s.replaceChars('<' to "&lt;", '>' to "&gt;", '&' to "&amp;", '"' to "&quot;", '\'' to "&apos;")
}
package net.yet.sqlite

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */


open class KEntityClass<out T> {
	val modelKClass = javaClass.enclosingClass.kotlin
	val modelClassJava: Class<*> = javaClass.enclosingClass as Class<T>

	fun findByID(key: String): T? {
		val session = KSession.current
		val mi = ModelInfo.find(modelKClass)
		val pk = mi.pk ?: return null
		session.from(modelKClass).where(pk.prop EQ key).query()

		return null
	}

	fun findByID(key: Int): T? {
		return null
	}

	fun findByID(key: Long): T? {
		return null
	}
}
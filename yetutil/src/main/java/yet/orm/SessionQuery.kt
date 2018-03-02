package yet.orm

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

/**
 * Created by entaoyang@163.com on 2017-03-10.
 */

//Session.named("yang").from(Person::class).where(Person::name EQ "yang").orderBy(Person::name).one()
fun Session.from(mcls: KClass<*>): ModelQuery {
	val mi = ModelInfo.find(mcls)
	TableCreator.check(db, mi)
	return ModelQuery(db, mcls)
}

fun <T> Session.findAll(mcls: KClass<*>, orderBy: KMutableProperty<*>? = null, asc: Boolean = true): ArrayList<T> {
	if (orderBy == null) {
		return this.from(mcls).all<T>()
	} else {
		if (asc) {
			return this.from(mcls).asc(orderBy).all<T>()
		} else {
			return this.from(mcls).desc(orderBy).all<T>()
		}
	}
}

fun <T> Session.findByKey(mcls: KClass<*>, key: String): T? {
	val mi = ModelInfo.find(mcls)
	return this.from(mcls).where(mi.pk!!.prop EQ key).one<T>()
}

fun <T> Session.find(mcls: KClass<*>, w: Where): ArrayList<T> {
	return this.from(mcls).where(w).all<T>()
}

fun Session.find(mcls: KClass<*>): ModelQuery {
	return this.from(mcls)
}
fun Session.dump(mcls: KClass<*>) {
	from(mcls).dump()
}
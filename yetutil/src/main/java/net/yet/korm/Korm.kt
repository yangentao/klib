package net.yet.korm

import android.database.sqlite.SQLiteDatabase
import net.yet.file.UserFile
import net.yet.util.app.App
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2016-12-13.
 */
class Person(var name: String, var age: String)

val DEFAULT_KORM_DB = "korm.db"


fun OpenDB(name: String): SQLiteDatabase {
	return App.get().openOrCreateDatabase(name, 0, null)
}

fun OpenDBUser(user: String, dbname: String = DEFAULT_KORM_DB): SQLiteDatabase {
	val file = UserFile.file(user, dbname)
	return SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.CREATE_IF_NECESSARY)
}

fun <T : Any> SQLiteDatabase.from( cls: KClass<T>, vararg otherCls:KClass<*>): KQuery<T> {
	return KQuery(this, cls, *otherCls)
}


fun tableNameOf(cls:Class<*>):String {
	return  cls.getAnnotation(Name::class.java)?.value ?: cls.simpleName
}

fun fieldNameOf(p: KProperty<*>): String {
	var tabName = tableNameOf(p.javaField?.declaringClass!!)
	val fname = p.javaField?.getAnnotation(Name::class.java)?.value ?: p.name
	return tabName + "." + fname
}


fun test() {
	val db = OpenDB("a.db")
	db.from(Person::class).where((Person::name EQ "Yang") AND (Person::age EQ "32"))
}

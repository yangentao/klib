package net.yet.sqlite

import android.database.sqlite.SQLiteDatabase
import net.yet.file.UserFile
import net.yet.util.app.App
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2016-12-13.
 */



class KSession(val db: SQLiteDatabase) {

	//Session.named("yang").from(Person::class).where(Person::name EQ "yang").orderBy(Person::name).one()
	fun from(modelClass: KClass<*>): KQuery {
		TableCreator.check(this, modelClass)
		return KQuery(db, modelClass)
	}

	fun existTable(name: String): Boolean {
		val c = db.rawQuery("select count(*) from sqlite_master where type='table' AND name=$name", null) ?: return false
		return KCursorResult(c).resultCount() > 0
	}


	companion object {
		fun named(dbname: String): KSession {
			val db = App.get().openOrCreateDatabase(dbname, 0, null)
			return KSession(db)
		}

		fun user(user: String, dbname: String = "korm.db"): KSession {
			val file = UserFile.file(user, dbname)
			val db = SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READWRITE or SQLiteDatabase.CREATE_IF_NECESSARY)
			return KSession(db)
		}
	}
}


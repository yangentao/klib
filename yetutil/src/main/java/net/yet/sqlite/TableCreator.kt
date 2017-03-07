package net.yet.sqlite

import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaType

/**
 * Created by entaoyang@163.com on 2017-03-07.
 */

class TableCreator(val session: KSession, val cls: KClass<*>) {

	private fun defTable() {
		val ls = cls.declaredMemberProperties.filter {
			(it is KMutableProperty<*>) and !it.isAbstract and !it.isConst and !it.isLateinit and (it.javaField != null)
		}
		val defList = ArrayList<String>()
		for (p in ls) {
			defProp(p as KMutableProperty<*>)
		}

	}

	private fun defProp(p: KMutableProperty<*>): String {
		val name = fieldNameOf(p)
		val sqlType = SQL_TYPE_MAP[p.returnType.javaType]
		if (sqlType == null) {
			return ""
		} else {
			return name + " " + sqlType.toString()
		}
	}

	private fun defIndex() {

	}

	companion object {
		val checkedSet = HashSet<String>()
		fun check(session: KSession, modelClass: KClass<*>) {
			synchronized(checkedSet) {
				doCheck(session, modelClass)
			}

		}

		private fun doCheck(session: KSession, modelClass: KClass<*>) {
			val k = session.db.path + "@" + modelClass.simpleName
			if (k in checkedSet) {
				return
			}
			checkedSet.add(k)

			val b = session.existTable(tableNameOf(modelClass))
			if (b) {

			} else {
				val t = TableCreator(session, modelClass)
				t.defTable()
				t.defIndex()
			}
		}
	}


}
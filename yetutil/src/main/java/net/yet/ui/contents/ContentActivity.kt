package net.yet.ui.contents

import android.os.Bundle
import net.yet.ui.activities.BaseActivity
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-07-28.
 */


open class ContentActivity : BaseActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	companion object {
		val map = Hashtable<Long, Class<out Any>>()
		val argMap = Hashtable<Long, HashMap<String, Any>>()


	}

}
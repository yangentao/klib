package net.yet.sys.contact

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.theme.Str
import net.yet.ui.page.FilterIndexMultiSelectPage
import net.yet.ui.widget.listview.itemview.TextDetailView
import net.yet.util.log.log
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */

class ContactSelectPage() : FilterIndexMultiSelectPage<SysContact>() {
	private val REQ = 98

	override val itemComparator: Comparator<SysContact> = object : Comparator<SysContact> {
		override fun compare(lhs: SysContact, rhs: SysContact): Int {
			return lhs.compareTo(rhs)
		}
	}

	override fun itemTag(item: SysContact): Char {
		return item.tag
	}

	override fun isTagItem(item: SysContact): Boolean {
		return item.byTag
	}

	override fun onFilter(item: SysContact, text: String): Boolean {
		if (item.displayName.contains(text) || item.phone.contains(text)) {
			return true
		}
		if (item.spellString.contains(text) || item.spellShort.contains(text)) {
			return true
		}
		return false
	}

	override fun itemKey(item: SysContact): String {
		return item.phone
	}

	override fun onRequestItems(): List<SysContact> {
		return loadSysContacts()
	}

	var onSelect: (List<SysContact>) -> Unit = {
		for (c in it) {
			log(c.name, c.phone)
		}
	}

	override fun makeTagItem(tag: Char): SysContact {
		return SysContact(tag.toString(), "", true)
	}


	override fun onSelected(selList: List<SysContact>) {
		onSelect(selList)
	}

	override var SELECT_TITLE: String
		get() = Str.CONTACT_SELECT
		set(value) {
		}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar.showBack()
		titleBar.title = Str.CONTACT_SELECT

		setMultiChoiceMode(true)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQ)
		}
		requestItems()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
		if (requestCode == REQ) {
			if (grantResults != null && grantResults.size > 0) {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					requestItems()
				}
			}
		}
	}

	override val viewTypeCount: Int
		get() = 2

	override fun getItemViewType(position: Int): Int {
		val item = getItem(position)
		if (item.byTag) {
			return 1
		}
		return 0
	}

	override fun isItemCheckable(position: Int): Boolean {
		return !getItem(position).byTag
	}

	override fun bindView(position: Int, itemView: View, parent: ViewGroup, item: SysContact) {
		if (itemView is TextView) {
			itemView.text = item.tag.toString()
		} else if (itemView is TextDetailView) {
			itemView.setValues(item.displayName, item.phone)
		}

	}

	override fun newView(context: Context, position: Int, parent: ViewGroup, item: SysContact): View {
		if (item.byTag) {
			return makeTagView()
		} else {
			return TextDetailView(context)
		}
	}


}
package yet.contact

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import yet.theme.Str
import yet.ui.page.FilterIndexMultiSelectPage
import yet.ui.widget.listview.itemview.TextDetailView
import yet.util.log.log
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */

class ContactSelectPage() : FilterIndexMultiSelectPage<SysContact>() {

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


		requestItems()
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

	override fun bindView(position: Int, itemView: View, item: SysContact) {
		if (itemView is TextView) {
			itemView.text = item.tag.toString()
		} else if (itemView is TextDetailView) {
			itemView.setValues(item.displayName, item.phone)
		}

	}

	override fun newView(context: Context, position: Int, item: SysContact): View {
		if (item.byTag) {
			return makeTagView()
		} else {
			return TextDetailView(context)
		}
	}


}
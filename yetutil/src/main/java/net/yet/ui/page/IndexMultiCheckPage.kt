package net.yet.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.theme.Colors
import net.yet.theme.InputSize
import net.yet.ui.ext.*
import net.yet.ui.util.XTextWatcher
import net.yet.ui.widget.ListIndexBar
import java.util.*

@SuppressLint("UseSparseArrays")
abstract class IndexMultiCheckPage<T> : CheckListPage<T>() {
	protected lateinit var indexBar: ListIndexBar<T>

	abstract fun onSelected(selList: List<T>)
	abstract fun onFilter(item: T, text: String): Boolean
	abstract fun makeTagItem(tag: Char): T
	abstract fun itemTag(item: T): Char
	abstract fun isTagItem(item: T): Boolean
	abstract val itemComparator: Comparator<T>
	open var tagComparator: Comparator<Char>? = null
	open protected val indexBarLimit: Int
		get() = 0

	lateinit var searchEdit: EditText

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		searchEdit = context.createEditText()
		contentView.addView(searchEdit, 0, linearParam().widthFill().heightDp(InputSize.EditHeight - 5).margins(10, 2, 10, 2))
		searchEdit.addTextChangedListener(object : XTextWatcher() {
			override fun afterTextChanged(text: String) {
				search(text)
			}
		})
		searchEdit.gone()
		indexBar = object : ListIndexBar<T>(context, listViewParent, listView) {
			override fun isTagItem(item: T): Boolean {
				 return this@IndexMultiCheckPage.isTagItem(item)
			}

			override val tagComparator: Comparator<Char>?
				get() = this@IndexMultiCheckPage.tagComparator

			override val itemComparator: Comparator<T>
				get() = this@IndexMultiCheckPage.itemComparator

			override fun itemTag(item: T): Char {
				return this@IndexMultiCheckPage.itemTag(item)
			}

			override fun makeTagItem(tag: Char): T {
				return this@IndexMultiCheckPage.makeTagItem(tag)
			}
		}
		listViewParent.addView(indexBar, relativeParam().width(NAVBAR_WIDTH).heightFill().parentRight())

		setAutoEnterCheckModelOnLongClick(false)
		setCheckItemPaddingRight(NAVBAR_WIDTH)

		requestItems()


	}

	override fun beforeEnterChoiceMode() {
		searchEdit.visiable()
	}

	override fun beforeLeaveChoiceMode() {
		searchEdit.gone()
	}

	private fun search(s: String) {
		if (s.trim().length > 0) {
			adapter.setFilter {
				onFilter(it, s.trim())
			}
		} else {
			adapter.resetFilter()
		}
	}

	override fun onOrderItems(items: ArrayList<T>): ArrayList<T> {
		return indexBar.processItems(items, indexBarLimit)
	}


	fun done() {
		val ls = checkedItems
		finish()
		onSelected(ls)
	}

	fun makeTagView(): TextView {
		val alphaView = createTextView()
		alphaView.textSizeB().textColorMajor().padding(20, 0, 20, 0).backColor(Colors.color("#eee"))
		listParam().widthFill().height_(20).set(alphaView)
		return alphaView
	}

	companion object {
		const val NAVBAR_WIDTH = 40
	}

}

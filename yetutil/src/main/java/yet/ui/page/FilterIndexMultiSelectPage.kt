package yet.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import net.yet.R
import yet.theme.*
import yet.ui.ext.*
import yet.ui.res.Res
import yet.ui.util.XTextWatcher
import yet.ui.viewcreator.createEditX
import yet.ui.viewcreator.createTextView
import yet.ui.widget.*
import java.util.*

@SuppressLint("UseSparseArrays")
abstract class FilterIndexMultiSelectPage<T> : CheckListPage<T>() {
	var DONE = Str.DONE
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

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		val searchEdit = context.createEditX().singleLine().imeDone{
			it.hideInputMethod()
		}

		contentView.addView(searchEdit, 0, linearParam().widthFill().heightDp(InputSize.EditHeight - 5).margins(10, 2, 10, 2))
		searchEdit.addTextChangedListener(object : XTextWatcher() {
			override fun afterTextChanged(text: String) {
				search(text)
			}
		})
		indexBar = object : ListIndexBar<T>(context, listViewParent, listView) {
			override fun isTagItem(item: T): Boolean {
				return this@FilterIndexMultiSelectPage.isTagItem(item)
			}

			override val tagComparator: Comparator<Char>?
				get() = this@FilterIndexMultiSelectPage.tagComparator

			override val itemComparator: Comparator<T>
				get() = this@FilterIndexMultiSelectPage.itemComparator

			override fun itemTag(item: T): Char {
				return this@FilterIndexMultiSelectPage.itemTag(item)
			}

			override fun makeTagItem(tag: Char): T {
				return this@FilterIndexMultiSelectPage.makeTagItem(tag)
			}
		}
		listViewParent.addView(indexBar, relativeParam().width(NAVBAR_WIDTH).heightFill().parentRight())

		setAutoEnterCheckModelOnLongClick(false)
		setCheckItemPaddingRight(NAVBAR_WIDTH)

		requestItems()
		setMultiChoiceMode(true)

		DONE = Res.str(R.string.yet_done)
		titleBar.addAction(DONE)

	}

	override fun onBackPressed(): Boolean {
		finish()
		return true
	}

	private fun search(s: String) {
		adapter.filter(s)
	}

	override fun onOrderItems(items: List<T>): List<T> {
		return indexBar.processItems(items, indexBarLimit)
	}


	override fun onTitleBarAction(bar: TitleBar, action: Action) {
		if (action.isTag(DONE)) {
			done()
			return
		}
		super.onTitleBarAction(bar, action)
	}

	private fun done() {
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

package net.yet.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface.OnClickListener
import net.yet.R
import net.yet.locale.LibS
import net.yet.util.xlog
import java.util.*

abstract class StringMultiSelectDialogX<T> {
	private val list = ArrayList<T>(128)
	private val map = HashMap<String, Boolean>(128)

	private var midListener: OnClickListener? = null
	private var midText: String? = null
	private var title: String? = null

	fun setTitle(title: String) {
		this.title = title
	}

	fun select(data: Collection<T>) {
		for (s in data) {
			select(s, true)
		}
	}

	fun select(vararg data: T) {
		for (s in data) {
			select(s, true)
		}
	}

	fun select(vararg indexs: Int) {
		for (i in indexs) {
			val s = list[i]
			select(s, true)
		}
	}

	fun select(data: T, sel: Boolean) {
		map.put(getString(data), sel)
	}

	private fun isSelect(data: T): Boolean {
		val b = map[getString(data)]
		return b ?: false
	}

	fun select(index: Int, sel: Boolean) {
		select(list[index], sel)
	}

	fun addItem(s: T) {
		list.add(s)
	}

	fun addItem(s: T, sel: Boolean) {
		list.add(s)
		select(s, sel)
	}

	fun addItems(vararg data: T) {
		this.list.ensureCapacity(data.size + 8)
		for (s in data) {
			this.list.add(s)
		}
	}

	fun addItems(data: Collection<T>) {
		this.list.addAll(data)
	}

	fun setMidButton(text: String, listener: OnClickListener) {
		this.midText = text
		this.midListener = listener
	}

	fun show(context: Context, vararg data: T): AlertDialog {
		addItems(*data)
		return show(context)
	}

	//items 不能为空, 必须有值
	fun show(context: Context): AlertDialog {
		val items = arrayOfNulls<String>(list.size)
		val selectArray = BooleanArray(list.size)

		for (i in list.indices) {
			val s = list[i]
			selectArray[i] = isSelect(s)
			items[i] = getString(s)
		}

		for (i in items.indices) {
			xlog.d("select ", items[i], selectArray[i])
		}

		val builder = AlertDialog.Builder(context)

		builder.setTitle(title)

		builder.setMultiChoiceItems(items, selectArray) { dialog, which, isChecked ->
			val s = list[which]
			select(s, isChecked)
		}
		builder.setNegativeButton(LibS(R.string.cancel), OnClickListener { dlg, arg1 -> dlg.dismiss() })
		builder.setPositiveButton(LibS(R.string.ok), OnClickListener { dlg, arg1 ->
			dlg.dismiss()
			val indexs = ArrayList<Int>(list.size)
			val selectItems = ArrayList<T>(list.size)
			for (i in list.indices) {
				val s = list[i]
				if (isSelect(s)) {
					indexs.add(i)
					selectItems.add(s)
				}
			}
			onOK(indexs, selectItems)
		})

		if (midListener != null) {
			builder.setNeutralButton(midText, midListener)
		}
		val dlg = builder.create()
		dlg.setCancelable(true)
		dlg.setCanceledOnTouchOutside(true)
		dlg.show()
		return dlg
	}

	protected abstract fun onOK(selectedIndex: List<Int>, selectData: List<T>)

	protected abstract fun getString(item: T): String
}

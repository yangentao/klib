package net.yet.ui.dialogs


import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import net.yet.ui.res.Res
import java.util.*

abstract class StringMultiSelectDialog {
	private val list = ArrayList<String>(128)
	private val map = HashMap<String, Boolean>(128)

	fun selectAll() {
		for (s in list) {
			select(s)
		}
	}

	fun select(vararg data: String) {
		for (s in data) {
			map.put(s, true)
		}
	}

	fun select(vararg indexs: Int) {
		for (i in indexs) {
			val s = list[i]
			select(s)
		}
	}

	fun select(targetData: String, sel: Boolean) {
		map.put(targetData, sel)
	}

	fun select(index: Int, sel: Boolean) {
		map.put(list[index], sel)
	}

	fun addItem(s: String) {
		list.add(s)
	}

	fun addItem(s: String, sel: Boolean) {
		list.add(s)
		map.put(s, sel)
	}

	fun addItems(vararg data: String) {
		this.list.ensureCapacity(data.size + 8)
		for (s in data) {
			this.list.add(s)
		}
	}

	fun addItems(data: Collection<String>) {
		this.list.addAll(data)
	}

	fun show(context: Context, vararg data: String) {
		addItems(*data)
		show(context)
	}

	// items 不能为空, 必须有值
	fun show(context: Context): AlertDialog {
		val items = arrayOfNulls<String>(list.size)
		val selectArray = BooleanArray(list.size)

		for (i in list.indices) {
			val s = list[i]
			var b: Boolean? = map[s]
			if (b == null) {
				b = false
			}
			selectArray[i] = b
			items[i] = s
		}
		val builder = AlertDialog.Builder(context)

		builder.setMultiChoiceItems(items, selectArray) { dialog, which, isChecked ->
			val s = list[which]
			map.put(s, isChecked)
		}

		builder.setPositiveButton(Res.str(R.string.ok), DialogInterface.OnClickListener { dlg, arg1 ->
			dlg.dismiss()
			val indexs = ArrayList<Int>(list.size)
			val selectItems = ArrayList<String>(list.size)
			for (i in list.indices) {
				val s = list[i]
				var b: Boolean? = map[s]
				if (b == null) {
					b = false
				}
				if (b!!) {
					indexs.add(i)
					selectItems.add(s)
				}
			}
			onOK(indexs, selectItems)
		})
		builder.setNegativeButton(Res.str(R.string.cancel), DialogInterface.OnClickListener { dlg, arg1 -> dlg.dismiss() })
		val dlg = builder.create()
		dlg.setCancelable(true)
		dlg.setCanceledOnTouchOutside(true)
		dlg.show()
		return dlg
	}

	protected abstract fun onOK(selectedIndex: List<Int>, selectData: List<String>)
}

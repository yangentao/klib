//package net.yet.yetlibdemo
//
//import android.content.Context
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.TextView
//import net.yet.theme.Colors
//import net.yet.ui.ext.*
//import net.yet.util.back
//import net.yet.util.debugMustInMainThread
//import net.yet.util.fore
//import java.util.*
//
///**
// * Created by entaoyang@163.com on 2016-08-09.
// */
//
//
//abstract class TagAdapter<T> : BaseAdapter() {
//
//	internal var items: ArrayList< Any> = ArrayList()
//
//	private var filtedItems: ArrayList<Any> = ArrayList()
//	private var filterBlock: ((T) -> Boolean)? = null
//	val inFilter: Boolean get() = filterBlock != null
//
//
//	abstract fun itemTag(item: T): String
//
//
//	abstract fun bindView(position: Int, itemView: View, parent: ViewGroup, item: T, viewType: Int)
//
//	abstract fun newView(context: Context, position: Int, parent: ViewGroup, viewType: Int): View
//
//	fun isTag(item: Any): Boolean {
//		return item is TagItem
//	}
//
//	/**
//	 * 会自动调用notifyDataSetChanged(), 因此,必须在主线程调用
//
//	 * @param items
//	 */
//	fun setItems(items: List<out Any>) {
//		debugMustInMainThread()
//		this.items.clear()
//		this.items.addAll(items)
//		refilter()
//	}
//
//	fun setItemArray(vararg items: Any) {
//		setItems(listOf(*items))
//	}
//
//	fun setFilter(block: (T) -> Boolean) {
//		debugMustInMainThread()
//		filterBlock = block
//		refilter()
//	}
//
//	fun refilter() {
//		debugMustInMainThread()
//		if (filterBlock != null) {
//			filtedItems.clear()
//			this.items.filterTo(filtedItems, filterBlock!!)
//		}
//		if (inFilter) {
//			val ls = onOrderItems(filtedItems)
//			if (ls !== filtedItems) {
//				filtedItems = ls
//			}
//		} else {
//			val ls = onOrderItems(this.items)
//			if (ls !== this.items) {
//				this.items = ls
//			}
//		}
//		notifyDataSetChanged()
//		onItemsRefreshed()
//	}
//
//	fun resetFilter() {
//		debugMustInMainThread()
//		filterBlock = null
//		refilter()
//	}
//
//	open fun onOrderItems(items: ArrayList<T>): ArrayList<T> {
//		return items
//	}
//
//
//	override fun getCount(): Int {
//		if (inFilter) {
//			return filtedItems.size
//		}
//		return items.size
//	}
//
//	override fun getItem(position: Int): T {
//		if (inFilter) {
//			if (position in filtedItems.indices) {
//				return filtedItems[position]
//			}
//		} else {
//			if (position in items.indices) {
//				return items[position]
//			}
//		}
//		throw  IndexOutOfBoundsException("adapter position out of bound $position " + this.javaClass.name)
//	}
//
//	override fun getItemId(position: Int): Long {
//		return position.toLong()
//	}
//
//	// 异步加载支持
//	protected open fun onRequestItems(): List<T> {
//		return ArrayList()
//	}
//
//	protected open fun onItemsRefreshed() {
//	}
//
//	// 请求数据集, 后台线程回调onRequestItems
//	fun requestItems() {
//		back {
//			val data = onRequestItems()
//			fore {
//				setItems(data)
//			}
//		}
//	}
//
//	fun getReuseId(position: Int):String {
//
//	}
//	override fun getItemViewType(position: Int): Int {
//
//	}
//
//	override fun getViewTypeCount(): Int {
//
//	}
//
//
//	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//		var convertView = convertView
//		val type = getItemViewType(position)
//		if (convertView == null) {
//			convertView = newView(parent.context, position, parent, type)
//		}
//		bindView(position, convertView, parent, getItem(position), type)
//		return convertView
//	}
//
//	open fun makeTagView(context: Context): TextView {
//		val alphaView = context.createTextView()
//		alphaView.textSizeB().textColorMajor().padding(20, 0, 20, 0).backColor(Colors.color("#eee"))
//		listParam().widthFill().height_(20).set(alphaView)
//		return alphaView
//	}
//
//}

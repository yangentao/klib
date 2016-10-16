package net.yet.ui.widget.recycler

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import net.yet.theme.Colors
import net.yet.ui.ext.backColor
import net.yet.util.BackFore
import net.yet.util.TaskUtil
import java.util.*

/**
 * Created by yet on 2015/10/28.
 */
abstract class XListRecyclerView<T>(context: Context) : RecyclerView(context) {
	private val items = ArrayList<T>()
	private var normalColor = Color.WHITE
	private var pressColor = Colors.Fade
	private var enableItemBackColor = true
	private val layoutManager: LinearLayoutManager

	init {
		layoutManager = LinearLayoutManager(context)
		setLayoutManager(layoutManager)
		adapter = XListAdapter()
		setBackgroundColor(Color.WHITE)
		this.addItemDecoration(DividerItemDecoration(context))
	}

	fun setReverse(reverse: Boolean) {
		layoutManager.reverseLayout = reverse
	}

	fun setSelector(enable: Boolean, normalColor: Int, pressColor: Int) {
		this.normalColor = normalColor
		this.pressColor = pressColor
		this.enableItemBackColor = enable
	}

	fun notifyDataSetChanged() {
		adapter.notifyDataSetChanged()
	}

	fun setItems(ls: Collection<T>) {
		this.items.clear()
		this.items.addAll(ls)
		notifyDataSetChanged()
	}

	fun getItem(position: Int): T {
		return items[position]
	}

	val itemCount: Int
		get() = items.size

	fun getItemId(position: Int): Long {
		return position.toLong()
	}

	protected fun getItemViewType(position: Int): Int {
		return 0
	}

	protected abstract fun newView(context: Context, viewType: Int, parent: ViewGroup): View

	protected abstract fun bindView(holder: ListHolder, itemView: View, position: Int, item: T)

	protected abstract fun onItemClick(holder: ListHolder, itemView: View, position: Int, item: T)

	protected fun onItemLongClick(holder: ListHolder, itemView: View, position: Int, item: T): Boolean {
		return false
	}

	inner class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
		var layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

		init {
			itemView.setOnClickListener(this)
			itemView.setOnLongClickListener(this)
		}

		override fun onClick(v: View) {
			val pos = adapterPosition
			onItemClick(this, this.itemView, pos, getItem(pos))
		}

		override fun onLongClick(v: View): Boolean {
			val pos = adapterPosition
			return onItemLongClick(this, this.itemView, pos, getItem(pos))
		}
	}

	private inner class XListAdapter : RecyclerView.Adapter<ListHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
			val view = newView(parent.context, viewType, parent)
			if (enableItemBackColor) {
				view.backColor(normalColor, pressColor)
			}
			return ListHolder(view)
		}

		override fun onBindViewHolder(holder: ListHolder, position: Int) {
			bindView(holder, holder.itemView, position, getItem(position))
			holder.itemView.layoutParams = holder.layoutParams
		}

		override fun getItemCount(): Int {
			return items.size
		}

		override fun getItemViewType(position: Int): Int {
			return this@XListRecyclerView.getItemViewType(position)
		}

		override fun getItemId(position: Int): Long {
			return this@XListRecyclerView.getItemId(position)
		}
	}

	fun requestItems() {
		TaskUtil.backFore(object : BackFore {
			var ls: List<T>? = null

			override fun onFore() {
				setItems(ls ?: emptyList())
			}

			override fun onBack() {
				ls = onRequestItems()
			}
		})
	}

	protected fun onRequestItems(): List<T>? {
		return null
	}
}

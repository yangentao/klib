package yet.ui.widget.recycler

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import yet.theme.Colors
import yet.ui.ext.backColor
import java.util.*

/**
 * Created by yet on 2015/10/28.
 */
abstract class XGridRecyclerView<T>(context: Context, columns: Int) : RecyclerView(context) {
	private val items = ArrayList<T>()
	private var normalColor = Color.WHITE
	private var pressColor = Colors.Fade
	private var enableItemBackColor = true
	private val layoutManager: GridLayoutManager = GridLayoutManager(context, columns)

	init {
		this.setLayoutManager(layoutManager)
		adapter = GridAdapter()
		this.setBackgroundColor(Color.WHITE)
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
		this.adapter.notifyDataSetChanged()
	}

	fun getItem(position: Int): T {
		return items[position]
	}

	fun setItems(items: Collection<T>?) {
		this.items.clear()
		if (items != null) {
			this.items.addAll(items)
		}
		this.notifyDataSetChanged()
	}

	val itemCount: Int
		get() = items.size

	protected fun getItemId(position: Int): Long {
		return position.toLong()
	}

	protected fun getItemViewType(position: Int): Int {
		return 0
	}

	protected abstract fun newView(context: Context, viewType: Int, parent: ViewGroup): View

	protected abstract fun bindView(holder: GridHolder, itemView: View, position: Int, item: T)

	protected abstract fun onItemClick(holder: GridHolder, itemView: View, position: Int, item: T)

	inner class GridHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		var layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

		init {
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			val pos = adapterPosition
			onItemClick(this, this.itemView, pos, getItem(pos))
		}

	}

	private inner class GridAdapter : RecyclerView.Adapter<GridHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridHolder {
			val view = this@XGridRecyclerView.newView(parent.context, viewType, parent)
			if (enableItemBackColor) {
				view.backColor(normalColor, pressColor)
			}
			return GridHolder(view)
		}

		override fun onBindViewHolder(holder: GridHolder, position: Int) {
			this@XGridRecyclerView.bindView(holder, holder.itemView, position, getItem(position))
			holder.itemView.layoutParams = holder.layoutParams
		}

		override fun getItemCount(): Int {
			return items.size
		}

		override fun getItemViewType(position: Int): Int {
			return this@XGridRecyclerView.getItemViewType(position)
		}

		override fun getItemId(position: Int): Long {
			return this@XGridRecyclerView.getItemId(position)
		}
	}
}

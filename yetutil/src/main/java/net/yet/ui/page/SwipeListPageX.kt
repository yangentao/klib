package net.yet.ui.page

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import net.yet.ui.widget.Action
import net.yet.ui.widget.listview.swipe.SwipeHandlerX
import net.yet.ui.widget.listview.swipe.XSwipeItemView
import net.yet.util.xlog

/**
 * 支持多选和侧滑的ListView Fragment

 * @param
 */
abstract class SwipeListPageX<T> : ListPage<T>() {
	private var swipeHandler: SwipeHandlerX? = null

	override fun packNewView(context: Context, view: View, position: Int, parent: ViewGroup): View {
		val v = super.packNewView(context, view, position, parent)
		if (canSwipe() && needSwipe(position)) {
			val swipeView = XSwipeItemView(context, v)
			onAddSwipeAction(swipeView, position, getItem(position))
			swipeView.commit()
			swipeView.onSwipeItemAction = {
				swipeItemView, actionView, action ->
				val pos = listView.getPositionForView(swipeItemView)
				if (pos >= 0) {
					onSwipeAction(swipeItemView, actionView, pos, action)
				}
			}
			return swipeView
		}
		return v
	}

	override fun unpackBindView(position: Int, itemView: View, parent: ViewGroup, item: T): View {
		if (itemView is XSwipeItemView) {
			onUpdateSwipeView(itemView, position, item)
			return super.unpackBindView(position, itemView.itemView, parent, item)
		}
		return super.unpackBindView(position, itemView, parent, item)
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		swipeHandler = SwipeHandlerX(listView)
		swipeHandler!!.enable(canSwipe())
	}

	/**
	 * 在这里添加Action

	 * @param swipeView
	 * *
	 * @param position
	 * *
	 * @param item
	 */
	protected abstract fun onAddSwipeAction(swipeView: XSwipeItemView, position: Int, item: T)

	/**
	 * 在这里更新Action

	 * @param swipeView
	 * *
	 * @param position
	 * *
	 * @param item
	 */
	protected fun onUpdateSwipeView(swipeView: XSwipeItemView, position: Int, item: T) {
	}

	/**
	 * 某个Item是否允许swipe侧滑

	 * @param position
	 * *
	 * @return
	 */
	protected fun needSwipe(position: Int): Boolean {
		return true
	}

	protected fun canSwipe(): Boolean {
		return true
	}

	private fun onSwipeAction(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {
		swipeHandler!!.resetCurrent()
		val countAdapter = adapter.count
		var pos = position
		if (pos >= 0 && pos < listView.headerViewsCount) {
			onSwipeActionHeader(swipeItemView, actionView, pos, action)
		}
		pos -= listView.headerViewsCount
		if (pos >= 0 && pos < countAdapter) {
			onSwipeActionAdapter(swipeItemView, actionView, pos, action)
		}
		pos -= countAdapter
		if (pos >= 0 && pos < listView.footerViewsCount) {
			onSwipeActionFooter(swipeItemView, actionView, pos, action)
		}
	}

	open protected fun onSwipeActionAdapter(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {
		xlog.d(position, action.tag)
	}

	open protected fun onSwipeActionHeader(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {

	}

	open protected fun onSwipeActionFooter(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {

	}
}

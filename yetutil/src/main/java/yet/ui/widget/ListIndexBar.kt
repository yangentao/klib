package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.*
import yet.ui.ext.*
import yet.ui.util.LayerUtil
import yet.ui.util.ShapeUtil
import yet.util.*
import java.util.*

/**
 * Created by yet on 2015/10/29.
 */
abstract class ListIndexBar<T>(context: Context, feedbackParentView: RelativeLayout, private val listView: ListView) : LinearLayout(context) {

	private var selectView: View? = null
	private val selectDrawable = bgDraw()
	private var tagList: ArrayList<Char>? = null
	private val darkColor = Util.argb("#ccc")
	private val normalColor = Color.TRANSPARENT
	private var feedbackView: TextView
	private val tagPosMap = HashMap<Char, Int>(30)

	val hideFeedbackRun:Runnable

	private val touchListener = View.OnTouchListener { v, event ->
		val action = event.actionMasked
		val y = event.y.toInt()
		if (action == MotionEvent.ACTION_DOWN) {
			setBackgroundColor(darkColor)
		} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_OUTSIDE) {
			setBackgroundColor(normalColor)
			selectByY(y)
		} else if (action == MotionEvent.ACTION_MOVE) {
			selectByY(y)
		}
		false
	}

	init {
		orientationVertical().gravityCenterHorizontal().padding(0, 0, 0, 0).makeClickable()
		feedbackView = createTextView()
		this.setOnTouchListener(touchListener)
		hideFeedbackRun = Runnable { feedbackView.gone() }
		val d = ShapeUtil.round(10, Util.argb("#555"), 2, Util.argb("#ddd"))
		feedbackView.textColor_(Color.WHITE).textSize_(50).gravityCenter().backDrawable(d).gone()
		feedbackParentView.addView(feedbackView, relativeParam().centerInParent().size(70))

		listView.setOnScrollListener(object : AbsListView.OnScrollListener {

			override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {

			}

			override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
				if (visibleItemCount > 0) {
					val obj = this@ListIndexBar.listView.adapter.getItem(firstVisibleItem)
					val item = obj as? T ?: return
					if (isTagItem(item)) {

					} else {
						select(itemTag(item))
					}
				}
			}
		})
	}

	open val tagComparator: Comparator<Char>? get() = null
	abstract val itemComparator: Comparator<T>
	abstract fun makeTagItem(tag: Char): T
	abstract fun itemTag(item: T): Char
	abstract fun isTagItem(item: T): Boolean

	fun onIndexBarVisiblityChanged(visiblity: Int) {

	}


	private fun onIndexChanged(newTag: Char, adapterPosition: Int) {
		if (adapterPosition == 0) {
			listView.setSelection(0)
		} else {
			listView.setSelection(adapterPosition + listView.headerViewsCount)
		}
	}

	private fun selectByY(y: Int) {
		for (i in 0..childCount - 1) {
			val itemView = getChildAt(i)
			if (y >= itemView.top && y <= itemView.bottom) {
				if (selectView !== itemView) {
					val tv = itemView as TextView
					val tag = tv.text.toString()[0]
					select(tag, true)
					TaskUtil.fore(Runnable { onIndexChanged(tag, tagPosMap[tag]!!) })
				}
				break
			}
		}
	}

	fun select(tag: Char) {
		fore {
			select(tag, false)
		}
	}

	private fun select(tag: Char, feedback: Boolean) {
		val tagIndex = tagList!!.indexOf(tag)
		if (tagIndex >= 0) {
			if (selectView != null) {
				selectView!!.setBackgroundColor(Color.TRANSPARENT)
			}
			selectView = getChildAt(tagIndex)
			selectView!!.setBackgroundDrawable(selectDrawable)

			val str = (selectView as TextView).text.toString()
			feedbackView.text(str)
			if (feedback) {
				feedbackView.visiable()
				LazyTask.schedule("tag.feedback", 650, hideFeedbackRun)
			}
		}
	}

	/**
	 * @param items
	 * *
	 * @param autoHidenSize 10:表示当结果集小于10个的时候, 自动隐藏;  <=0表示不执行显示隐藏,保持原来的
	 * *
	 * @return
	 */
	fun processItems(itemsOld: List<T>, autoHidenSize: Int): ArrayList<T> {
		val items = itemsOld.filter { !isTagItem(it) }
		val multiMap = MultiHashMapArray<Char, T>(30, 50)
		for (item in items) {
			val tag = itemTag(item)
			multiMap.put(tag, item)
		}
		val tagSet = multiMap.keySet()
		val tagList = ArrayList<Char>(tagSet.size + 1)
		tagList.addAll(tagSet)
		if (tagComparator != null) {
			Collections.sort(tagList, tagComparator)
		} else {
			Collections.sort(tagList)
		}

		val newItems = ArrayList<T>(items.size + tagList.size + 1)
		for (tag in tagList) {
			tagPosMap.put(tag, newItems.size)
			val ls = multiMap.get(tag)//不会出现ls是空的情况!
			Collections.sort(ls, itemComparator)
			val tagItem = makeTagItem(tag)
			newItems.add(tagItem)
			for (item in ls) {
				newItems.add(item)
			}
		}

		fore {
			if (autoHidenSize > 0) {
				visibility = if (items.size >= autoHidenSize) View.VISIBLE else View.GONE
			}
			buildTagViews(tagList)
		}
		return newItems
	}

	private fun buildTagViews(tagList: ArrayList<Char>) {
		this.tagList = tagList
		removeAllViews()
		for (s in tagList) {
			val v = createTextView()
			v.tag = s
			v.text_(s.toString()).textSizeD().textColor(Color.BLACK).gravityCenter()
			addViewParam(v) {
				width(40).height_(0).weight(1).gravityCenter()
			}
		}
	}


	private fun bgDraw(): Drawable {
		val gd = GradientDrawable()
		gd.setColor(Color.GRAY)
		gd.setStroke(2, Color.WHITE)
		gd.setCornerRadius(5f)

		val lu = LayerUtil()
		lu.add(gd, 6, 0, 6, 0)
		lu.add(ColorDrawable(Color.TRANSPARENT))
		return lu.get()
	}

	override fun setVisibility(visibility: Int) {
		super.setVisibility(visibility)
		onIndexBarVisiblityChanged(visibility)
	}

	fun postHide() {
		TaskUtil.fore(Runnable { visibility = View.GONE })
	}

	fun postShow() {
		TaskUtil.fore(Runnable { visibility = View.VISIBLE })
	}
}

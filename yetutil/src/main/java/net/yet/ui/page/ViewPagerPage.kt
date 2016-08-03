package net.yet.ui.page

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import net.yet.theme.Colors
import net.yet.ui.ext.*
import net.yet.ui.widget.AbstractViewPager
import net.yet.ui.widget.SwitchView
import java.util.*

/**
 * Created by yangentao on 16/4/10.
 */
open class ViewPagerPage : TitledPage() {
	protected var viewPager: AbstractViewPager<String>? = null
	protected var switchView: SwitchView = object : SwitchView() {
		override fun onSelectChanged(index: Int, itemName: String) {
			if (viewPager != null) {
				viewPager!!.setCurrentItem(index, true)
			}
		}
	}
	private var items: MutableList<String> = ArrayList()
	private var views: MutableList<View> = ArrayList()

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		switchView.create(context)
		switchView.view.setBackgroundColor(Colors.PageGray)
		switchView.setItems(items)
		contentView.addViewParam(switchView.view) { widthFill().heightDp(45) }

		viewPager = object : AbstractViewPager<String>(context) {

			override fun newView(context: Context, position: Int, item: String): View {
				return views[position]
			}

			override fun onPageSelected(position: Int, item: String) {
				switchView.select(item, false)
			}
		}
		contentView.addView(viewPager, linearParam().widthFill().heightDp(0).weight_(1))
		viewPager!!.setItems(items)
	}

	fun setItems(labels: Array<String>, views: Array<View>) {
		this.views.clear()
		this.items.clear()
		for (s in labels) {
			this.items.add(s)
		}
		for (v in views) {
			this.views.add(v)
		}
		if (viewPager != null) {
			switchView.setItems(items)
			viewPager!!.setItems(items)
		}
	}

	fun setItems(labels: MutableList<String>, views: MutableList<View>) {
		this.items = labels
		this.views = views
		if (viewPager != null) {
			switchView.setItems(items)
			viewPager!!.setItems(items)
		}
	}
}

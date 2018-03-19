package yet.ui.widget.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.GridView
import yet.theme.Colors

/**
 * Created by entaoyang@163.com on 2018-03-19.
 */
open class LineGridView(context: Context) : GridView(context) {

	var enableLine = false
	//px
	var lineWidth = 1
	var lineBottom = true
	var lineColor = Colors.LineGray
	private val localPaint = Paint()

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		if (!enableLine) {
			return
		}
		val cols = numColumns //获取列数
		val total = childCount  //获取Item总数
		if (total <= 0) {
			return
		}
		//计算行数
		val rows = if (total % cols == 0) {
			total / cols
		} else {
			total / cols + 1 //当余数不为0时，要把结果加上1
		}

		localPaint.style = Paint.Style.STROKE //画笔实心
		localPaint.color = lineColor//画笔颜色
		localPaint.strokeWidth = lineWidth.toFloat()

		val view = getChildAt(0)
		val cellWidth = view.width.toFloat()
		val cellHeight = view.height.toFloat()


		val x0: Float = paddingLeft.toFloat()
		val y0: Float = paddingTop.toFloat()
		val x1: Float = (width - paddingRight).toFloat()
		val y1: Float = (height - paddingBottom - 1).toFloat()

		for (r in 0 until rows) {
			val y = y0 + cellHeight * (r + 1) - 1
			if (!lineBottom) {
				if (r == rows - 1) {
					continue
				}
			}
			canvas.drawLine(x0, y, x1, y, localPaint)
		}

		for (c in 0 until cols) {
			if (c == cols - 1) {
				continue
			}
			val x = x0 + cellWidth * (c + 1)
			canvas.drawLine(x, y0, x, y1, localPaint)
		}


	}
}
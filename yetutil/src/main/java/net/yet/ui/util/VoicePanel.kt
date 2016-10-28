package net.yet.ui.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import net.yet.ui.ext.*
import net.yet.util.AmrRecord
import net.yet.util.ToastUtil
import net.yet.util.Util
import net.yet.util.log.xlog
import java.io.File
import java.util.*

/**
 * Created by yangentao on 2015/11/22.
 * entaoyang@163.com
 */
class VoicePanel(val parent: RelativeLayout) : AmrRecord.AMRRecordCallback {
	lateinit var amrRecord: AmrRecord
	lateinit var infoPanel: LinearLayout
	lateinit var waveView: WaveView
	lateinit var textView: TextView
	private var callback: VoicePanelCallback? = null

	val context:Context = parent.context

	var lastFile: File? = null
		private set

	fun init() {
		amrRecord = AmrRecord()

		val bg = ShapeUtil.round(10, Util.argb("#8000"), 1, Util.argb("#ccc"))
		infoPanel = context.createLinearVertical().backDrawable(bg).padding(5).gone()

		waveView = WaveView(context)
		waveView.setColor(Color.WHITE)
		waveView.setMaxValue(MAX)
		infoPanel.addView(waveView, linearParam().widthFill().height(100))

		textView = context.createTextViewC().gravityCenterHorizontal().textColorWhite()
		infoPanel.addView(textView, linearParam().widthFill().heightWrap())
		parent.addView(infoPanel, relativeParam().centerInParent().width(200).heightWrap())
	}

	fun setCallback(callback: VoicePanelCallback) {
		this.callback = callback
	}

	fun setText(text: String) {
		textView.text = text
	}

	fun start() {
		xlog.d("开始录音")
		val file = parent.context.getFileStreamPath("audio_" + System.currentTimeMillis() + ".amr")
		amrRecord.start(file, this)
		infoPanel.visibility = View.VISIBLE
		waveView.clearData()
		setText(TEXT_IN)
	}

	fun end() {
		xlog.d("结束录音")
		amrRecord.stop()
		waveView.clearData()
		infoPanel.visibility = View.GONE
		val duration = amrRecord.duration()
		val file = amrRecord.file
		if (duration < 2000) {
			file.delete()
			ToastUtil.show("小于2秒, 取消发送")
		} else {
			if (file.exists() && callback != null) {
				lastFile = file
				callback!!.onRecordOK(file)
			}
		}
	}

	fun cancel() {
		xlog.d("取消录音")
		amrRecord.stop()
		val file = amrRecord.file
		val b = file.delete()
		xlog.dIf(!b, "删除文件失败")
		waveView.clearData()
		infoPanel.visibility = View.GONE
	}

	override fun onAmp(amps: ArrayList<Int>) {
		waveView.setValue(amps)
	}

	interface VoicePanelCallback {
		fun onRecordOK(file: File)
	}

	companion object {
		val TEXT_IN = "手指上滑, 取消发送"
		val TEXT_OUT = "松开取消"
		private val MAX = 30000
	}
}

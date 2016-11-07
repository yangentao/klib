package net.yet.ui.page

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.SparseArray
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import net.yet.file.SdAppFile
import net.yet.theme.Str
import net.yet.ui.activities.AnimConf
import net.yet.ui.activities.PageUtil
import net.yet.ui.activities.TabBarContainerActivity
import net.yet.ui.dialogs.HorProgressDlg
import net.yet.ui.dialogs.OKDialog
import net.yet.ui.dialogs.SpinProgressDlg
import net.yet.ui.dialogs.StringSelectDialog
import net.yet.ui.widget.TabBar
import net.yet.util.*
import net.yet.util.app.App
import net.yet.util.database.Values
import net.yet.util.event.EventMerge
import java.io.File
import java.util.*

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


/**
 * 不要调用getActivity().finish(). 要调用finish(), finish处理了动画
 * fragment基类 公用方法在此处理
 */
open class BaseFragment : Fragment(), MsgListener {

	val args = Values()
	private val resultListeners = SparseArray<PreferenceManager.OnActivityResultListener>(8)
	private val eventMerges = ArrayList<EventMerge>()

	lateinit var spinProgressDlg: SpinProgressDlg
	lateinit var horProgressDlg: HorProgressDlg

	var openFlag: Int = 0

	val activityAnim: AnimConf?
		get() {
			if (tabBar == null) {
				return App.animConfDefault
			}
			return null

		}

	fun singleTop() {
		openFlag = openFlag or Intent.FLAG_ACTIVITY_SINGLE_TOP
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
		spinProgressDlg = SpinProgressDlg(activity)
		horProgressDlg = HorProgressDlg(activity)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
	}

	fun statusBarColor(color: Int) {
		val w = activity?.window ?: return
		if (Build.VERSION.SDK_INT >= 21) {
			w.statusBarColor = color
		}
	}

	fun alert(title: String, msg: String) {
		val dlg = OKDialog()
		dlg.show(activity, title, msg)
	}

	fun alert(title: String) {
		val dlg = OKDialog()
		dlg.show(activity, title, null)
	}

	fun itemSelectN(items: Collection<String>, block: (Int) -> Unit) {
		var dlg = StringSelectDialog()
		dlg.onSelect = { n, s ->
			block(n)
		}
		dlg.addItems(items)
		dlg.show(activity)
	}

	fun itemSelect(items: Collection<String>, block: (String) -> Unit) {
		val dlg = StringSelectDialog()
		dlg.onSelect = { n, s ->
			block(s)
		}
		dlg.addItems(items)
		dlg.show(activity)
	}

	fun itemSelect(vararg items: String, block: (String) -> Unit) {
		val dlg = StringSelectDialog()
		dlg.onSelect = { n, s ->
			block(s)
		}
		dlg.addItems(*items)
		dlg.show(activity)
	}

	fun showMenuSelect(vararg items: String, block: (String) -> Unit) {
		val dlg = StringSelectDialog()
		dlg.onSelect = { n, s ->
			block(s)
		}
		dlg.addItems(*items)
		dlg.show(activity)
	}

	fun openPage(page: BaseFragment) {
		PageUtil.open(activity, page)
	}


	override fun onResume() {
		super.onResume()
		if (!isHidden) {
			onShow()
		}
	}

	override fun onPause() {
		if (!isHidden) {
			onHide()
		}
		super.onPause()
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (hidden) {
			if (isResumed) {
				onHide()
			}
		} else {
			if (isResumed) {
				onShow()
			}
		}
	}

	open fun onShow() {

	}

	open fun onHide() {

	}

	/**
	 * 可见, 并且没锁屏

	 * @return
	 */
	val isVisiableToUser: Boolean
		get() = this.isResumed && isVisible && !Util.getKeyguardManager().inKeyguardRestrictedInputMode()

	fun takeViedo(sizeM: Int, block: (Uri) -> Unit) {
		val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeM * 1024 * 1024)
		val onResult = PreferenceManager.OnActivityResultListener { requestCode, resultCode, data ->
			if (resultCode == Activity.RESULT_OK) {
				if (data != null && data.data != null) {
					block.invoke(data.data)
				}
			}
			true
		}
		startActivityForResult(TAKE_VIDEO, intent, onResult)
	}

	fun pickVideo(block: (Uri) -> Unit) {
		val i = Intent(Intent.ACTION_PICK)
		i.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")
		val onResult = PreferenceManager.OnActivityResultListener { requestCode, resultCode, data ->
			if (resultCode == Activity.RESULT_OK) {
				if (data != null && data.data != null) {
					block.invoke(data.data)
				}
			}
			true
		}
		startActivityForResult(PICK_PHOTO, i, onResult)
	}

	fun pickPhoto(block: (Uri) -> Unit) {
		val i = Intent(Intent.ACTION_PICK)
		i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
		val onResult = PreferenceManager.OnActivityResultListener { requestCode, resultCode, data ->
			if (resultCode == Activity.RESULT_OK) {
				if (data != null && data.data != null) {
					block.invoke(data.data)
				}
			}
			true
		}
		startActivityForResult(PICK_PHOTO, i, onResult)
	}

	fun takePhotoPng(block: (File) -> Unit) {
		takePhoto("PNG", block)
	}

	fun takePhotoJpg(block: (File) -> Unit) {
		takePhoto("JPEG", block)
	}

	fun takePhoto(format: String, block: (File) -> Unit) {
		val outputFile = SdAppFile.temp("" + System.currentTimeMillis() + "." + format)
		val intent = Intent("android.media.action.IMAGE_CAPTURE")
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
		intent.putExtra("outputFormat", format)
		val onResult = PreferenceManager.OnActivityResultListener { requestCode, resultCode, data ->
			if (resultCode == Activity.RESULT_OK && outputFile.exists()) {
				block(outputFile)
			}
			true
		}
		startActivityForResult(TAKE_PHOTO, intent, onResult)
	}


	fun cropPhoto(uri: Uri, outX: Int, outY: Int, result: (Bitmap?) -> Unit) {
		val intent = Intent("com.android.camera.action.CROP")
		intent.setDataAndType(uri, "image/*")
		intent.putExtra("crop", "true")
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1)
		intent.putExtra("aspectY", 1)
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outX)
		intent.putExtra("outputY", outY)
		intent.putExtra("return-data", true)
		// intent.putExtra("output",CAMERA_EXTRA_OUTPUT_FILE);
		val onResult = PreferenceManager.OnActivityResultListener { requestCode, resultCode, data ->
			if (resultCode == Activity.RESULT_OK) {
				val extras = data.extras
				var photo: Bitmap? = null
				if (extras != null) {
					photo = extras.getParcelable<Bitmap>("data")
				}
				result.invoke(photo)
			} else {
				result.invoke(null)
			}
			true
		}
		startActivityForResult(CROP_PHOTO, intent, onResult)
	}

	fun startActivityForResult(requestCode: Int, intent: Intent, onResult: PreferenceManager.OnActivityResultListener) {
		resultListeners.put(requestCode, onResult)
		startActivityForResult(intent, requestCode)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		val rl = resultListeners.get(requestCode)
		if (rl != null) {
			resultListeners.remove(requestCode)
			rl.onActivityResult(requestCode, resultCode, data)
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	open fun onBackPressed(): Boolean {
		return false
	}

	fun finish() {
		val context = activity
		context.finish()
	}

	val tabBar: TabBar?
		get() {
			if (activity is TabBarContainerActivity) {
				return (activity as TabBarContainerActivity).tabBar
			}
			return null
		}

	fun toastIf(condition: Boolean, trueString: String, falseString: String) {
		if (condition) {
			toast(trueString)
		} else {
			toast(falseString)
		}
	}

	fun toastIf(condition: Boolean, trueString: String) {
		if (condition) {
			toast(trueString)
		}
	}

	fun toast(vararg texts: Any) {
		val s = StrBuilder.build(*texts)
		fore {
			if (activity != null) {
				Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
			} else {
				Toast.makeText(App.get(), s, Toast.LENGTH_LONG).show()
			}
		}
	}

	fun toastSuccessFailed(b: Boolean) {
		toast(if (b) Str.OP_SUCCESS else Str.OP_FAILED)
	}

	fun toastShort(text: String) {
		fore {
			if (activity != null) {
				Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(App.get(), text, Toast.LENGTH_SHORT).show()
			}
		}
	}

	fun softInputAdjustResize() {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
	}

	fun hideInputMethod() {
		val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		if (imm.isActive() && activity.getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()!!.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
		}
	}

	fun showInputMethod() {
		val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		// 显示或者隐藏输入法
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
	}

	open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		return false
	}

	protected fun createMerge(millSec: Int): EventMerge {
		val em = EventMerge.delay(millSec)
		eventMerges.add(em)
		return em
	}


	fun pickDate(initDate: Long, block: (Long) -> Unit) {
		pickDate(MyDate(initDate), block)
	}

	fun pickDate(date: MyDate, block: (Long) -> Unit) {
		val dlg = DatePickerDialog(activity, object : DatePickerDialog.OnDateSetListener {
			override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
				block(MyDate.makeDate(year, monthOfYear, dayOfMonth))
			}

		}, date.year, date.month, date.day)
		dlg.show()
	}

	fun pickTime(time: Long, block: (Long) -> Unit) {
		pickTime(MyDate(time), block)
	}

	fun pickTime(time: MyDate, block: (Long) -> Unit) {
		val dlg = TimePickerDialog(activity, object : TimePickerDialog.OnTimeSetListener {
			override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
				block(MyDate.makeTime(hourOfDay, minute))
			}

		}, time.hour, time.minute, true)
		dlg.show()
	}


	override fun onDestroy() {
		MsgCenter.remove(this)
		super.onDestroy()
		for (m in eventMerges) {
			m.clear()
		}
		eventMerges.clear()
	}

	protected fun listenMsg(vararg msgs: String) {
		MsgCenter.listen(this, *msgs)
	}

	protected fun listenMsg(vararg clses: Class<*>) {
		MsgCenter.listen(this, *clses)
	}

	override fun onMsg(msg: Msg) {
	}

	companion object {
		private val TAKE_PHOTO = 988
		private val TAKE_VIDEO = 989
		private val PICK_PHOTO = 977
		private val CROP_PHOTO = 966
	}

}

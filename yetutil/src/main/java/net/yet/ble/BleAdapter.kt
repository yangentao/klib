package net.yet.ble

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import net.yet.util.app.App
import net.yet.util.app.OS
import net.yet.util.app.hasPerms
import net.yet.util.foreDelay
import net.yet.util.log.log
import net.yet.util.toast

/**
 * Created by entaoyang@163.com on 2016-10-17.
 */
object BleAdapter {
	val DELAY: Long = 100
	val REQ_CODE = 65

	val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
	var blueMgr: BluetoothManager? = App.get().getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

	val isEnabled: Boolean get() = adapter?.isEnabled ?: false

	var time: Long = 0

	val blePermSet = setOf(
			android.Manifest.permission.ACCESS_COARSE_LOCATION,
			android.Manifest.permission.BLUETOOTH,
			android.Manifest.permission.BLUETOOTH_ADMIN
	)

	fun checkBle(context: Activity): Boolean {
		if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			toast(BleStr.NotSupport)
			return false
		}
		if (adapter == null) {
			toast(BleStr.NotSupport)
			return false
		}
		return true
	}

	fun checkPerms(context: Activity): Boolean {
		return hasPerms(blePermSet)
	}

	fun enable(context: Activity) {
		if (adapter == null) {
			return
		} else {
			if (!adapter.isEnabled) {
				adapter.enable()
			}
			if (!adapter.isEnabled) {
				val it = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
				context.startActivityForResult(it, REQ_CODE)
			}
		}
	}

	fun disable() {
		adapter?.disable()
	}


	val isStateOn: Boolean get() = BluetoothAdapter.STATE_ON == adapter?.state
	val isStateTurningOn: Boolean get() = BluetoothAdapter.STATE_TURNING_ON == adapter?.state
	val isStateOff: Boolean get() = BluetoothAdapter.STATE_OFF == adapter?.state
	val isStateTurningOff: Boolean get() = BluetoothAdapter.STATE_TURNING_OFF == adapter?.state


	fun checkExample() {
		if (OS.GE60) {
			if (!hasPerms(blePermSet)) {
				toast(BleStr.NeedPerms)
				return
			}
		}
		if (!isEnabled) {
			toast(BleStr.NotOpened)
			return
		}
		if (isStateOff || isStateTurningOff) {
			toast("蓝牙已关闭")
			return
		}
		val ad = adapter ?: return
		if (isStateOn) {
			log("蓝牙已经开启")
			log("开始扫描")
//TODO 连接
			return
		}
		if (isStateTurningOn) {
			log("蓝牙正在开启")
			foreDelay(DELAY) {
				//TODO 连接
			}
			return
		}
		return
	}

}

package net.yet.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import net.yet.util.fore
import net.yet.util.foreDelay
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-31.
 * timeout: 小于等于0, 一直扫描直到onFind返回true时停止扫描;   大于0:延时timeout毫秒后自动关闭扫描
 * timeDelta:  //timeDelta毫秒内不重复报告,  负数或0总是报告
 */

class BleScanner(val adapter: BluetoothAdapter, val timeout: Int, val timeDelta: Int = 2000) : BluetoothAdapter.LeScanCallback {
	//是否要处理当前扫描结果, 返回true会回调onFind, 返回false继续扫描下一个
	var accepter: (BroadData) -> Boolean = { true }

	//返回TRUE, 停止扫描;  false:继续扫描
	var onFind: (BroadData) -> Boolean = { true }

	var onTimeout: () -> Unit = {}

	var started: Boolean = false
		private set

	var startTime: Long = System.currentTimeMillis()
		private set

	private var map = HashMap<String, Long>()

	init {

	}

	fun start() {
		started = true
		startTime = System.currentTimeMillis()
		map.clear()
		if (timeout > 0) {
			val preTime = startTime
			foreDelay(timeout.toLong()) {
				if (!started) {
					if (preTime == startTime) {
						stop()
						onTimeout()
					}
				}
			}
		}
		adapter.startLeScan(this)
	}

	fun stop() {
		started = false
		map.clear()
		adapter.stopLeScan(this)
	}

	private fun needReportByTime(address: String): Boolean {
		if (timeDelta <= 0) {
			return true
		}
		val preTime = map[address] ?: 0L
		val cur = System.currentTimeMillis()
		map[address] = cur
		return cur - preTime > timeDelta
	}

	override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
		if (device != null && scanRecord != null) {
			val item = BroadData(device, rssi, scanRecord)
			val a = needReportByTime(device.address) && accepter(item)
			if (!a) {
				return
			}

			val b = onFind(item)
			if (b) {
				fore {
					stop()
				}
			}
		}
	}
}
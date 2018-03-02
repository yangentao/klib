package yet.ble

import android.bluetooth.BluetoothDevice

/**
 * Created by entaoyang@163.com on 2016-10-24.
 */
//
class BroadData(val device: BluetoothDevice, val rssi: Int, val data: ByteArray) {

	val mac: String get() = device.address

	override fun toString(): String {
		return "BraodData mac:$mac rssi:$rssi"
	}

}
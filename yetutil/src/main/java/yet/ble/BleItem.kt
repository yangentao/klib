package yet.ble

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import yet.util.Hex
import yet.util.log.log

/**
 * Created by entaoyang@163.com on 16/6/17.
 */

class BleItem(val data: ByteArray) {
	var ch: BluetoothGattCharacteristic? = null
	var desc: BluetoothGattDescriptor? = null

	var onResult: (Boolean) -> Unit = { ok ->

	}

	constructor(ch: BluetoothGattCharacteristic, data: ByteArray) : this(data) {
		this.ch = ch
	}

	constructor(desc: BluetoothGattDescriptor, data: ByteArray) : this(data) {
		this.desc = desc
	}

	fun write(gatt: BluetoothGatt?) {
		if (gatt != null) {
			log("写数据:", Hex.encode(data))
			if (ch != null) {
				ch?.value = data
				gatt.writeCharacteristic(ch!!)
			} else if (desc != null) {
				desc?.value = data
				gatt.writeDescriptor(desc!!)
			}
		}
	}
}
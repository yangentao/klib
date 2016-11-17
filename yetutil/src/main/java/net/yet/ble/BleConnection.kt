package net.yet.ble

import android.bluetooth.*
import net.yet.crypt.AES
import net.yet.util.*
import net.yet.util.app.App
import net.yet.util.app.Lang
import net.yet.util.log.log
import net.yet.util.log.logd
import net.yet.util.log.loge
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-11-17.
 */

class BleConnection(val mac: String) : BluetoothGattCallback() {
	val PKG_LEN = 20
	val DELAY: Long = 100
	val UUID_CC: UUID = UUID.fromString("f000ccc0-0451-4000-b000-000000000000")
	val UUID_CLIENT_CONFIG: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
	val UUID_OAD: UUID = UUID.fromString("f000ffc0-0451-4000-b000-000000000000")
	val UUID_SERVICE: UUID = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb")

	val adapter: BluetoothAdapter? = BleAdapter.adapter
	var gatt: BluetoothGatt? = null

	var writeCh: BluetoothGattCharacteristic? = null
	var readCh: BluetoothGattCharacteristic? = null


	//mac
	var onConnectedCallback: (String) -> Unit = {}
	//mac
	var onCloseCallback: (String) -> Unit = {}
	var onDiscoverOK: (BleConnection) -> Unit = {}

	var onReadCallback: (data: ByteArray) -> Unit = {}

	val dataQueue = SyncQueue<BleItem>()


	var closed: Boolean = false

	var busy: Boolean = false
		set(value) {
			field = value
			if (!field) {
				fore {
					writeNext()
				}
			}
		}


	fun connect(): Boolean {
		if (adapter == null) {
			return false
		}
		if (!TaskUtil.inMainThread()) {
			debugThrow("必须在主线程中调用connect方法")
		}
		val bd = adapter?.getRemoteDevice(mac) ?: return false
		gatt = bd.connectGatt(App.getContext(), false, this)
		foreDelay(DELAY) {
			val g = gatt
			if (g != null) {
				val ok = g.connect()
				log("连接结果", ok)
				if (!ok) {
					close()
				}
			}
		}
		return gatt != null
	}


	fun getCCService(): BluetoothGattService? {
		return gatt?.getService(UUID_CC)
	}

	fun getOADService(): BluetoothGattService? {
		return gatt?.getService(UUID_OAD)
	}

	fun getService(): BluetoothGattService? {
		return gatt?.getService(UUID_SERVICE)
	}

	private fun writeNext() {
		synchronized(this) {
			if (busy) {
				return
			}
			val p = dataQueue.peek() ?: return
			busy = true
			foreDelay(DELAY) {
				p.write(gatt)
			}
			return

		}
	}

	fun push(item: BleItem) {
		dataQueue.push(item)
		writeNext()
	}


	fun push(c: BluetoothGattCharacteristic, data: ByteArray) {
		dataQueue.push(BleItem(c, data))
		writeNext()
	}

	fun push(desc: BluetoothGattDescriptor, data: ByteArray) {
		dataQueue.push(BleItem(desc, data))
		writeNext()
	}

	//protocol
	fun writeData(bs: ByteArray) {
		var arr = ByteArray(PKG_LEN) {
			n ->
			0.toByte()
		}
		for (i: Int in bs.indices) {
			if (i < PKG_LEN) {
				arr[i] = bs[i]
			}
		}
		if (writeCh != null) {
			push(writeCh!!, arr)
		}

	}


	fun toast(msg: String) {
		ToastUtil.show(msg)
	}

	//连接成功
	fun onConnected() {
		onConnectedCallback(mac)
	}

	fun isConnected(): Boolean {
		val remote = gatt?.device
		if (remote != null) {
			val st = BleAdapter.blueMgr?.getConnectionState(remote, BluetoothProfile.GATT)
			return st == BluetoothProfile.STATE_CONNECTED
		}
		return false
	}


	fun close() {
		dataQueue.clear()
		gatt?.disconnect()
		foreDelay(100) {
			gatt?.close()
		}
		onCloseCallback(mac)
	}


	private fun getNotifyValue(ch: BluetoothGattCharacteristic): ByteArray? {
		return if (ch.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE > 0) {
			BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
		} else if (ch.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
			BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
		} else {
			null
		}
	}

	//一些 手机状态处理有问题(旧状态和新状态一样), 需要自己管理状态
	override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
		logd(mac, "连接状态 status:", status, " Connection State:", newState)
		if (newState == BluetoothProfile.STATE_DISCONNECTED || newState == BluetoothProfile.STATE_DISCONNECTING
		) {
			fore {
				close()
			}
			return
		}
		if (status == BluetoothGatt.GATT_SUCCESS) {
			if (newState == BluetoothProfile.STATE_CONNECTING) {
				return
			}
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				onConnected()
				log(mac, "调用发现服务")
				foreDelay(DELAY * 2) {
					if (!gatt.discoverServices()) {
						logd(mac, "发现服务失败")
						fore {
							close()
						}
					}
				}
				return
			}
			return
		}
		when (status) {
			BluetoothGatt.GATT_FAILURE -> {
				logd(mac, "GATT_FAILURE")
			}
			BluetoothGatt.GATT_READ_NOT_PERMITTED -> {
				logd(mac, "GATT_READ_NOT_PERMITTED")
			}
			BluetoothGatt.GATT_WRITE_NOT_PERMITTED -> {
				logd(mac, "GATT_WRITE_NOT_PERMITTED")
			}
			BluetoothGatt.GATT_INSUFFICIENT_AUTHENTICATION -> {
				logd(mac, "GATT_INSUFFICIENT_AUTHENTICATION")
			}
			BluetoothGatt.GATT_REQUEST_NOT_SUPPORTED -> {
				logd(mac, "GATT_REQUEST_NOT_SUPPORTED")
			}
			BluetoothGatt.GATT_INSUFFICIENT_ENCRYPTION -> {
				logd(mac, "GATT_INSUFFICIENT_ENCRYPTION")
			}
			BluetoothGatt.GATT_INVALID_OFFSET -> {
				logd(mac, "GATT_INVALID_OFFSET")
			}
			BluetoothGatt.GATT_INVALID_ATTRIBUTE_LENGTH -> {
				logd(mac, "GATT_INVALID_ATTRIBUTE_LENGTH")
			}
			BluetoothGatt.GATT_CONNECTION_CONGESTED -> {
				val s = if (Lang.zhCN) {
					"连接的设备太多"
				} else if (Lang.zhOther) {
					"連接的設備太多"
				} else {
					"Too much lock connected"
				}
				toast(s)
				return
			}
			else -> {
				logd(mac, "未知错误", status)
			}
		}

		fore {
			close()
		}

	}

	override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
		val OK = status == BluetoothGatt.GATT_SUCCESS
		logd(mac, "发现服务, onServicesDiscovered:", OK)
		if (!OK) {
			fore {
				close()
			}
			return
		}
		val svr = getService()
		val ls = svr?.characteristics ?: emptyList()
		val readProp = BluetoothGattCharacteristic.PROPERTY_READ
		val writeProp = BluetoothGattCharacteristic.PROPERTY_WRITE
		readCh = null
		writeCh = null
		for (ch in ls) {
			val p = ch.properties
			if (readCh == null) {
				if ((p and readProp) != 0) {
					readCh = ch
				}
			}
			if (writeCh == null) {
				if ((p and writeProp) != 0) {
					writeCh = ch
				}
			}
		}

		if (writeCh == null || readCh == null) {
			log(mac, "没有找到读或写得characteristic")
			fore {
				close()
			}
			return
		}
		log(mac, "设置读写通知")
		var ok = gatt.setCharacteristicNotification(writeCh!!, true) && gatt.setCharacteristicNotification(readCh!!, true)
		if (!ok) {
			log(mac, "设置")
			fore {
				close()
			}
			return
		}
		val descriptor = readCh!!.getDescriptor(UUID_CLIENT_CONFIG) ?: return
		val data: ByteArray? = getNotifyValue(readCh!!)
		if (data == null) {
			log(mac, "读取通知设置失败")
			fore {
				close()
			}
			return
		}
		val item = BleItem(descriptor, data)
		item.onResult = { ok ->
			log(mac, "设置读通知结果", ok)
			if (ok) {
				onDiscoverOK(this@BleConnection)
			} else {
				log(mac, "设置读监听失败")
				fore {
					close()
				}
			}
		}
		push(item)
	}

	override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {

	}

	override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
		val ok = status == BluetoothGatt.GATT_SUCCESS
		val value = characteristic.value
		val s = Hex.encode(value)
		logd(mac, "onCharacteristicWrite:", ok, s)
		val item = dataQueue.pop()
		busy = false
		item?.onResult?.invoke(ok)

	}

	override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
		val s = Hex.encode(characteristic.value)
		logd(mac, "onCharacteristicChanged:", s, this, "   UUID:", characteristic.uuid)
		onReadCallback(characteristic.value)
	}

	override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {

	}

	override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
		val ok = status == BluetoothGatt.GATT_SUCCESS
		logd(mac, "onDescriptorWrite: ", ok)
		val item = dataQueue.pop()
		busy = false
		item?.onResult?.invoke(ok)
	}
}
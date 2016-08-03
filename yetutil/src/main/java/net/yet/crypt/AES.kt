package net.yet.crypt

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by entaoyang@163.com on 16/4/27.
 */
object AES {

	fun encode(key: ByteArray, data: ByteArray): ByteArray {
		return doAES(Cipher.ENCRYPT_MODE, key, data)
	}


	fun decode(key: ByteArray, data: ByteArray): ByteArray {
		return doAES(Cipher.DECRYPT_MODE, key, data)
	}

	private fun doAES(mode: Int, key: ByteArray, data: ByteArray): ByteArray {
		val cipher = Cipher.getInstance("AES/ECB/NoPadding")
		cipher.init(mode, SecretKeySpec(key, "AES"))
		return cipher.doFinal(data)
	}

}

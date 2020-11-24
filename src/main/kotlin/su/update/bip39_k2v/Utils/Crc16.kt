package su.update.bip39_k2v.Utils

import kotlin.experimental.and

class Crc16 {

    fun calculate(bytes: ByteArray): Int {
        var i: Int
        var crc_value = 0
        for (len in bytes.indices) {
            i = 0x80
            while (i != 0) {
                crc_value = if (crc_value and 0x8000 != 0) {
                    crc_value shl 1 xor 0x8005
                } else {
                    crc_value shl 1
                }
                if ((bytes[len] and i.toByte()) != (0).toByte()) {
                    crc_value = crc_value xor 0x8005
                }
                i = i shr 1
            }
        }
        return crc_value
    }
    fun get(text: String): Int {
        return arc(text.toByteArray())
    }

    fun arc(bytes: ByteArray): Int {
        var crc = 0x0000
//        val len = bytes.count()-1
        bytes.forEach {
            val curr = it and 0xFF.toByte()
//            val curr = it
            crc = crc xor curr.toInt()
            (0..7).forEach { i ->
                if (crc and 1 !=0)
                    crc = (crc shr 1) xor 0xA001; // 0xA001 is the reflection of 0x8005
                else
                    crc = crc shr 1
            }
        }
        return crc
    }

/*
int calculate_crc(byte[] bytes) {
    int i;
    int crc_value = 0;
    for (int len = 0; len < bytes.length; len++) {
        for (i = 0x80; i != 0; i >>= 1) {
            if ((crc_value & 0x8000) != 0) {
                crc_value = (crc_value << 1) ^ 0x8005;
            } else {
                crc_value = crc_value << 1;
            }
            if ((bytes[len] & i) != 0) {
                crc_value ^= 0x8005;
            }
        }
    }
    return crc_value;
}
*/

}
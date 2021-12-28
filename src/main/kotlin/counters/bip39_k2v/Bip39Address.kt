package counters.bip39_k2v

import counters.bip39_k2v.Enum.TypeBip39Address
import counters.bip39_k2v.Model.ObjBip39Address
import counters.bip39_k2v.Utils.Crc16
import counters.bip39_k2v.Utils.HelpHex
import kotlin.random.Random


class Bip39Address {

    private val helpHex = HelpHex();

    private val crc = Crc16()

    private val delimiter = "01"
    private val delimiter2 = "03"

    fun encode(type: TypeBip39Address, topic: String, payload: String): String? {
        return encode(type, topic, payload, null)
    }

    fun encode(type: TypeBip39Address, topic: String, payload: Long): String? {
        return encode(type, topic, null, payload)
    }

    private fun encode(type: TypeBip39Address, topic: String, payload: String?, payloadInt: Long?): String? {
        var address = ""

        address += helpHex.fromNum(type.int)

        val payloadHex =
                if (type == TypeBip39Address.ASCII && payload != null) helpHex.getASCII(payload)
                else if (type == TypeBip39Address.INTEGER && payloadInt != null) helpHex.fromNum(payloadInt)
                else return null

        address += payloadHex

        if (topic !== "") address += delimiter + helpHex.getASCII(topic)
        else address += delimiter2

        if (address.length > 36) {
            return null
        } else if (address.length < 36) {
            address += delimiter
            while (address.length < 36) {
                address += helpHex.fromNum(Random.nextInt(33, 93))
            }
            /*   for (i in address.length..36) {
                   address += helpHex.fromNum(Random.nextInt(33, 93))
               }*/

        }
        val crc = crc.get(address)
//        println("$address $crc ${crc.toString(16)}")
        address += helpHex.fromNum(crc)
//        val crc16 = CRC32() //CRC16()
//        crc16.
        return address
    }

    private fun numDelimiters(address: String, findStr: String): Int {
        val pattern = findStr.toRegex()
        val matches = pattern.findAll(address)
        return matches.count()
    }


    fun decode(address: String): ObjBip39Address? {
        if (address.length != 40) return null;
        var topic: String? = ""
        var topicNull = false
        var payload = ""
//        var payloadArr: ByteArray = byteArrayOf()
        var random = ""


        val numDelimiter = numDelimiters(address, delimiter)
        val numDelimiter2 = numDelimiters(address, delimiter2)

        val type = Integer.parseInt(address.substring(0, 2), 16)
        var findDelimiterNew = 0

        if (numDelimiter == 0) {
            findDelimiterNew = 2
        } else if (numDelimiter == 1) findDelimiterNew = 1

        for (i in 34 downTo 2 step 2) {
            val strHex = address.substring(i, i + 2)

            if (findDelimiterNew < 2 && strHex == delimiter) {
                findDelimiterNew = findDelimiterNew.plus(1)
                continue
            } else if ((findDelimiterNew < 2 || numDelimiter2 > 0) && topicNull == false && strHex == delimiter2) {
//                    val strHexPrev = address.substring(i+2, i+4 )
//                    println("$i strHex=$strHex,strHexPrev=$strHexPrev")
                topicNull = true
                findDelimiterNew = findDelimiterNew.plus(1)
                continue
            }
            if (findDelimiterNew == 0) {
                random = strHex + random
            } else if (findDelimiterNew == 1) {
                topic = Integer.parseInt(strHex, 16).toChar().toString() + topic

            } else {
                payload = strHex + payload

            }
        }

        val _crc16 = address.substring(36, 40)
        val currCrc16 = helpHex.fromNum(crc.get(address.substring(0, 36)))

//        println("$_crc16 $currCrc16 ${address.substring(0, 36)}      ")
        val enumType = TypeBip39Address.values().lastOrNull { it.int == type }

        if (_crc16 == currCrc16 && enumType != null) {
            if (topicNull) {
                topic = null
            }
            return ObjBip39Address(enumType, topic, payload)
        }
        return null;
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "Error Integer type")
    fun decode_old(address: String): ObjBip39Address? {
        if (address.length != 40) return null;
        var topic: String? = ""
        var topicNull = false
        var payload = ""
//        var payloadArr: ByteArray = byteArrayOf()
        var random = ""

        val type = Integer.parseInt(address.substring(0, 2), 16)
        var findDelimiter = 0

        for (i in 2..34 step 2) {
            val strHex = address.substring(i, i + 2)
            if (strHex == delimiter) {
                findDelimiter = findDelimiter.plus(1)
                continue
            } else if (strHex == delimiter2) {
                topicNull = true
                findDelimiter = findDelimiter.plus(1)
                continue
            }
            if (findDelimiter == 0) {
//                payloadArr += (Integer.parseInt(strHex, 16).toByte())
                payload += strHex
            } else if (findDelimiter == 1) topic += Integer.parseInt(strHex, 16).toChar()
            else random += strHex
        }

        val _crc16 = address.substring(36, 40)
        val currCrc16 = helpHex.fromNum(crc.get(address.substring(0, 36)))

//        println("$_crc16 $currCrc16 ${address.substring(0, 36)}      ")
        val enumType = TypeBip39Address.values().lastOrNull { it.int == type }

        if (_crc16 == currCrc16 && enumType != null) {
            if (topicNull) {
                topic = null
            }
            return ObjBip39Address(enumType, topic, payload)
        }
        return null;
    }
}
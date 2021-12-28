package counters.bip39_k2v

import counters.bip39_k2v.Enum.TypeBip39Address
import counters.bip39_k2v.Utils.HelpHex
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Bip39AddressTest {
    private val bip39Address = Bip39Address()

    private data class Values(
        val address: String,
        val string: String? = null,
        val int: Long? = null,
        val double: Double? = null,
        val key: String? = "",
        val type: TypeBip39Address,
        val success: Boolean = true,
    )

    private val examples = listOf(
        Values(string = "Hello Minter!", key = "key", type = TypeBip39Address.ASCII, address = "0248656c6c6f204d696e74657221016b657976a5"),
//        Values(double = 0.32, key = "BIP", type = TypeBip39Address.DOUBLE, address = "033fd47ae147ae14000142495001316e7c70a0ff"),
//        Values(int = 4123456789, key = "count", type = TypeBip39Address.INTEGER, address = "01f5c6f51501636f756e740164587b202a777c51"),
        Values(int = 42, key = "answer", type = TypeBip39Address.INTEGER, address = "012a01616e7377657201544f7b5a4e504e56e463"),
    )


    @BeforeEach
    fun setUp() {
    }

    @Test
    fun encode() {
        examples.forEach { expected ->

            val actual = when (expected.type) {
                TypeBip39Address.INTEGER -> {
                    bip39Address.encode(expected.type, expected.key!!, expected.int!!)?.let { address ->
                        bip39Address.decode(address)?.let {
                            Values(int = Integer.parseInt(it.payload, 16).toLong(), key = it.topic ?: "", type = it.type, address = expected.address)
                        } ?: run {
                            null
                        }
                    } ?: run {
                        null
                    }
                }
                TypeBip39Address.ASCII -> {
                    bip39Address.encode(expected.type, expected.key!!, expected.string!!)?.let { address ->
                        bip39Address.decode(address)?.let {
                            Values(string = HelpHex().getText(it.payload), key = it.topic ?: "", type = it.type, address = expected.address)
                        } ?: run {
                            null
                        }
                    } ?: run {
                        null
                    }
                }
                else -> {
                    null
                }
            }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun decode() {
        examples.forEach { expected ->
            bip39Address.decode(expected.address).let {
                assertEquals(expected.type, it?.type)
                assertEquals(expected.key, it?.topic)

                when (expected.type) {
                    TypeBip39Address.INTEGER -> {
                        assertEquals(expected.int, Integer.parseInt(it?.payload, 16).toLong())
                    }
                    TypeBip39Address.ASCII -> {
                        assertEquals(expected.string, HelpHex().getText(it!!.payload))
                    }
                    else -> {
                        assert(false)
                    }
                }
//                assertEquals(expected., it?.type)
//                assertEquals(expected.type, it?.type)
//                assertEquals(expected.type, it?.type)
            }
        }

    }
}
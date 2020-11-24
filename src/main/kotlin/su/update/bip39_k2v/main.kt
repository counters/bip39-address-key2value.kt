package su.update.bip39_k2v

import su.update.bip39_k2v.Enum.TypeBip39Address
import su.update.bip39_k2v.Utils.HelpHex

fun main(args: Array<String>) {
    val bip39Address = Bip39Address()

    val payload = "Hello World"
    val type = TypeBip39Address.ASCII
//    val payload = 197379L // 197379  16843009
//    val type = TypeBip39Address.INTEGER
    var address = bip39Address.encode(type, "", payload)
    println("Mx"+address)
    if (address!=null) {
        bip39Address.decode(address)?.let {
            println(it)
            val _payload = it.payload
            if (it.type==TypeBip39Address.INTEGER){
//                println(_payload)
                println(Integer.parseInt(_payload, 16))
            } else if (it.type==TypeBip39Address.ASCII){
                println(HelpHex().getText(_payload))
            }

        }
    }
}
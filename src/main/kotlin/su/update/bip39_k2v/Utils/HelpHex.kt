package su.update.bip39_k2v.Utils

class HelpHex {

    fun fromNum(num: Int): String {
        val hex = num.toString(16)
        if (hex.length % 2 == 0) return hex else return "0"+hex
    }

    fun fromNum(num: Long): String {
        val hex = num.toString(16)
        if (hex.length % 2 == 0) return hex else return "0"+hex
    }

    fun getASCII(text: String): String {
        var hex = ""
        text.toByteArray().forEach {
            hex += fromNum(it.toInt())
        }
        return hex
    }

    fun getText(textHex: String): String {
        var text = ""
        for (start in 0..(textHex.length-2) step 2) {
            text += Integer.parseInt(textHex.substring(start, start + 2), 16).toChar()
        }
        return text
    }

}
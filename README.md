# bip39-address-key2value.kt

Kotlin  implementation for [bip39-address-key2value](https://github.com/counters/bip39-address-key2value)
  
## Examples

Encode
```kotlin
val payload = "Hello World!"
val key = "key"
val type = TypeBip39Address.ASCII
var address = bip39Address.encode(type, key, payload)
println("Mx"+address) // Mx0248656c6c6f20576f726c64210301454a5437d8
```

Decode
```kotlin
val address = "0248656c6c6f20576f726c64210301454a5437d8"
bip39Address.decode(address)?.let {
    println(it) //  ObjBip39Address(type=ASCII, topic=key, payload=48656c6c6f20576f726c64)
    val _payload = it.payload
    if (it.type == TypeBip39Address.INTEGER) {
        println(Integer.parseInt(_payload, 16))
    } else if (it.type == TypeBip39Address.ASCII) {
        println(HelpHex().getText(_payload)) // Hello World!
    }
}
```
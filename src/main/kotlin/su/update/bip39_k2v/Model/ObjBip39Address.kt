package su.update.bip39_k2v.Model

import su.update.bip39_k2v.Enum.TypeBip39Address

data class ObjBip39Address(
        val type: TypeBip39Address,
        val topic: String?,
        val payload: String,
)
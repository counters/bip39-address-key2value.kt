package su.update.bip39_k2v.Enum

enum class TypeBip39Address (val int: Int) {
    RAW (0),
    INTEGER (1),
    ASCII (2),
    DOUBLE (3),
    BINARY (4),
    UNIXTIME (5),
    DATETIME (6),
    UTF8 (7),
    MPIP (8),
}
package com.tosmo.ktils.extension

/**
 * 将十进制的数以[carryBit]的进制转换，返回转换后每一位所对应的十进制值
 */
fun Long.decTo(carryBit: Int): LongArray {
    var n = this
    val arr = ArrayDeque<Long>()
    do {
        arr.addFirst(n % carryBit)
        n /= carryBit
        if (n in 1 until carryBit) {
            arr.addFirst(n)
        }
    } while (n >= carryBit)
    return arr.toLongArray()
}

/**
 * 将十进制的数以[carryBit]的进制转换，返回转换后每一位所对应的十进制值
 */
fun Int.decTo(carryBit: Int): IntArray {
    val arr = toLong().decTo(carryBit)
    return IntArray(arr.size) {
        arr[it].toInt()
    }
}

/**
 * 将十进制的数以[carryBit]的进制转换，返回转换后每一位所对应的十进制值
 */
fun Short.decTo(carryBit: Int): ShortArray {
    val arr = toLong().decTo(carryBit)
    return ShortArray(arr.size) {
        arr[it].toShort()
    }
}

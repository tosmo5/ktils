package com.tosmo.ktils.nodemap.exception

import com.tosmo.ktils.nodemap.data.TypeKey

/**
 *
 * @author Thomas Miao
 */
class ValueTypeException : Exception {
    constructor(key: TypeKey, valueValue: Any) : super("值的类型错误，应为：${key.typeKClass}\n\t实际为：${valueValue::class}")

    constructor() : super("值的类型错误")
}
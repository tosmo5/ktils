package com.tosmo.ktils.typemap.exception

import com.tosmo.ktils.typemap.data.TypeKey

/**
 *
 * @author Thomas Miao
 */
class ValueTypeException : Exception {
    constructor(key: TypeKey, valueValue: Any) : super("值的类型错误，应为：${key.typeKClass}\n\t实际为：${valueValue::class}")

    constructor() : super("值的类型错误")
}
package com.tosmo.ktils.typemap.data

import kotlin.reflect.KClass

/**
 * 特定类型键
 *
 * @author Thomas Miao
 */
interface TypeKey {
    /**
     * 键名，此值唯一
     */
    val keyName: String

    /**
     * 此键保存的值的类型类
     */
    val typeKClass: KClass<*>

    /**
     * 解析未知类型的值，返回转换后的值
     */
    fun parseValueType(unknownValue: Any?): Any?
}
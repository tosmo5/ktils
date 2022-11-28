package com.tosmo.ktils.nodemap.data

/**
 * 值
 *
 * @author Thomas Miao
 */
interface TypeValue<T, N : Node> {
    /**
     * 值的值
     */
    val valueValue: T
}
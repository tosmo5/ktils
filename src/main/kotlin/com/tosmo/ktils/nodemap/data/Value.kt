package com.tosmo.ktils.nodemap.data

/**
 * 值要实现的接口
 *
 * @author Thomas Miao
 */
interface Value<T, N : Node> {
    /**
     * 值的值
     */
    val valueValue: T
}
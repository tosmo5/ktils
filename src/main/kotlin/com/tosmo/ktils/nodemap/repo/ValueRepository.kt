package com.tosmo.ktils.nodemap.repo

import com.tosmo.ktils.nodemap.data.Key
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.data.Value

/**
 * 值存储接口
 *
 * @author Thomas Miao
 */
interface ValueRepository<K : Key, N : Node, out V : Value<*, N>> {

    /**
     * 添加值[value]，成功返回true
     */
    fun addValue(value: @UnsafeVariance V): Boolean

    /**
     * 修改值
     */
    fun updateValue(value: @UnsafeVariance V): Boolean

    /**
     * 根据节点取得值
     */
    fun getValue(node: N): V?

    /**
     * [value]是否已存在
     */
    fun containsValue(value: @UnsafeVariance V): Boolean

    /**
     * 根据键删除值
     */
    fun deleteByKey(key: K): Int
}
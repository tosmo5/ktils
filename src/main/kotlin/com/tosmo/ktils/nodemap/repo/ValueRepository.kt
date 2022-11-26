package com.tosmo.ktils.nodemap.repo

import com.tosmo.ktils.nodemap.data.Key
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.data.Value

/**
 * 值存储接口
 *
 * @author Thomas Miao
 */
interface ValueRepository<K : Key, N : Node, out V : Value<*, *>> {
    /**
     * 在节点[node]添加值[value]，成功返回true
     */
    fun addValue(key: K, node: N, value: @UnsafeVariance V): Boolean

    /**
     * 修改值
     */
    fun updateValue(key: K, node: N, value: @UnsafeVariance V): Boolean

    /**
     * 根据节点取得值
     */
    fun getValue(key: K, node: N): V?

    /**
     * 根据键取得所有值
     */
    fun getKeyValues(key: K): List<V>

    /**
     * [value]在[node]下是否已存在
     */
    fun containsValue(node: N, value: @UnsafeVariance V): Boolean

    /**
     * 根据键删除值
     */
    fun deleteByKey(key: K): Int
}
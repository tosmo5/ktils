package com.tosmo.ktils.typemap.repo

import com.tosmo.ktils.typemap.data.TypeKey
import com.tosmo.ktils.typemap.data.Index
import com.tosmo.ktils.typemap.data.TypeValue

/**
 * 值存储接口
 *
 * @author Thomas Miao
 */
interface TypeValueRepository<K : TypeKey, I : Index, out V : TypeValue<*, I>> {

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
    fun getValue(index: I): V?

    /**
     * [value]是否已存在
     */
    fun containsValue(value: @UnsafeVariance V): Boolean

    /**
     * 根据键删除值
     */
    fun deleteByKey(key: K): Int
}
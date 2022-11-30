package com.tosmo.ktils.nodemap.provider

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Index
import com.tosmo.ktils.nodemap.data.TypeValue
import com.tosmo.ktils.nodemap.repo.TypeValueRepository

/**
 * 值存储接口分配器
 *
 * @author Thomas Miao
 */
fun interface ValueRepositoryProvider<K : TypeKey, I : Index> {
    /**
     * @param key 键
     * @return 值的操作接口
     */
    fun provide(key: K): TypeValueRepository<K, I, TypeValue<*, I>>
}
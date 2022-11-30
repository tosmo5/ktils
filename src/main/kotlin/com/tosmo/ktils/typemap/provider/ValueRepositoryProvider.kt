package com.tosmo.ktils.typemap.provider

import com.tosmo.ktils.typemap.data.TypeKey
import com.tosmo.ktils.typemap.data.Index
import com.tosmo.ktils.typemap.data.TypeValue
import com.tosmo.ktils.typemap.repo.TypeValueRepository

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
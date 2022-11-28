package com.tosmo.ktils.nodemap.provider

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.data.TypeValue
import com.tosmo.ktils.nodemap.repo.TypeValueRepository

/**
 * 值存储接口分配器
 *
 * @author Thomas Miao
 */
fun interface ValueRepositoryProvider<K : TypeKey, N : Node> {
    /**
     * @param key 键
     * @return 值的操作接口
     */
    fun provide(key: K): TypeValueRepository<K, N, TypeValue<*, N>>
}
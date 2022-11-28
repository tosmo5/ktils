package com.tosmo.ktils.nodemap.provider

import com.tosmo.ktils.nodemap.data.Key
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.data.Value
import com.tosmo.ktils.nodemap.repo.ValueRepository

/**
 * 值存储接口分配器
 *
 * @author Thomas Miao
 */
fun interface ValueRepositoryProvider<K : Key, N : Node> {
    /**
     * @param key 键
     * @return 值的操作接口
     */
    fun provide(key: K): ValueRepository<K, N, Value<*, N>>
}
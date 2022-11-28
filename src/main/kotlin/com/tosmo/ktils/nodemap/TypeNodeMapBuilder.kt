package com.tosmo.ktils.nodemap

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.nodemap.repo.TypeKeyRepository
import com.tosmo.ktils.nodemap.repo.NodeRepository

/**
 * 结点映射构造器
 *
 * @author Thomas Miao
 */
class TypeNodeMapBuilder<K : TypeKey, N : Node> {

    private lateinit var mKeyRepo: TypeKeyRepository<K>

    private lateinit var mNodeRepo: NodeRepository<K, N>

    private lateinit var mValueRepoProvider: ValueRepositoryProvider<K, N>

    /**
     * 构造一个节点映射
     */
    fun build(): TypeNodeMap<K, N> {
        require(this::mKeyRepo.isInitialized) { "KeyRepository未初始化" }
        require(this::mNodeRepo.isInitialized) { "NodeRepository未初始化" }
        require(this::mValueRepoProvider.isInitialized) { "ValueRepositoryProvider未初始化" }
        return TypeNodeMapImpl(mKeyRepo, mNodeRepo, mValueRepoProvider)
    }

    /**
     * 设置键存储接口
     */
    fun setKeyRepository(repo: TypeKeyRepository<K>): TypeNodeMapBuilder<K, N> {
        mKeyRepo = repo
        return this
    }

    /**
     * 设置节点存储接口
     */
    fun setNodeRepository(repo: NodeRepository<K, N>): TypeNodeMapBuilder<K, N> {
        mNodeRepo = repo
        return this
    }

    /**
     * 设置值存储接口分配器
     */
    fun setValueRepositoryProvider(provider: ValueRepositoryProvider<K, N>): TypeNodeMapBuilder<K, N> {
        mValueRepoProvider = provider
        return this
    }
}
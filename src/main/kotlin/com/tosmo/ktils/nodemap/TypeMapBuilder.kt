package com.tosmo.ktils.nodemap

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Index
import com.tosmo.ktils.nodemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.nodemap.repo.TypeKeyRepository
import com.tosmo.ktils.nodemap.repo.IndexRepository

/**
 * 结点映射构造器
 *
 * @author Thomas Miao
 */
class TypeMapBuilder<K : TypeKey, N : Index> {

    private lateinit var mKeyRepo: TypeKeyRepository<K>

    private lateinit var mIndexRepo: IndexRepository<K, N>

    private lateinit var mValueRepoProvider: ValueRepositoryProvider<K, N>

    /**
     * 构造一个[TypeMap]
     */
    fun build(): TypeMap<K, N> {
        require(this::mKeyRepo.isInitialized) { "未初始化：${mKeyRepo::class}" }
        require(this::mIndexRepo.isInitialized) { "未初始化：${mIndexRepo::class}" }
        require(this::mValueRepoProvider.isInitialized) { "未初始化：${mValueRepoProvider::class}" }
        return TypeMapImpl(mKeyRepo, mIndexRepo, mValueRepoProvider)
    }

    /**
     * 设置[TypeKeyRepository]
     */
    fun setKeyRepository(repo: TypeKeyRepository<K>): TypeMapBuilder<K, N> {
        mKeyRepo = repo
        return this
    }

    /**
     * 设置[IndexRepository]
     */
    fun setIndexRepository(repo: IndexRepository<K, N>): TypeMapBuilder<K, N> {
        mIndexRepo = repo
        return this
    }

    /**
     * 设置[ValueRepositoryProvider]
     */
    fun setValueRepositoryProvider(provider: ValueRepositoryProvider<K, N>): TypeMapBuilder<K, N> {
        mValueRepoProvider = provider
        return this
    }
}
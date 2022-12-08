package com.tosmo.ktils.typemap

import com.tosmo.ktils.typemap.data.TypeKey
import com.tosmo.ktils.typemap.data.Index
import com.tosmo.ktils.typemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.typemap.repo.TypeKeyRepository
import com.tosmo.ktils.typemap.repo.IndexRepository

/**
 * 结点映射构造器
 *
 * @author Thomas Miao
 */
class TypeMapBuilder<K : TypeKey, I : Index> {

    private lateinit var mKeyRepo: TypeKeyRepository<K>

    private lateinit var mIndexRepo: IndexRepository<K, I>

    private lateinit var mValueRepoProvider: ValueRepositoryProvider<K, I>

    /**
     * 构造一个[TypeMap]
     */
    fun build(): TypeMap<K, I> {
        require(this::mKeyRepo.isInitialized) { "未初始化：${mKeyRepo::class}" }
        require(this::mIndexRepo.isInitialized) { "未初始化：${mIndexRepo::class}" }
        require(this::mValueRepoProvider.isInitialized) { "未初始化：${mValueRepoProvider::class}" }
        return TypeMapImpl(mKeyRepo, mIndexRepo, mValueRepoProvider)
    }

    /**
     * 设置[TypeKeyRepository]
     */
    fun setKeyRepository(repo: TypeKeyRepository<K>): TypeMapBuilder<K, I> {
        mKeyRepo = repo
        return this
    }

    /**
     * 设置[IndexRepository]
     */
    fun setIndexRepository(repo: IndexRepository<K, I>): TypeMapBuilder<K, I> {
        mIndexRepo = repo
        return this
    }

    /**
     * 设置[ValueRepositoryProvider]
     */
    fun setValueRepositoryProvider(provider: ValueRepositoryProvider<K, I>): TypeMapBuilder<K, I> {
        mValueRepoProvider = provider
        return this
    }
}
package com.tosmo.ktils.nodemap.repo

import com.tosmo.ktils.nodemap.data.TypeKey

/**
 * 键的存储接口
 *
 * @author Thomas Miao
 */
interface TypeKeyRepository<K : TypeKey> {
    /**
     * 添加
     */
    fun addKey(key: K): Boolean

    /**
     * 添加[keys]中所有的键，返回成功的数量
     */
    fun addAllKeys(keys: Collection<K>): Int

    /**
     * 根据[key]删除
     */
    fun deleteKey(key: K): Boolean

    /**
     * 删除所有的键，返回成功的数量
     */
    fun deleteAllKeys(): Int

    /**
     * 取得键名为[keyName]的键
     */
    fun getKeyByName(keyName: String): K?

    /**
     * 根据键名集合，取得对应的所有键
     */
    fun getKeysByNames(keyNames: Collection<String>): List<K>

    /**
     * 取得保存的所有键
     */
    fun getAllKeys(): List<K>

    /**
     * 取得保存的所有键的名称
     */
    fun getAllKeyNames(): List<String>

    /**
     * 查询是否没有键
     */
    fun isNoKey(): Boolean

    /**
     * 查询是否存在键名
     */
    fun hasKeyName(keyName: String): Boolean

    /**
     * 查询是否存在[key]
     */
    fun hasKey(key: K): Boolean

    /**
     * 计算键的数量
     */
    fun countKey(): Int
}
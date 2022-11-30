package com.tosmo.ktils.nodemap

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Index
import com.tosmo.ktils.nodemap.data.TypeValue
import com.tosmo.ktils.nodemap.exception.MissingKeyException
import com.tosmo.ktils.nodemap.exception.MissingIndexException
import com.tosmo.ktils.nodemap.exception.ValueTypeException
import com.tosmo.ktils.nodemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.nodemap.repo.TypeKeyRepository
import com.tosmo.ktils.nodemap.repo.IndexRepository

/**
 * 分类映射集合
 *
 * @author Thomas Miao
 */
interface TypeMap<K : TypeKey, I : Index> {

    /**
     * 键存储接口
     */
    val keyRepository: TypeKeyRepository<K>

    /**
     * 索引存储接口
     */
    val indexRepository: IndexRepository<K, I>

    /**
     * 值存储接口分配器
     */
    val valueRepositoryProvider: ValueRepositoryProvider<K, I>

    /**
     * 键的数量
     */
    val keysSize: Int

    /**
     * 所有的键集合
     */
    val keys: List<K>

    /**
     * 所有键的名称
     */
    val keyNames: List<String>

    /**
     * 是否含[key]
     */
    fun containsKey(key: K): Boolean

    /**
     * 是否含名为[keyName]的键
     */
    fun containsKey(keyName: String): Boolean

    /**
     * 是否有值[value]
     */
    fun containsValue(keyName: String, value: TypeValue<*, I>): Boolean

    /**
     * 是否有值[value]
     */
    fun containsValue(key: K, value: TypeValue<*, I>): Boolean

    /**
     * 清空所有的值，返回删除的值数
     */
    fun clearValues(): Int

    /**
     * 删除键名为[keyName]键的所有值和节点，返回删除的值数
     */
    fun clearKeyValues(keyName: String): Int

    /**
     * 删除键[key]的所有值和节点，返回删除的值数
     */
    fun clearKeyValues(key: K): Int

    /**
     * 删除键名为[keyName]的键
     *
     * @param clearValues true：同时删除对应的值
     */
    fun clearKey(keyName: String, clearValues: Boolean = true)

    /**
     * 删除键[key]的键
     *
     * @param clearValues true：同时删除对应的值
     */
    fun clearKey(key: K, clearValues: Boolean = true)

    /**
     * 清空所有键、节点、值，返回删除的键数
     */
    fun clearAll(): Int

    /**
     * 是否没有键
     */
    fun isKeysEmpty(): Boolean

    /**
     * 添加键
     */
    fun putKey(key: K): Boolean

    /**
     * 添加所有键，返回成功数量
     */
    fun putMultiKeys(keys: Collection<K>): Int

    /**
     * 根据键名取得键
     */
    fun getKey(keyName: String): K?

    /**
     * 根据节点取得键
     */
    fun getKey(node: I): K?

    /**
     * 根据键名批量取得键
     */
    fun getMutliKeys(keyNames: Collection<String>): List<K>

    /**
     * 取得[index]下的[TypeValue]
     */
    fun getValue(index: I): TypeValue<*, I>?

    /**
     * 取得[index]下的[TypeValue]
     *
     * @param key 提供后不再查询键
     */
    fun getValue(key: K, index: I): TypeValue<*, I>?

    /**
     * 设置[index]的[TypeValue]为[value]，如果不存在，则新增
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     */
    fun setValue(index: I, value: TypeValue<*, I>): Boolean

    /**
     * 设置[index]的[TypeValue]为[value]，如果不存在，则新增
     *
     * @param key 提供后不再查询键
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     */
    fun setValue(key: K, index: I, value: TypeValue<*, I>): Boolean

    /**
     * 取得默认[Index]
     */
    fun getDefaultNode(key: K): I?

    /**
     * 取得默认[Index]
     */
    fun getDefaultNode(keyName: String): I?

    /**
     * 取得[keyName]的默认值，找不到返回空
     */
    fun getDefaultValue(keyName: String): TypeValue<*, I>?

    /**
     * 取得[key]的默认值，找不到返回空
     */
    fun getDefaultValue(key: K): TypeValue<*, I>?

    /**
     * 更新或新增[keyName]默认值为[value]
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     * @exception MissingIndexException
     */
    fun setDefaultValue(keyName: String, value: TypeValue<*, I>): Boolean

    /**
     * 更新或新增[key]默认值为[value]
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     * @exception MissingIndexException
     */
    fun setDefaultValue(key: K, value: TypeValue<*, I>): Boolean
}
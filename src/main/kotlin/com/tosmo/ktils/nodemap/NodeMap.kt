package com.tosmo.ktils.nodemap

import com.tosmo.ktils.nodemap.data.Key
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.data.Value
import com.tosmo.ktils.nodemap.exception.MissingKeyException
import com.tosmo.ktils.nodemap.exception.MissingNodeException
import com.tosmo.ktils.nodemap.exception.ValueTypeException
import com.tosmo.ktils.nodemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.nodemap.repo.KeyRepository
import com.tosmo.ktils.nodemap.repo.NodeRepository

/**
 * 节点映射集合
 *
 * @author Thomas Miao
 */
interface NodeMap<K : Key, N : Node> {

    /**
     * 键存储接口
     */
    val keyRepository: KeyRepository<K>

    /**
     * 节点存储接口
     */
    val nodeRepository: NodeRepository<K, N>

    /**
     * 值存储接口分配器
     */
    val valueRepositoryProvider: ValueRepositoryProvider<K, N>

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
    fun containsValue(keyName: String, value: Value<*, N>): Boolean

    /**
     * 是否有值[value]
     */
    fun containsValue(key: K, value: Value<*, N>): Boolean

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
    fun putMultiKeys(key: Collection<K>): Int

    /**
     * 根据键名取得键
     */
    fun getKey(keyName: String): K?

    /**
     * 根据节点取得键
     */
    fun getKey(node: N): K?

    /**
     * 根据键名批量取得键
     */
    fun getMutliKeys(keyNames: Collection<String>): List<K>

    /**
     * 取得节点[node]下的值
     */
    fun getValue(node: N): Value<*, N>?

    /**
     * 取得节点[node]下的值
     *
     * @param key 提供后不再查询键
     */
    fun getValue(key: K, node: N): Value<*, N>?

    /**
     * 设置[node]的值为[value]，如果不存在，则新增
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     */
    fun setValue(node: N, value: Value<*, N>): Boolean

    /**
     * 设置[node]的值为[value]，如果不存在，则新增
     *
     * @param key 提供后不再查询键
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     */
    fun setValue(key: K, node: N, value: Value<*, N>): Boolean

    /**
     * 取得默认节点
     */
    fun getDefaultNode(key: K): N?

    /**
     * 取得默认节点
     */
    fun getDefaultNode(keyName: String): N?

    /**
     * 取得[keyName]的默认值，找不到返回空
     */
    fun getDefaultValue(keyName: String): Value<*, N>?

    /**
     * 取得[key]的默认值，找不到返回空
     */
    fun getDefaultValue(key: K): Value<*, N>?

    /**
     * 更新或新增[keyName]默认值为[value]
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     * @exception MissingNodeException
     */
    fun setDefaultValue(keyName: String, value: Value<*, N>): Boolean

    /**
     * 更新或新增[key]默认值为[value]
     *
     * @exception ValueTypeException
     * @exception MissingKeyException
     * @exception MissingNodeException
     */
    fun setDefaultValue(key: K, value: Value<*, N>): Boolean
}
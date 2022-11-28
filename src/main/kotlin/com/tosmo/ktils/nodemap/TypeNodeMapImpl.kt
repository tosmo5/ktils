package com.tosmo.ktils.nodemap

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Node
import com.tosmo.ktils.nodemap.data.TypeValue
import com.tosmo.ktils.nodemap.exception.MissingKeyException
import com.tosmo.ktils.nodemap.exception.ValueTypeException
import com.tosmo.ktils.nodemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.nodemap.repo.TypeKeyRepository
import com.tosmo.ktils.nodemap.repo.NodeRepository
import kotlin.reflect.full.isSuperclassOf

/**
 *
 * @author Thomas Miao
 */
class TypeNodeMapImpl<K : TypeKey, N : Node> internal constructor(
    keyRepo: TypeKeyRepository<K>,
    nodeRepo: NodeRepository<K, N>,
    valueRepoProvider: ValueRepositoryProvider<K, N>
) : TypeNodeMap<K, N> {

    override val keyRepository: TypeKeyRepository<K> = keyRepo

    override val nodeRepository: NodeRepository<K, N> = nodeRepo

    override val valueRepositoryProvider: ValueRepositoryProvider<K, N> = valueRepoProvider

    override val keysSize: Int
        get() = keyRepository.countKey()

    override val keys: List<K>
        get() = keyRepository.getAllKeys()

    override val keyNames: List<String>
        get() = keyRepository.getAllKeyNames()

    override fun containsKey(key: K): Boolean {
        return keyRepository.hasKey(key)
    }

    override fun containsKey(keyName: String): Boolean {
        return keyRepository.hasKeyName(keyName)
    }

    override fun containsValue(keyName: String, value: TypeValue<*, N>): Boolean {
        return getKey(keyName)?.let {
            containsValue(it, value)
        } ?: false
    }

    override fun containsValue(key: K, value: TypeValue<*, N>): Boolean {
        return valueRepositoryProvider.provide(key).containsValue(value)
    }

    override fun clearValues(): Int {
        nodeRepository.deleteAllNode()
        return keys.sumOf {
            valueRepositoryProvider.provide(it).deleteByKey(it)
        }
    }

    override fun clearKeyValues(keyName: String): Int {
        return getKey(keyName)?.let { clearKeyValues(it) } ?: 0
    }

    override fun clearKeyValues(key: K): Int {
        // 根据键除节点
        nodeRepository.deleteByKey(key)
        // 根据键分配值的存储接口并删除键的值，返回删除的个数
        return valueRepositoryProvider.provide(key).deleteByKey(key)
    }

    override fun clearKey(keyName: String, clearValues: Boolean) {
        getKey(keyName)?.let {// 取得键
            clearKey(it, clearValues)
        }
    }

    override fun clearKey(key: K, clearValues: Boolean) {
        // 根据键除节点
        nodeRepository.deleteByKey(key)
        if (clearValues) {
            // 根据键分配值的存储接口并删除键的值，返回删除的个数
            valueRepositoryProvider.provide(key).deleteByKey(key)
        }
        // 删除键
        keyRepository.deleteKey(key)
    }

    override fun clearAll(): Int {
        clearValues()
        return keyRepository.deleteAllKeys()
    }

    override fun isKeysEmpty(): Boolean {
        return keyRepository.isNoKey()
    }

    override fun putKey(key: K): Boolean {
        return keyRepository.addKey(key)
    }

    override fun putMultiKeys(keys: Collection<K>): Int {
        return keyRepository.addAllKeys(this.keys)
    }

    override fun getKey(keyName: String): K? {
        return keyRepository.getKeyByName(keyName)
    }

    override fun getKey(node: N): K? {
        return nodeRepository.getKeyByNode(node)
    }

    override fun getMutliKeys(keyNames: Collection<String>): List<K> {
        return keyRepository.getKeysByNames(keyNames)
    }

    override fun getValue(node: N): TypeValue<*, N>? {
        return getKey(node)?.let { getValue(it, node) }
    }

    override fun getValue(key: K, node: N): TypeValue<*, N>? {
        return valueRepositoryProvider.provide(key).getValue(node)
    }

    override fun setValue(node: N, value: TypeValue<*, N>): Boolean {
        return getKey(node)?.let {
            setValue(it, node, value)
        } ?: throw MissingKeyException()
    }

    override fun setValue(key: K, node: N, value: TypeValue<*, N>): Boolean {
        // 判断值的类型是否正确
        if (!checkValueType(key, value)) {
            throw ValueTypeException(key, value)
        }
        val valueRepo = valueRepositoryProvider.provide(key)
        // 更新或新增
        return if (containsValue(key, value)) {
            valueRepo.updateValue(value)
        } else {
            valueRepo.addValue(value)
        } && nodeRepository.addNode(node)
    }

    override fun getDefaultNode(key: K): N? {
        return nodeRepository.getDefaultNode(key)
    }

    override fun getDefaultNode(keyName: String): N? {
        return getKey(keyName)?.let { getDefaultNode(it) }
    }

    override fun getDefaultValue(keyName: String): TypeValue<*, N>? {
        return getKey(keyName)?.let { getDefaultValue(it) }
    }

    override fun getDefaultValue(key: K): TypeValue<*, N>? {
        return nodeRepository.getDefaultNode(key)?.let {// 取得默认节点
            valueRepositoryProvider.provide(key).getValue(it)
        }
    }

    override fun setDefaultValue(keyName: String, value: TypeValue<*, N>): Boolean {
        // 取出值对象
        return getKey(keyName)?.let { key ->
            setDefaultValue(key, value)
        } ?: throw MissingKeyException("[${keyName}]")
    }

    override fun setDefaultValue(key: K, value: TypeValue<*, N>): Boolean {
        // 判断值的类型是否正确
        if (!checkValueType(key, value)) {
            throw ValueTypeException(key, value)
        }
        // 分配值存储接口
        val valueRepo = valueRepositoryProvider.provide(key)
        // 如果存在则更新，否则新增
        return if (valueRepo.containsValue(value)) {
            valueRepo.updateValue(value)
        } else {
            valueRepo.addValue(value)
        }
    }

    internal fun checkValueType(key: TypeKey, value: TypeValue<*, *>): Boolean {
        return value.valueValue?.let {
            key.typeKClass.isSuperclassOf(it::class)
        } ?: true
    }
}
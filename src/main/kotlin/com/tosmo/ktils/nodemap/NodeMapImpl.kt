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
import kotlin.reflect.full.isSuperclassOf

/**
 *
 * @author Thomas Miao
 */
class NodeMapImpl<K : Key, N : Node> internal constructor(
    keyRepo: KeyRepository<K>,
    nodeRepo: NodeRepository<K, N>,
    valueRepoProvider: ValueRepositoryProvider<K, N>
) : NodeMap<K, N> {

    override val keyRepository: KeyRepository<K> = keyRepo

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

    override fun containsNode(node: N): Boolean {
        return nodeRepository.hasNode(node)
    }

    override fun containsValue(keyName: String, node: N, value: Value<*, N>): Boolean {
        return getKey(keyName)?.let {
            containsValue(it, node, value)
        } ?: false
    }

    override fun containsValue(key: K, node: N, value: Value<*, N>): Boolean {
        return valueRepositoryProvider.provide(key).containsValue(node, value)
    }

    override fun clearValues(clearNodes: Boolean): Int {
        var count = 0
        keys.forEach { k ->
            if (clearNodes) {
                nodeRepository.deleteKeyNodes(k)
            }
            count += valueRepositoryProvider.provide(k).deleteByKey(k)
        }
        return count
    }

    override fun clearKeyValues(keyName: String, clearNodes: Boolean): Int {
        return getKey(keyName)?.let {
            if (clearNodes) {
                nodeRepository.deleteKeyNodes(it)
            }
            valueRepositoryProvider.provide(it).deleteByKey(it)
        } ?: 0
    }

    override fun clearKey(keyName: String, clearValues: Boolean, clearNodes: Boolean) {
        getKey(keyName)?.let {
            if (clearNodes) {
                nodeRepository.deleteKeyNodes(it)
            }
            if (clearValues) {
                valueRepositoryProvider.provide(it).deleteByKey(it)
            }
        }
        keyRepository.deleteKeyByName(keyName)
    }

    override fun clearAll(): Int {
        clearValues(true)
        return keyRepository.deleteAllKeys()
    }

    override fun isKeysEmpty(): Boolean {
        return keyRepository.isNoKey()
    }

    override fun isNodeValuesEmpty(node: N): Boolean {
        return nodeRepository.isNodeNoValues(node)
    }

    override fun putKey(key: K): Boolean {
        return keyRepository.addKey(key)
    }

    override fun putMultiKeys(key: Collection<K>): Int {
        return keyRepository.addAllKeys(keys)
    }

    override fun getKey(keyName: String): K? {
        return keyRepository.getKeyByName(keyName)
    }

    override fun getMutliKeys(keyNames: Collection<String>): List<K> {
        return keyRepository.getKeysByNames(keyNames)
    }

    override fun getValue(keyName: String, node: N): Value<*, N>? {
        return getKey(keyName)?.let {
            getValue(it, node)
        }
    }

    override fun getValue(key: K, node: N): Value<*, N>? {
        return valueRepositoryProvider.provide(key).getValue(key, node)
    }

    override fun setValue(keyName: String, node: N, value: Value<*, N>): Boolean {
        return getKey(keyName)?.let {
            setValue(it, node, value)
        } ?: throw MissingKeyException("[${keyName}]")
    }

    override fun setValue(key: K, node: N, value: Value<*, N>): Boolean {
        // 判断值的类型是否正确
        if (!checkValueType(key, value)) {
            throw ValueTypeException(key, value)
        }
        val valueRepo = valueRepositoryProvider.provide(key)
        // 更新或新增
        return if (containsValue(key, node, value)) {
            valueRepo.updateValue(key, node, value)
        } else {
            valueRepo.addValue(key, node, value)
        }
    }

    override fun getDefaultValue(keyName: String): Value<*, N>? {
        return getKey(keyName)?.let { getDefaultValue(it) }
    }

    override fun getDefaultValue(key: K): Value<*, N>? {
        return nodeRepository.getDefaultNode(key.keyName)?.let {
            valueRepositoryProvider.provide(key).getValue(key, it)
        }
    }

    override fun setDefaultValue(keyName: String, value: Value<*, N>): Boolean {
        // 取出值对象
        return getKey(keyName)?.let { key ->
            setDefaultValue(key, value)
        } ?: throw MissingKeyException("[${keyName}]")
    }

    override fun setDefaultValue(key: K, value: Value<*, N>): Boolean {
        // 判断值的类型是否正确
        if (!checkValueType(key, value)) {
            throw ValueTypeException(key, value)
        }
        // 取出默认的节点，并设置判断非空
        val node = nodeRepository.getDefaultNode(key.keyName) ?: throw MissingNodeException()
        // 分配值存储接口
        val valueRepo = valueRepositoryProvider.provide(key)
        // 如果存在则更新，否则新增
        return if (containsValue(key, node, value)) {
            valueRepo.updateValue(key, node, value)
        } else {
            valueRepo.addValue(key, node, value)
        }
    }

    override fun checkValueType(key: Key, value: Value<*, *>): Boolean {
        return value.valueValue?.let {
            key.typeKClass.isSuperclassOf(it::class)
        } ?: true
    }
}
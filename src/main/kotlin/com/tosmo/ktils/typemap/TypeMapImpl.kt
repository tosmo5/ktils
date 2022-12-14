package com.tosmo.ktils.typemap

import com.tosmo.ktils.typemap.data.TypeKey
import com.tosmo.ktils.typemap.data.Index
import com.tosmo.ktils.typemap.data.TypeValue
import com.tosmo.ktils.typemap.exception.MissingKeyException
import com.tosmo.ktils.typemap.exception.ValueTypeException
import com.tosmo.ktils.typemap.provider.ValueRepositoryProvider
import com.tosmo.ktils.typemap.repo.TypeKeyRepository
import com.tosmo.ktils.typemap.repo.IndexRepository
import kotlin.reflect.full.isSuperclassOf

/**
 *
 * @author Thomas Miao
 */
internal class TypeMapImpl<K : TypeKey, N : Index>(
    keyRepo: TypeKeyRepository<K>,
    nodeRepo: IndexRepository<K, N>,
    valueRepoProvider: ValueRepositoryProvider<K, N>
) : TypeMap<K, N> {

    override val keyRepository: TypeKeyRepository<K> = keyRepo

    override val indexRepository: IndexRepository<K, N> = nodeRepo

    override val valueRepositoryProvider: ValueRepositoryProvider<K, N> = valueRepoProvider

    override val keysSize: Int
        get() = keyRepository.countKey()

    override val keys: List<K>
        get() = keyRepository.getAllKeys()

    override val keyNames: List<String>
        get() = keyRepository.getAllKeyValues()

    override fun containsKey(key: K): Boolean {
        return keyRepository.hasKey(key)
    }

    override fun containsKey(keyValue: String): Boolean {
        return keyRepository.hasKeyValue(keyValue)
    }

    override fun containsValue(keyValue: String, value: TypeValue<*, N>): Boolean {
        return getKey(keyValue)?.let {
            containsValue(it, value)
        } ?: false
    }

    override fun containsValue(key: K, value: TypeValue<*, N>): Boolean {
        return valueRepositoryProvider.provide(key).containsValue(value)
    }

    override fun clearValues(): Int {
        indexRepository.deleteAllIndex()
        return keys.sumOf {
            valueRepositoryProvider.provide(it).deleteByKey(it)
        }
    }

    override fun clearKeyValues(keyValue: String): Int {
        return getKey(keyValue)?.let { clearKeyValues(it) } ?: 0
    }

    override fun clearKeyValues(key: K): Int {
        // ??????????????????
        indexRepository.deleteByKey(key)
        // ???????????????????????????????????????????????????????????????????????????
        return valueRepositoryProvider.provide(key).deleteByKey(key)
    }

    override fun clearKey(keyValue: String, clearValues: Boolean) {
        getKey(keyValue)?.let {// ?????????
            clearKey(it, clearValues)
        }
    }

    override fun clearKey(key: K, clearValues: Boolean) {
        // ??????????????????
        indexRepository.deleteByKey(key)
        if (clearValues) {
            // ???????????????????????????????????????????????????????????????????????????
            valueRepositoryProvider.provide(key).deleteByKey(key)
        }
        // ?????????
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

    override fun getKey(keyValue: String): K? {
        return keyRepository.getKeyByKeyValue(keyValue)
    }

    override fun getKey(node: N): K? {
        return indexRepository.getKeyByIndex(node)
    }

    override fun getMutliKeys(keyValues: Collection<String>): List<K> {
        return keyRepository.getKeysByKeyValues(keyValues)
    }

    override fun getValue(index: N): TypeValue<*, N>? {
        return getKey(index)?.let { getValue(it, index) }
    }

    override fun getValue(key: K, index: N): TypeValue<*, N>? {
        return valueRepositoryProvider.provide(key).getValue(index)
    }

    override fun setValue(index: N, value: TypeValue<*, N>): Boolean {
        return getKey(index)?.let {
            setValue(it, index, value)
        } ?: throw MissingKeyException()
    }

    override fun setValue(key: K, index: N, value: TypeValue<*, N>): Boolean {
        // ??????????????????????????????
        if (!checkValueType(key, value)) {
            throw ValueTypeException(key, value)
        }
        val valueRepo = valueRepositoryProvider.provide(key)
        // ???????????????
        return if (containsValue(key, value)) {
            valueRepo.updateValue(value)
        } else {
            valueRepo.addValue(value)
        } && indexRepository.addIndex(index)
    }

    override fun getDefaultIndex(key: K): N? {
        return indexRepository.getDefaultIndex(key)
    }

    override fun getDefaultIndex(keyValue: String): N? {
        return getKey(keyValue)?.let { getDefaultIndex(it) }
    }

    override fun getDefaultValue(keyValue: String): TypeValue<*, N>? {
        return getKey(keyValue)?.let { getDefaultValue(it) }
    }

    override fun getDefaultValue(key: K): TypeValue<*, N>? {
        return indexRepository.getDefaultIndex(key)?.let {// ??????????????????
            valueRepositoryProvider.provide(key).getValue(it)
        }
    }

    override fun setDefaultValue(keyValue: String, value: TypeValue<*, N>): Boolean {
        // ???????????????
        return getKey(keyValue)?.let { key ->
            setDefaultValue(key, value)
        } ?: throw MissingKeyException("[${keyValue}]")
    }

    override fun setDefaultValue(key: K, value: TypeValue<*, N>): Boolean {
        // ??????????????????????????????
        if (!checkValueType(key, value)) {
            throw ValueTypeException(key, value)
        }
        // ?????????????????????
        val valueRepo = valueRepositoryProvider.provide(key)
        // ????????????????????????????????????
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
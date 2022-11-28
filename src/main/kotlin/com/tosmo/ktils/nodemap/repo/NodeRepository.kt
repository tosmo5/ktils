package com.tosmo.ktils.nodemap.repo

import com.tosmo.ktils.nodemap.data.TypeKey
import com.tosmo.ktils.nodemap.data.Node

/**
 * 节点存储接口
 *
 * @author Thomas Miao
 */
interface NodeRepository<K : TypeKey, N : Node> {

    /**
     * 添加节点，不需要的话返回true
     */
    fun addNode(node: N): Boolean = true

    /**
     * 删除所有节点，不需要的话返回true
     */
    fun deleteAllNode(): Boolean = true

    /**
     * 删除键的所有节点，不需要直接返回0
     */
    fun deleteByKey(key: K): Int = 0

    /**
     * 根据键取得默认节点
     */
    fun getDefaultNode(key: K): N?

    /**
     * 取得节点对应的键
     */
    fun getKeyByNode(node: N): K?
}
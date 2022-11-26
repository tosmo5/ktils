package com.tosmo.ktils.nodemap.repo

import com.tosmo.ktils.nodemap.data.Key
import com.tosmo.ktils.nodemap.data.Node

/**
 * 节点存储接口
 *
 * @author Thomas Miao
 */
interface NodeRepository<K : Key, N : Node> {

    /**
     * 根据键名取得默认节点
     */
    fun getDefaultNode(keyName: String): N?

    /**
     * 根据键名取得的所有节点
     */
    fun getNodesByKeyName(keyName: String): List<N>

    /**
     * 取得[key]的所有节点
     */
    fun getNodesByKey(key: K): List<N>

    /**
     * 取得所有节点
     */
    fun getAllNodes(): List<N>

    /**
     * 根据[node]取得键
     */
    fun getNodeKey(node: N): K?

    /**
     * 查询是否存在此节点
     */
    fun hasNode(node: N): Boolean

    /**
     * 查询节点[node]下是是否没有数据
     */
    fun isNodeNoValues(node: N): Boolean

    /**
     * 查询[node]是否存在
     */
    fun containsNode(node: N): Boolean

    /**
     * 删除[key]下的所有节点
     */
    fun deleteKeyNodes(key: K): Int
}
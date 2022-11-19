package com.tosmo.ktils

/**
 * 实体类基础接口
 *
 * T 实体类类型
 *
 * I 实体id类型
 *
 * @author Thomas Miao
 */
interface BaseMapper<T, I> {
    /**
     * 插入[obj]的数据，返回是否成功
     */
    infix fun insert(obj: T): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 根据编号删除[obj]的数据，通过主键匹配，返回是否成功
     */
    infix fun delete(obj: T): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 根据编号更新[T]的数据，通过主键匹配，返回是否成功
     */
    infix fun update(obj: T): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 通过[id]查找[T]，失败返回空
     */
    infix fun findById(id: I): T? {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 查找[id]是否存在
     */
    infix fun idExists(id: I): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 用[query]查询数据并返回[Int]或空
     *
     * @param query 原始sql语句
     */
    fun queryInt(query: String): Int? {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 用[query]查询数据并返回[Long]或空
     *
     * @param query 原始sql语句
     */
    fun queryLong(query: String): Long? {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 用[query]查询数据并返回[Float]或空
     *
     * @param query 原始sql语句
     */
    fun queryFloat(query: String): Float? {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 用[query]查询数据并返回[Double]或空
     *
     * @param query 原始sql语句
     */
    fun queryDouble(query: String): Double? {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 用[query]查询数据并返回[String]或空
     *
     * @param query 原始sql语句
     */
    fun queryString(query: String): String? {
        throw UnsupportedOperationException("未实现的功能")
    }
}
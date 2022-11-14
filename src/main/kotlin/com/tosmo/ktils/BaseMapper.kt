package com.tosmo.ktils

/**
 * 实体类基础接口
 *
 * @author Thomas Miao
 */
interface BaseMapper<T> {
    /**
     * 插入[obj]的数据，返回是否成功
     */
    fun insert(obj: T): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 插入[objs]中所有的[T]数据，返回成功的数量
     */
    fun insertAll(objs: Collection<T>): Int {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 删除[obj]的数据，通过主键匹配，返回是否成功
     */
    fun delete(obj: T): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 删除[objs]中所有[T]数据，返回删除的数量
     */
    fun deleteAll(objs: Collection<T>): Int {
        throw UnsupportedOperationException("未实现的功能")
    }

    /**
     * 更新[T]的数据，通过主键匹配，返回是否成功
     */
    fun update(obj: T): Boolean {
        throw UnsupportedOperationException("未实现的功能")
    }
}
package com.tosmo.ktils.refect

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

/**
 * 反射
 *
 * @author Thomas Miao
 */
class RefectHelper<T : Any>(val kClass: KClass<T>) {

    companion object {
        /**
         * 对比[property]是否为[typeClass]的类型
         */
        fun mergeType(property: KProperty<*>, typeClass: KClass<*>): Boolean {
            return property.returnType.isSubtypeOf(typeClass.createType())
        }

        /**
         * [property]的类型是否与[typeClasses]所有类型都匹配
         */
        fun mergeAllType(property: KProperty<*>, vararg typeClasses: KClass<*>): Boolean {
            return typeClasses.all { property.returnType.isSubtypeOf(it.createType()) }
        }

        /**
         * [property]是否与[typeClasses]中一个或多个类型匹配
         */
        fun mergeAnyType(property: KProperty<*>, vararg typeClasses: KClass<*>): Boolean {
            return typeClasses.any { property.returnType.isSubtypeOf(it.createType()) }
        }
    }

    /**
     * 本类中定义的所有属性集合，按照声明顺序排序
     */
    val declaredPropertyNames: Collection<String> = kClass.java.declaredFields.map { it.name }

    /**
     * 调用[T]的空构造函数创建一个对象，不存在空构建函数时返回空
     */
    fun createBean(): T {
        require(kClass.constructors.any { it.parameters.isEmpty() }) { "${kClass.qualifiedName} → 找不到无参构造函数" }
        return kClass.createInstance()
    }

    /**
     * 通过[args]创建一个对象
     *
     * @param args 属性名与值的映射集合
     */
    fun createBean(args: Map<String, *>): T {
        kClass.constructors.find {
            it.parameters.isEmpty()
        }
        val usedArgNames = mutableListOf<String>()
        // 构造对象
        val bean = if (kClass.constructors.any { it.parameters.isEmpty() }) {
            kClass.createInstance()
        } else {
            val initFun = kClass.constructors.find { kFunction ->
                kFunction.parameters.all { kParameter ->
                    // 所给的属性值包含了构造函数的参数 或 构造函数的参数可为空
                    kParameter.name in args || kParameter.type.isMarkedNullable
                }
            }
            requireNotNull(initFun) { "${kClass.qualifiedName} → 提供的参数不足以创建对象" }
            buildMap {
                initFun.parameters.forEach {
                    put(it, args[it.name])
                    usedArgNames.add(it.name!!)
                }
            }.let { initFun.callBy(it) }
        }

        // 设定非构造函数中的属性值
        kClass.memberProperties.filter {
            it.name in args && it.name !in usedArgNames
        }.forEach {
            if (it is KMutableProperty<*>) {
                it.setter.call(bean, args[it.name])
            }
        }

        return bean
    }
}
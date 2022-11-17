package com.tosmo.ktils

import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.superclasses

class Holder(inheritChildClass: KClass<out Any>? = null) {

    interface Server {
        infix fun register(child: Any): Holder

        infix fun deregister(child: Any): Holder
    }

    val children: MutableMap<KClass<out Any>, Any> = mutableMapOf()

    val defaults: MutableMap<KClass<out Any>, Any> = mutableMapOf()

    init {
        inheritChildClass?.let { preset(it) }
    }

    /**
     * 从[inheritChildClass]直接继承的父类中，检查其伴生对象是否为此父类的实现对象，
     * 若是则注册绑定关系，且将此实现对象做为默认实现
     */
    infix fun preset(inheritChildClass: KClass<out Any>) {
        inheritChildClass.superclasses.forEach { childClass ->
            childClass.companionObjectInstance?.let {
                if (childClass in it::class.allSuperclasses) {
                    children[childClass] = it
                    defaults[childClass] = it
                }
            }
        }
    }

    /**
     * 注册[child]
     */
    infix fun register(child: Any): Holder {
        val childAllSuperClasses = child::class.allSuperclasses
        children.keys.forEach {
            if (it in childAllSuperClasses) {
                children[it] = child
            }
        }
        return this
    }

    /**
     * 撤销注册[child]，如果存在，则变为默认
     */
    infix fun deregister(child: Any): Holder {
        children.forEach { (kClass, registedChild) ->
            if (registedChild::class == child::class) {
                children[kClass] = defaults[kClass]!!
            }
        }
        return this
    }

    inline operator fun <reified C : Any> get(kClass: KClass<C>): C {
        return children[kClass]!! as C
    }

    /**
     * 从功能库中取得功能
     */
    inline fun <reified C : Any> getOrPut(kClass: KClass<C>, defaultChild: () -> C): C {
        val child = (children[kClass] ?: defaultChild().apply {
            defaults[kClass] = this
        }) as C
        children[kClass] = child
        return child
    }
}
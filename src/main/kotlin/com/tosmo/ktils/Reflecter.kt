package com.tosmo.ktils

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

/**
 * 反射
 *
 * @author Thomas Miao
 */
object Reflecter {

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

    private fun <T : Any> tryCreateBeanByNoArgs(kClass: KClass<T>): Result<T> {
        return runCatching {
            val constructor = kClass.constructors.find {
                it.parameters.isEmpty() || it.parameters.all { p -> p.isOptional }
            }
            requireNotNull(constructor) { "没有符合的构造函数" }
            constructor.callBy(emptyMap())
        }
    }

    /**
     * 用[args]中的参数，自动匹配一个合适的构造函数并创建一个[kClass]对象
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun <T : Any> createBean(kClass: KClass<T>, autoTrim: Boolean = true, vararg args: Any?): T =
        createBean(kClass, args.toList(), autoTrim)

    /**
     * 用[args]中的参数，自动匹配一个合适的构造函数并创建一个[kClass]对象
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun <T : Any> createBean(kClass: KClass<T>, args: Collection<*>, autoTrim: Boolean = true): T {
        // 当没有给参数时，尝试调用无参构造
        if (args.isEmpty()) {
            tryCreateBeanByNoArgs(kClass).onSuccess { return it }.onFailure { throw it }
        }
        // 找出可传参数数量匹配的构造函数
        val constructors = kClass.constructors.filter {
            it.parameters.isEmpty() || it.parameters.size == args.size || (
                    it.parameters.size > args.size && it.parameters.subList(
                        args.size, it.parameters.size
                    ).all { p -> p.isOptional })
        }.toMutableList()
        // 过滤掉参数类型不匹配的构造函数，并记录参数映射
        var argsMap = mutableMapOf<KParameter, Any?>()
        val constructor = constructors.filter { initFun ->
            if (initFun.parameters.isEmpty())
                return@filter true
            for (i in args.indices) {
                val param = initFun.parameters[i]
                val arg = args.elementAt(i)
                if (arg == null) {
                    if (param.type.isMarkedNullable)
                        argsMap.putIfAbsent(param, null)
                    else
                        return@filter false
                } else {
                    if (arg::class.createType().isSubtypeOf(param.type))
                        argsMap.putIfAbsent(param, arg)
                    else
                        return@filter false
                }
            }
            true
        }.let {
            require(it.isNotEmpty()) { "没有符合的构造函数" }
            it.firstOrNull { initFun -> initFun.parameters.isNotEmpty() }
                ?: it.first()
        }
        argsMap = convertTypes(argsMap, autoTrim)
        return constructor.callBy(argsMap)
    }

    /**
     * 用[args]中的参数，自动匹配一个合适的构造函数并创建一个[kClass]对象
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun <T : Any> createBean(kClass: KClass<T>, args: Map<*, *>, autoTrim: Boolean = true): T {
        // 判断空构造函数
        if (args.isEmpty()) {
            tryCreateBeanByNoArgs(kClass).onSuccess { return it }.onFailure { throw it }
        }
        // 转换参数映射
        var argsMap = mutableMapOf<KParameter, Any?>()
        kClass.constructors.forEach { initFun ->
            initFun.parameters.forEach { kParam ->
                args[kParam.name]?.let { argsMap.putIfAbsent(kParam, it) }
            }
        }
        val constructor = kClass.constructors.filterNot { initFun ->
            initFun.parameters.any { !(it in argsMap || it.isOptional) }
        }.let {
            require(it.isNotEmpty()) { "没有符合的构造函数" }
            (it.firstOrNull { initFun -> initFun.parameters.isNotEmpty() }
                ?: it.first())
        }
        // 转换值类型
        argsMap = convertTypes(argsMap, autoTrim)
        return constructor.callBy(argsMap)
    }

    /**
     * 转换值类型
     */
    internal fun convertTypes(args: Map<KParameter, *>, autoTrim: Boolean = true): MutableMap<KParameter, Any?> {
        val argsMap = args.toMutableMap()
        return argsMap.onEach { (key, value) ->
            when (value) {
                is String -> if (autoTrim) argsMap[key] = value.trim()
                is Number -> {
                    val numberValue = argsMap[key] as Number
                    argsMap[key] = when (key.type.jvmErasure) {
                        Byte::class -> numberValue.toByte()
                        UByte::class -> numberValue.toLong().toUByte()
                        Short::class -> numberValue.toShort()
                        UShort::class -> numberValue.toLong().toUShort()
                        Int::class -> numberValue.toInt()
                        UInt::class -> numberValue.toLong().toUInt()
                        Long::class -> numberValue.toLong()
                        ULong::class -> numberValue.toString().toULong()
                        Float::class -> numberValue.toFloat()
                        Double::class -> numberValue.toDouble()
                        BigDecimal::class -> numberValue.toString().toBigDecimal()
                        BigInteger::class -> numberValue.toString().toBigInteger()
                        String::class -> numberValue.toString()
                        Date::class -> Date(numberValue.toLong())
                        LocalDate::class -> TimeUtils.format(Date(numberValue.toLong())).let {
                            TimeUtils.parseLocalDateTime(it).toLocalDate()
                        }

                        LocalTime::class -> TimeUtils.format(Date(numberValue.toLong())).let {
                            TimeUtils.parseLocalTime(it)
                        }

                        LocalDateTime::class -> TimeUtils.format(Date(numberValue.toLong())).let {
                            TimeUtils.parseLocalDateTime(it)
                        }

                        else -> numberValue
                    }
                }
            }
        }
    }

    /**
     * 用[args]中的参数，自动匹配一个合适的构造函数并创建一个[kClass]对象，创建失败返回空
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun <T : Any> createBeanOrNull(kClass: KClass<T>, args: Collection<*>, autoTrim: Boolean = true): T? {
        return try {
            createBean(kClass, args, autoTrim)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 用[args]中的参数，自动匹配一个合适的构造函数并创建一个[kClass]对象，创建失败返回空
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun <T : Any> createBeanOrNull(kClass: KClass<T>, autoTrim: Boolean = true, vararg args: Any?): T? {
        return try {
            createBean(kClass, autoTrim, *args)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 用[args]中的参数，自动匹配一个合适的构造函数并创建一个[kClass]对象，创建失败返回空
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun <T : Any> createBeanOrNull(kClass: KClass<T>, args: Map<*, *>, autoTrim: Boolean = true): T? {
        return try {
            createBean(kClass, args, autoTrim)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 将对象[obj]中所有的公开属性映射为[Map]并返回
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun mapProps(obj: Any, autoTrim: Boolean = true): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        obj::class.memberProperties.filter { it.visibility == KVisibility.PUBLIC }.forEach {
            map[it.name] = it.getter.call(obj)
        }
        if (autoTrim) {
            map.forEach { (key, value) ->
                if (value is String) {
                    map[key] = value.trim()
                }
            }
        }
        return map
    }

    /**
     * 将数据类对象[obj]主构造方法中的公开属性映射为[Map]并返回
     *
     * @param autoTrim 自动去除开头与结尾的空白字符，默认：true
     */
    fun mapParams(obj: Any, autoTrim: Boolean = true): MutableMap<String, Any?> {
        val kClass = obj::class
        require(kClass.isData) { "${kClass}不是data class" }
        val map = mutableMapOf<String, Any?>()
        kClass.primaryConstructor?.parameters?.forEach {
            map[it.name!!] = null
        }
        kClass.memberProperties.filter { it.visibility == KVisibility.PUBLIC }.forEach {
            map[it.name] = it.getter.call(obj)
        }
        if (autoTrim) {
            map.forEach { (key, value) ->
                if (value is String) {
                    map[key] = value.trim()
                }
            }
        }
        return map
    }

    /**
     * 将[obj]中的属性复制到新的[target]对象中
     */
    fun <T : Any> transfer(obj: Any, target: KClass<T>): T {
        return createBean(target, mapProps(obj))
    }

    /**
     * 根据[propertyName]取得[obj]中对应名称的属性的值
     */
    fun <T : Any> getPropertyValue(obj: Any, propertyName: String): Any? {
        return obj::class.declaredMemberProperties.find {
            it.visibility == KVisibility.PUBLIC && it.name == propertyName
        }?.getter!!.call(obj)
    }
}
package com.tosmo.ktils.serializer

import com.tosmo.ktils.TimeUtils

/**
 * 序列化配置类
 *
 * @author Thomas Miao
 */
class SerializerConfig internal constructor() {
    /**
     * 日期格式
     */
    var datePattern: String = TimeUtils.defaultDateFormat

    /**
     * 时间格式
     */
    var timePattern: String = TimeUtils.defaultTimeFormat

    companion object {
        val GLOBAL_CONFIG = SerializerConfig()
    }
}

/**
 * 设置序列化
 */
fun serializerSet(block: (config: SerializerConfig) -> Unit) {
    block(SerializerConfig.GLOBAL_CONFIG)
}
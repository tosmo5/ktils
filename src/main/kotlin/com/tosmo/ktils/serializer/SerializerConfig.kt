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
    var datePattern: String = TimeUtils.DP_YEAR_MONTH_DAY

    /**
     * 日期时间格式
     */
    var dateTimePattern: String = TimeUtils.DP_FULL

    /**
     * 时间格式
     */
    var timePattern: String = TimeUtils.TP_NORMAL

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
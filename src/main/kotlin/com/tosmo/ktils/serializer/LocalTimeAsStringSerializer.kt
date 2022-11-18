package com.tosmo.ktils.serializer

import com.tosmo.ktils.TimeUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

/**
 *
 * @author Thomas Miao
 */
object LocalTimeAsStringSerializer : KSerializer<LocalTime> {

    val pattern = SerializerConfig.GLOBAL_CONFIG.timePattern

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalTime {
        return TimeUtils.parseLocalTime(decoder.decodeString(), pattern)
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(TimeUtils.format(value, pattern))
    }
}
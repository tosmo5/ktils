package com.tosmo.ktils.serializer

import com.tosmo.ktils.TimeUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

/**
 *
 * @author Thomas Miao
 */
object LocalDateAsStringSerializer : KSerializer<LocalDate> {

    val pattern
        get() = SerializerConfig.GLOBAL_CONFIG.datePattern

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return TimeUtils.parseLocalDateTime(decoder.decodeString(), pattern).toLocalDate()
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(TimeUtils.format(value, pattern))
    }
}
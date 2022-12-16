package com.tosmo.ktils.serializer

import com.tosmo.ktils.TimeUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

object LocalDateTimeAsStringSerializer : KSerializer<LocalDateTime> {

    val pattern
        get()= SerializerConfig.GLOBAL_CONFIG.dateTimePattern

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime =
        TimeUtils.parseLocalDateTime(decoder.decodeString(), pattern)


    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(TimeUtils.format(value, pattern))
    }
}
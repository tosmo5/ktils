package com.tosmo.ktils.serializer

import com.tosmo.ktils.TimeUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object DateAsStringSerializer : KSerializer<Date> {

    val pattern
        get() = SerializerConfig.GLOBAL_CONFIG.datePattern

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        return TimeUtils.parseDate(decoder.decodeString(), pattern)
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(TimeUtils.format(value, pattern))
    }
}
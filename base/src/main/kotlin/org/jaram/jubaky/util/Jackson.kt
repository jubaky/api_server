package org.jaram.jubaky.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime

object Jackson {

    private val dateTimeConvertModule = SimpleModule()
        .addSerializer(DateTime::class.java, DateTimeSerializer())
        .addDeserializer(DateTime::class.java, DateTimeDeserializer())

    val mapper = jacksonObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .setDateFormat(TimestampDateFormat())
        .registerModule(dateTimeConvertModule)

    class DateTimeSerializer : JsonSerializer<DateTime>() {

        override fun serialize(value: DateTime, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeNumber(value.toDate().time)
        }
    }

    class DateTimeDeserializer : JsonDeserializer<DateTime>() {

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): DateTime {
            return DateTime(p.valueAsLong)
        }
    }
}
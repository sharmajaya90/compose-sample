package com.service.composesample.extensions

import com.google.gson.*
import com.service.composesample.utils.CoreJsonString
import com.service.composesample.utils.DisplayableDateDMY
import java.lang.reflect.Type


fun Any.provideGsonWithCoreJsonString(): Gson {
    return GsonBuilder().also {
        it.registerTypeAdapter(
            CoreJsonString::class.java,
            object : JsonDeserializer<CoreJsonString?> {
                override fun deserialize(
                    json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
                ): CoreJsonString? {
                    return json?.toString()?.let { time ->
                        CoreJsonString(time)
                    } ?: kotlin.run { null }
                }

            })
        it.registerTypeAdapter(
            CoreJsonString::class.java,
            object : JsonSerializer<CoreJsonString?> {
                override fun serialize(
                    src: CoreJsonString?, typeOfSrc: Type?, context: JsonSerializationContext?
                ): JsonElement {
                    val parser = JsonParser()
                    return parser.parse(src?.data)
                }
            })
    }.create()
}

fun provideGsonDate(): Gson {
    return GsonBuilder().also {
        it.registerTypeAdapter(DisplayableDateDMY::class.java, object : JsonDeserializer<DisplayableDateDMY?> {
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DisplayableDateDMY? {
                return json?.asJsonPrimitive?.asString?.let { time ->
                    DisplayableDateDMY(time)
                } ?: kotlin.run { null }
            }
        })
    }.create()
}
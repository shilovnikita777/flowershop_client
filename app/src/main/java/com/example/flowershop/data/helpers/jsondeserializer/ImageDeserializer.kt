package com.example.flowershop.data.helpers.jsondeserializer

import com.example.flowershop.domain.model.*
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ImageDeserializer : JsonDeserializer<Image> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Image {
        val image = json?.asString ?: ""

        return Image(image)
    }
}
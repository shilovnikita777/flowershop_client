package com.example.flowershop.data.helpers.jsonserializer

import com.example.flowershop.domain.model.Flower
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class FlowerSerializer : JsonSerializer<Flower> {
    override fun serialize(
        src: Flower?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("id", src?.id)

        return jsonObject
    }
}
package com.example.flowershop.data.helpers.jsondeserializer

import android.util.Log
import com.example.flowershop.domain.model.Flower
import com.example.flowershop.domain.model.Image
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class FlowerDeserializer : JsonDeserializer<Flower> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Flower {
        val gson = GsonBuilder()
            .registerTypeAdapter(Image::class.java,ImageDeserializer())
            .create()

        val jsonObject = json!!.asJsonObject
        Log.d("xd12",jsonObject.toString())

        val id = jsonObject["id"]?.asInt ?: -1
        val name = jsonObject["name"]?.asString ?: ""
        val price = jsonObject["price"]?.asInt ?: 0
        val image = gson.fromJson(jsonObject["image"], Image::class.java) ?: Image("")
        val description = jsonObject["description"]?.asString ?: ""
        val type = jsonObject["type"]?.asString ?: ""
        val smallImage = gson.fromJson(jsonObject["smallImage"], Image::class.java) ?: Image("")
        val sort = jsonObject["sort"]?.asString ?: ""
        val categoriesIds = jsonObject["categoriesIds"].asJsonArray?.map { it.asInt } ?: emptyList()

        return Flower(
            id = id,
            name = name,
            price = price,
            image = image,
            description = description,
            type = type,
            smallImage = smallImage,
            sort = sort,
            categoriesIds = categoriesIds
        )
    }
}
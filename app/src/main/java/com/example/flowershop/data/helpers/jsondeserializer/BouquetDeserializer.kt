package com.example.flowershop.data.helpers.jsondeserializer

import com.example.flowershop.domain.model.*
import com.example.flowershop.presentation.model.BouquetSize
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class BouquetDeserializer : JsonDeserializer<Bouquet> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Bouquet {
        val gson = GsonBuilder()
            .registerTypeAdapter(Flower::class.java, FlowerDeserializer())
            .registerTypeAdapter(Image::class.java,ImageDeserializer())
            .create()

        val jsonObject = json!!.asJsonObject

        val id = jsonObject["id"].asInt
        val name = jsonObject["name"].asString
        val price = jsonObject["price"]?.asInt ?: 0
        val image = gson.fromJson(jsonObject["image"], Image::class.java) ?: Image("")
        val description = jsonObject["description"].asString
        val type = jsonObject["type"].asString
        val categoriesIds = jsonObject["categoriesIds"]?.asJsonArray?.map { it.asInt } ?: emptyList()
//        val flowers = jsonObject["flowers"].asJsonArray?.map {
//            it.
//        }
        val decoration = gson.fromJson(jsonObject["decoration"]?.asJsonObject, Decoration::class.java) ?: Decoration()
        val table = gson.fromJson(jsonObject["table"]?.asJsonObject, Table::class.java) ?: Table()
        val postcard = jsonObject["postcard"].asStringOrDefault("")

        val flowers = jsonObject["flowers"].asJsonArray?.map {
            val flowerWithCountJson = it!!.asJsonObject
            ProductWithCount(
                product = gson.fromJson(flowerWithCountJson["product"].asJsonObject, Flower::class.java),
                count = flowerWithCountJson["count"].asInt
            )

        } ?: emptyList()
        val bouquetSize = when(jsonObject["size"]?.asString) {
            "small" -> {
                BouquetSize.Small
            }
            "medium" -> {
                BouquetSize.Medium
            }
            "big" -> {
                BouquetSize.Big
            }
            "large" -> {
                BouquetSize.Large
            }
            else -> {
                BouquetSize.Small
            }
        }

        return Bouquet(
            id = id,
            name = name,
            price = price,
            image = image,
            description = description,
            type = type,
            categoriesIds = categoriesIds,
            flowers = flowers,
            size = bouquetSize,
            decoration = decoration,
            table = table,
            postcard = postcard
        )
    }
}
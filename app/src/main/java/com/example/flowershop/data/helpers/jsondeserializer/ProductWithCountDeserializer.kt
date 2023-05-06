package com.example.flowershop.data.helpers.jsondeserializer

import android.util.Log
import com.example.flowershop.domain.model.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ProductWithCountDeserializer : JsonDeserializer<ProductWithCount> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ProductWithCount {
        val gson = GsonBuilder()
            .registerTypeAdapter(Flower::class.java, FlowerDeserializer())
            .registerTypeAdapter(Bouquet::class.java, BouquetDeserializer())
            .registerTypeAdapter(Image::class.java,ImageDeserializer())
            .create()

        val productWithCountJson = json!!.asJsonObject

        val productJson = productWithCountJson["product"].asJsonObject
        val type = productJson["type"].asString

        if (type == "bouquet") {
            val decoration = gson.fromJson(productWithCountJson["decoration"]?.asJsonObject, Decoration::class.java) ?: Decoration()
            val table = gson.fromJson(productWithCountJson["table"]?.asJsonObject, Table::class.java) ?: Table()
            val postcard = productWithCountJson["postcard"].asStringOrDefault("")

            val bouquet = gson.fromJson(productJson,Bouquet::class.java)
            bouquet.decoration = decoration
            bouquet.table = table
            bouquet.postcard = postcard

            return ProductWithCount(
                product = bouquet,
                count = productWithCountJson["count"].asInt
            )
        } else if (type == "flower") {

            val decoration = gson.fromJson(productWithCountJson["decoration"]?.asJsonObject, Decoration::class.java) ?: Decoration()

            return FlowersWithDecoration(
                product = gson.fromJson(productJson,Flower::class.java),
                count = productWithCountJson["count"].asInt,
                decoration = decoration
            )
        } else {
            return ProductWithCount(
                product = gson.fromJson(productJson,Product::class.java),
                count = productWithCountJson["count"].asInt
            )
        }

//        val productJson = jsonObject["product"]!!.asJsonObject
//
//        val productType = productJson["type"]?.asString ?: "product"
//
//        val id = productJson["id"]?.asInt ?: -1
//        val name = productJson["name"]?.asString ?: ""
//        val price = productJson["price"]?.asInt ?: 0
//        val image = productJson["image"]?.asString ?: ""
//        val description = productJson["description"]?.asString ?: ""
//        val type = productJson["type"]?.asString ?: ""
//        val categoriesIds = productJson["categoriesIds"].asJsonArray?.map { it.asInt } ?: emptyList()
//
//        val count = jsonObject["count"]?.asInt ?: 1
//
//        when(productType) {
//            "bouquet" -> {
//
//            }
//            "flower" -> {
//
//            }
//            else -> {
//
//            }
//        }
//
//
//        val smallImage = jsonObject["smallImage"]?.asString ?: ""
//        val sort = jsonObject["sort"]?.asString ?: ""
    }
}

fun JsonElement?.asStringOrDefault(defaultValue: String): String {
    return if (this != null && !this.isJsonNull) {
        this.asString
    } else {
        defaultValue
    }
}
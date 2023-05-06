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

//class CategoryDeserializer : JsonDeserializer<Category> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): Category {
//        val jsonObject = json!!.asJsonObject
//
//        val id = jsonObject["id"]?.asInt ?: -1
//        val name = jsonObject["name"]?.asString ?: ""
//        val image = jsonObject["image"]?.asString ?: ""
//
//        return Category(
//            id = id,
//            name = name,
//            image = Image(image)
//        )
//    }
//}

//class DecorationDeserializer : JsonDeserializer<Decoration> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): Decoration {
//        val jsonObject = json!!.asJsonObject
//
//        val id = jsonObject["id"]?.asInt ?: -1
//        val title = jsonObject["name"]?.asString ?: ""
//        val price = jsonObject["price"]?.asInt ?: 0
//        val image = jsonObject["image"]?.asString ?: ""
//
//        return Decoration(
//            id = id,
//            title = title,
//            price = price,
//            image = Image(image)
//        )
//    }
//}
//
//class TableDeserializer : JsonDeserializer<Table> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): Table {
//        val jsonObject = json!!.asJsonObject
//
//        val id = jsonObject["id"]?.asInt ?: -1
//        val text = jsonObject["name"]?.asString ?: ""
//        val price = jsonObject["price"]?.asInt ?: 0
//        val image = jsonObject["image"]?.asString ?: ""
//
//        return Table(
//            id = id,
//            text = text,
//            price = price,
//            image = Image(image)
//        )
//    }
//}
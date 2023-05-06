package com.example.flowershop.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Flower(
    override var id: Int = -1,
    override val price: Int = 0,
    override val rating: Rating = Rating(),
    override val image: Image = Image(""),
    override val name: String = "",
    override val description: String = "",
    override val categoriesIds: List<Int> = emptyList(),
    override val type : String = "flower",
    val smallImage: Image = Image(""),
    val sort: String = ""
) : Product(id,price,rating,image,name,description,categoriesIds,type)
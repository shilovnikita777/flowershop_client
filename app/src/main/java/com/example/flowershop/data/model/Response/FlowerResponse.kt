package com.example.flowershop.data.model.Response

class FlowerResponse (
    val id: Int,
    val price: Int,
    val image: String,
    val name: String,
    val description: String,
    val categoriesIds: List<Int>,
    val type : String,
    val smallImage: String,
    val sort: String
)
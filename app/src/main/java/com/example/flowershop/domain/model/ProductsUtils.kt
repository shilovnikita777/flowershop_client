package com.example.flowershop.domain.model

data class Decoration(
    val id : Int = 1,
    val title: String = "",
    val price: Int = 0,
    val image: Image = Image("")
)

data class Table(
    val id : Int = 1,
    val text: String = "",
    val price: Int = 0
)

data class Promocode(
    val title: String,
    val description: String,
    val value: String,
    val amount: Int,
    val image: Image = Image("")
)

class Rating(
    var value: Double = 0.0,
    var votedCount: Int = 0
)